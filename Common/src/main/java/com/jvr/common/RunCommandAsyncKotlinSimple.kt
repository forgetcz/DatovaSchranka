package com.jvr.common

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.core.util.Consumer
import androidx.core.util.Supplier

public class RunCommandAsyncKotlinSimple<T>(
    activity: Context?, message: String?
    , mainMethod: Supplier<T>
    , callback: Consumer<T>?)
    : AsyncTask<Void, Void, T>() {

    private lateinit var dialog: ProgressDialog
    private var method: Supplier<T>
    private var callback: Consumer<T>? = null
    private var message: String? = null
    private var showMessage: Boolean = false
    private var finished: Boolean = false

    init {
        showMessage = activity != null && message != null && message.isNotEmpty()

        if (showMessage) {
            dialog = ProgressDialog(activity, ProgressDialog.STYLE_HORIZONTAL)
            dialog.setCancelable(false)
            dialog.setTitle("Wait please!")
        }

        this.method = mainMethod
        this.callback = callback
        this.message = message
        this.finished = false

    }

    /**
     * Show dialog window -  This method is called before task execution on UI thread.
     */
    @Deprecated("Deprecated in Java", ReplaceWith("super.onPreExecute()", "android.os.AsyncTask"))
    override fun onPreExecute() {
        //super.onPreExecute()
        if (showMessage) {
            dialog.setMessage(message)
            dialog.show()
        }
    }

    /**
     *  This method is called just after execution of onPreExecute() in background thread.
     * @return T
     */
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg p0: Void?): T {
        //return voids[0].get();
        return try {
            method.get()
        } catch (ex: Exception) {
            Log.e("", ex.message!!)
            throw ex
        }
        //publishProgress(); -> call onProgressUpdate
    }

    /**
     * Destroy window -  This method is called on UI thread just after completion of doInBackground(Params...) and the result is passed to onPostExecute(Result).
     * @param result T
     */
    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: T) {
        //super.onPostExecute(result)
        // do UI work here

        // do UI work here
        if (dialog != null && dialog.isShowing) {
            dialog.dismiss()
        }
        if (callback != null) {
            callback!!.accept(result)
        }
        finished = true
    }

    /**
     * his method is called on UI thread after calling publishProgress(Progress...) within the doInBackground(Params...) for any status update like progress bar.
     * @param values Void
     */
    @Deprecated("Deprecated in Java",
        ReplaceWith("super.onProgressUpdate(*values)", "android.os.AsyncTask")
    )
    override fun onProgressUpdate(vararg values: Void?) {
        super.onProgressUpdate(*values)
    }
}