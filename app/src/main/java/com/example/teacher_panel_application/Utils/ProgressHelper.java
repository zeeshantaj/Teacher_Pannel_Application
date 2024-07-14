package com.example.teacher_panel_application.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.teacher_panel_application.R;

public class ProgressHelper {
    private static ProgressDialog dialog;

    public static void showDialog(Context context,String title, String message) {
        if (dialog == null) {
            dialog = new ProgressDialog(context, R.style.CustomProgressDialog);
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setCancelable(false);
            dialog.show();
        }
    }
    public static  void dismissDialog(){
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }
}
