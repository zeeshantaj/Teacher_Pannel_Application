package com.example.teacher_panel_application.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.teacher_panel_application.R;

public class ClassEnded_NotificationService extends Service {
    public static final String ACTION_START_ALARM = "START_ALARM";
    public static final String ACTION_STOP_ALARM = "STOP_ALARM";
    public static final int NotificationID =123;
    public static boolean isForeground = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void setNotification(Context context){

//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Intent stopIntent = new Intent(context, NotificationBroadcastReceiver.class);
//        stopIntent.setAction("STOP_ALARM"); // Use a unique action string
//
//        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarm_channel")
//                .setContentTitle("Alarm")
//                .setContentText("Alarm is ringing!")
//                .setSmallIcon(R.drawable.baseline_alarm_on_24)
//                .addAction(R.drawable.baseline_alarm_off_24, "Stop", stopPendingIntent) // Add a "Stop" action
//                .setAutoCancel(true);
//        Log.e("MyApp", "stopCLicked"+stopIntent.getAction());
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("alarm_channel", "Alarm", NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(channel);
//        }
//        notificationManager.notify(NotificationID, builder.build()); // Use a unique notification ID
    }
}
