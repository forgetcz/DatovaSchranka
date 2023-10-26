package com.jvr.common.lib.logger

import android.app.AlertDialog
import android.content.DialogInterface
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.jvr.common.contracts.BaseActivityClass
import com.jvr.common.contracts.ILogger
import com.jvr.common.contracts.ILogger.Companion.errMessageNoContext
import com.jvr.common.contracts.ILogger.Companion.errMessageNotTargeted


class DialogLogger : ILogger {

    override fun d(message: String) {
        Log.d(getTag(), errMessageNotTargeted)
    }
    override fun d(tag: String, message: String) {
        Log.d(tag, errMessageNotTargeted)
    }
    override fun d(context: BaseActivityClass, message: String) {
        Log.d(context.getTag(), errMessageNotTargeted)
    }

    override fun i(message: String) {
        Log.d(getTag(), errMessageNoContext)
    }
    override fun i(tag: String, message: String) {
        Log.d(tag, errMessageNoContext)
    }
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

    override fun w(message: String) {
        Log.d(getTag(), errMessageNoContext)
    }
    override fun w(tag: String, message: String) {
        Log.d(tag, errMessageNoContext)
    }
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

    override fun e(message: String) {
        Log.d(getTag(), errMessageNoContext)
    }
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
        if (message.message != null) {
            e(context, message.message!!)
        } else {
            e(context, "Unknown message")
        }
    }

    override fun e(tag: String, message: Exception) {
        Log.d(getTag(), errMessageNoContext)
    }
    override fun e(message: Exception) {
        Log.d(getTag(), errMessageNoContext)
    }
}
