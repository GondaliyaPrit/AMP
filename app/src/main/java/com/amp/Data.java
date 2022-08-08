package com.amp;

import android.app.ProgressDialog;
import android.content.Context;

public class Data {
    public static ProgressDialog PROGRESS_DIALOG = null;

    public static void showdialog(Context context, String message)
    {
        PROGRESS_DIALOG = new ProgressDialog(context);
        PROGRESS_DIALOG.show();
        if (message != null) {
            PROGRESS_DIALOG.setMessage(message);
        }
    }
    public static void dissmissdialog() {
        PROGRESS_DIALOG.dismiss();

    }
}