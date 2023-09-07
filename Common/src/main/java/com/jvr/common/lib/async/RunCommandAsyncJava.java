package com.jvr.common.lib.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.core.util.Supplier;

/**
 * https://stackoverflow.com/questions/10446125/how-to-show-progress-dialog-in-android/10446165
 * https://stackoverflow.com/questions/29945627/java-8-lambda-void-argument
 * https://www.concretepage.com/android/android-asynctask-example-with-progress-bar
 * 1 - vstupni parametry
 * 2 - progress bar -> U SQL neni mozne zavest
 * 3 - Vystupni parametry
 */
public class RunCommandAsyncJava<T> extends AsyncTask<Void, Void, T> {
    private ProgressDialog dialog;
    private Supplier<T> method;
    private Consumer<T> callback;
    private String message;
    private Boolean showMessage;
    public Boolean finished;

    public RunCommandAsyncJava(@Nullable Context activity, @Nullable String message
            , @NonNull Supplier<T> mainMethod
            , @Nullable Consumer<T> callback) {
        super();
        showMessage = activity != null && message != null && message.length() > 0;

        if (showMessage) {
            dialog = new ProgressDialog(activity, ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(false);
            dialog.setTitle("Wait please!");
        }

        this.method = mainMethod;
        this.callback = callback;
        this.message = message;
        this.finished = false;
    }

    /**
     * Show dialog window -  This method is called before task execution on UI thread.
     */
    @Override
    protected void onPreExecute() {
        if (showMessage) {
            dialog.setMessage(message);
            dialog.show();
        }
    }

    /**
     *  This method is called just after execution of onPreExecute() in background thread.
     * @return T
     */
    @Override
    protected T doInBackground(Void... params) {
        //return voids[0].get();
        try {
            return method.get();
        } catch(Exception ex) {
            Log.e("",ex.getMessage());
            return null;
        }
        //publishProgress(); -> call onProgressUpdate
    }

    /**
     * Destroy window -  This method is called on UI thread just after completion of doInBackground(Params...) and the result is passed to onPostExecute(Result).
     * @param result T
     */
    @Override
    protected void onPostExecute(T result) {
        // do UI work here
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (callback != null) {
            callback.accept(result);
        }
        finished = true;
    }

    /**
     * his method is called on UI thread after calling publishProgress(Progress...) within the doInBackground(Params...) for any status update like progress bar.
     * @param values Void
     */
    @Override
    protected void onProgressUpdate(Void... values) { super.onProgressUpdate(values); }
}
