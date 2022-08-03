package com.amp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;

public class Utils {
    private static Utils sInstance;
    public static AlertDialog.Builder dialog;

    public static Utils getInstance() {
        if (sInstance == null) {
            sInstance = new Utils();
        }

        return sInstance;
    }



    public static void erroraleart(Context context, String message, String txtpositive, DialogInterface.OnClickListener onClickListener) {
        dialog = new AlertDialog.Builder(context);
        dialog.setMessage(message).setPositiveButton(txtpositive, onClickListener)
                .show();
    }

    public boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
