package com.jvr.common.lib.logger

import android.app.AlertDialog
import android.content.DialogInterface
import android.text.Html
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.jvr.common.contracts.BaseActivityClass
import com.jvr.common.contracts.ILogger


class DialogLogger : ILogger {
    override fun getTag(): String { return javaClass.name }

    override fun d(context: BaseActivityClass, message: String) {}
    override fun d(Tag: String, message: String) {}
    override fun i(context: BaseActivityClass, message: String) {
        context.runOnUiThread {
            Toast.makeText(
                context,
                HtmlCompat.fromHtml(
                    "<font color='green'>$message</font>",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                ), Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun i(Tag: String, message: String) {}
    override fun w(context: BaseActivityClass, message: String) {
        context.runOnUiThread {
            AlertDialog.Builder(context)
                .setTitle("Warning") //<font color='#e9e91e'>"
                .setMessage(Html.fromHtml(message))
                .setCancelable(false)
                .setPositiveButton(
                    "OK"
                ) { dialog: DialogInterface, id: Int -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    override fun w(Tag: String, message: String) {}
    override fun e(context: BaseActivityClass, message: String) {
        context.runOnUiThread {
            AlertDialog.Builder(context)
                .setTitle("Error !")
                .setMessage(Html.fromHtml("<font color='#E91E63'>$message</font>"))
                .setCancelable(false)
                .setPositiveButton(
                    "OK"
                ) { dialog: DialogInterface, id: Int -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    override fun e(context: BaseActivityClass, message: Exception) {
        e(context, message.message!!)
    }

    override fun e(Tag: String, message: Exception) {}
}
