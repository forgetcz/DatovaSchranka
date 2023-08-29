package com.jvr.common

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log

class RunCommandAsyncKotlin<T, K, M>(activity: Context?, message: String?
                                     , mainMethod: ((input : K?) -> T?)?//Supplier<T>
                                     , callback : ((input:T?) -> M?)?)//Consumer<T?>?
    : AsyncTask<K, Long, T>() {

    private var dialog: ProgressDialog? = null
    private var method: ((input : K?) -> T?)?
    private var callback: ((input:T?) -> M?)?
    private var message: String? = null
    private var showProgressBar: Boolean = false
    //private var finished: Boolean = false

    init {
        showProgressBar = activity != null && message != null && message.isNotEmpty()

        if (showProgressBar) {
            dialog = ProgressDialog(activity, ProgressDialog.STYLE_HORIZONTAL)
            dialog?.setCancelable(false)
            dialog?.setTitle("Wait please!")
        }

        this.method = mainMethod
        this.callback = callback
        this.message = message
        //this.finished = false

    }

    /**
     * Show dialog window -  This method is called before task execution on UI thread.
     */
    @Deprecated("Deprecated in Java", ReplaceWith("super.onPreExecute()", "android.os.AsyncTask"))
    override fun onPreExecute() {
        //super.onPreExecute()
        if (showProgressBar) {
            dialog?.setMessage(message)
            dialog?.show()
        }
    }

    /**
     *  This method is called just after execution of onPreExecute() in background thread.
     * @return T
     */
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg p0: K?): T? {
        //return voids[0].get();
        return try {
            //method.get()
            if (method != null) {
                if (p0.isNotEmpty()) {
                    method!!(p0[0]);
                } else {
                    method!!(null)
                }
            } else {
                return null
            }
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
    override fun onPostExecute(result: T?) : Unit {
        // super.onPostExecute(result)
        // do UI work here

        // do UI work here
        if (showProgressBar) {
            val d = dialog;
            if (d != null && d.isShowing) {
                d.dismiss()
            }
        }

        if (callback != null) {
            callback?.invoke(result)
        }
        //finished = true
    }

    /**
     * his method is called on UI thread after calling publishProgress(Progress...) within the doInBackground(Params...) for any status update like progress bar.
     * @param values Void
     */
    @Deprecated("Deprecated in Java",
        ReplaceWith("super.onProgressUpdate(*values)", "android.os.AsyncTask")
    )
    override fun onProgressUpdate(vararg values: Long?) : Unit {
        super.onProgressUpdate(values[0])
    }
}
