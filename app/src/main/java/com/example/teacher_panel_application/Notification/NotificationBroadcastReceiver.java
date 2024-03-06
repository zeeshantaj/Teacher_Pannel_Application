package com.example.teacher_panel_application.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.teacher_panel_application.Home.Home_Activity;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MyApp", "onReceive called in broadcast");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(notificationManager);

        Intent notificationIntent = new Intent(context, Home_Activity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Posted class has ended")
                //.setContentText("Your posted class has been removed as per\n your given time")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Your posted class has been removed as per your given time"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        // Show the notification
        notificationManager.notify(0, builder.build());

        String uid = intent.getStringExtra("CurrentUID");
        String name = intent.getStringExtra("name");
        String dep = intent.getStringExtra("dep");
        String loc = intent.getStringExtra("loc");
        String sub = intent.getStringExtra("sub");
        String topic = intent.getStringExtra("topic");
        String min = intent.getStringExtra("min");
        String dateTime = intent.getStringExtra("dateTime");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Teacher_Data").child(uid);
        reference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UploadClassModel uploadClassModel = new UploadClassModel(name, dep, loc, sub, topic, min, dateTime);
                DatabaseReference addToQueueReference = FirebaseDatabase.getInstance().getReference("PostedData").child(uid).child(dateTime);
                addToQueueReference.setValue(uploadClassModel).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(context, "uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        if (reference.removeValue().isSuccessful()) {
            Toast.makeText(context, "Value removed", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
