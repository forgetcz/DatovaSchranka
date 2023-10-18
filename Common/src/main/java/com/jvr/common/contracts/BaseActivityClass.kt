package com.jvr.common.contracts

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.jvr.common.lib.logger.BasicLogger
import com.jvr.common.lib.logger.ComplexLogger
import com.jvr.common.lib.logger.HistoryLogger
import java.util.*


abstract class BaseActivityClass: AppCompatActivity(), IGetTag {

    @Suppress("PropertyName")
    open val Log: ILogger = ComplexLogger(
        mutableListOf(
            BasicLogger(), HistoryLogger()
        )
    )

    /*
    * https://stackoverflow.com/questions/2900023/change-app-language-programmatically-in-android
    * */
    fun setLocale(activity: Activity, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun setLocale2(activity: Activity, languageCode: String) {
        val config = resources.configuration
        val lang = "fa" // your language code
        val locale = Locale(lang)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            config.setLocale(locale)
        else
            config.locale = locale

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)

        //this.setContentView(R.layout.main)
    }

    fun getLocale(): Locale {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        var lang = sharedPreferences.getString("language", "en")
        when (lang) {
            "English" -> lang = "en"
            "Spanish" -> lang = "es"
        }
        return Locale(lang)
    }

    open fun yesNoDialog(cancelable: Boolean, callBack : ((result:Boolean)->Any)?) {
        /*val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT //| InputType.TYPE_TEXT_VARIATION_PASSWORD
        if (defaultText != null && defaultText.isNotEmpty()) {
            input.setText(defaultText)
        }*/

        AlertDialog.Builder(this)
            .setMessage("Are you sure?")
            //.setView(input) //.setMessage("(CODE-93)")//Html.fromHtml("<font color='#e9e91e'></font>")
            .setCancelable(cancelable)
            .setPositiveButton("Yes") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                if (callBack != null) {
                    callBack(true)
                }
            }
            .setNegativeButton(
                "No"
            ) { dialog: DialogInterface, _: Int ->
                dialog.dismiss() //cancel()
                if (callBack != null) {
                    callBack(false)
                }
            }
            .create()
            .show()

    }
}