package com.example.teacher_panel_application.Network;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.teacher_panel_application.Utils.MethodsUtils;

public class NetworkCheckReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        View rootView = ((Activity) context).getWindow().getDecorView();
        MethodsUtils.checkInternet(context,rootView);
    }
}
