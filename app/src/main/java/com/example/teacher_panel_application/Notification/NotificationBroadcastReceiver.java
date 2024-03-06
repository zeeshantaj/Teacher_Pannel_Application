package com.example.teacher_panel_application.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent  intent) {
        Log.e("MyApp","onReceive called in broadcast");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(notificationManager);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Posted class has ended")
                .setContentText("Your posted class has been removed as per\n your given time")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
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
        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    UploadClassModel uploadClassModel = new UploadClassModel(name, dep, loc, sub, topic, min,dateTime);
                    DatabaseReference addToQueueReference = FirebaseDatabase.getInstance().getReference("PostedData").child(uid).child(dateTime);
                    addToQueueReference.setValue(uploadClassModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(context, "uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        if (reference.removeValue().isSuccessful()){
            Toast.makeText(context, "Value removed", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationChannel(NotificationManager notificationManager){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
