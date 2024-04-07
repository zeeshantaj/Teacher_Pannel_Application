package com.example.teacher_panel_application.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

public class NetworkUtils {
    public static void hasInternetAccess(InternetAccessCallBack callback) {
        new InternetAccessTask(callback).execute();
    }
}
