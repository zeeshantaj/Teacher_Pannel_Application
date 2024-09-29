package com.example.teacher_panel_application.NotificationService;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.webkit.internal.ApiFeature;

import com.example.teacher_panel_application.VideoStreaming.ReceivedVideoStreamingActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMNotificationService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("MyApp","on message received class called ");
        if (remoteMessage.getData().size() > 0) {
            // Get the click_action
            String clickAction = remoteMessage.getData().get("click_action");
            Log.d("MyApp","open watch activity remote msg size greater than 0");
            // Handle the click_action
            if ("FLUTTER_NOTIFICATION_CLICK".equals(clickAction)) { // Or your custom click_action
                // Start WatchActivity
                Log.d("MyApp","open watch activity");
                Intent intent = new Intent(this, ReceivedVideoStreamingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else {
                Log.d("MyApp","open watch activity action didnt match");
            }
        }else {
            Log.d("MyApp","remote msg size less than 0");
        }
    }
}