package com.example.teacher_panel_application.VideoStreaming;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teacher_panel_application.Access.AccessToken;
import com.example.teacher_panel_application.Access.SendNotification;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VideoStreamingActivity extends AppCompatActivity {

    // Fill in the app ID from Agora Console
    private String appId = KeyConstants.APPID;
    // Fill in the channel name
    private String channelName = KeyConstants.CHANNELNAME;
    // Fill in the temporary token generated from Agora Console
    private String token = KeyConstants.TOKEN;
    private RtcEngine mRtcEngine;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        // Callback when successfully joining the channel
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            super.onJoinChannelSuccess(channel, uid, elapsed);
            runOnUiThread(() -> {
                Toast.makeText(VideoStreamingActivity.this, "Join channel success", Toast.LENGTH_SHORT).show();
            });
        }
        // Callback when a remote user or host joins the current channel
        @Override
        public void onUserJoined(int uid, int elapsed) {
            runOnUiThread(() -> {
                // When a remote user joins the channel, display the remote video stream for the specified uid
                setupRemoteVideo(uid);
            });
        }
        // Callback when a remote user or host leaves the current channel
        @Override
        public void onUserOffline(int uid, int reason) {
            super.onUserOffline(uid, reason);
            runOnUiThread(() -> {
                Toast.makeText(VideoStreamingActivity.this, "User offline: " + uid, Toast.LENGTH_SHORT).show();
            });
        }
    };
    private void initializeAndJoinChannel() {
        try {
            // Create an RtcEngineConfig instance and configure it
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = getBaseContext();
            config.mAppId = appId;
            config.mEventHandler = mRtcEventHandler;
            // Create and initialize an RtcEngine instance
            mRtcEngine = RtcEngine.create(config);
        } catch (Exception e) {
            throw new RuntimeException("Check the error.");
        }
        // Enable the video module
        mRtcEngine.enableVideo();

        // Enable local preview
        mRtcEngine.startPreview();
        // Create a SurfaceView object and make it a child object of FrameLayout
        FrameLayout container = findViewById(R.id.local_video_view_container);
        SurfaceView surfaceView = new SurfaceView (getBaseContext());
        container.addView(surfaceView);
        // Pass the SurfaceView object to the SDK and set the local view
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0));
        // Create an instance of ChannelMediaOptions and configure it
        ChannelMediaOptions options = new ChannelMediaOptions();
        // Set the user role to BROADCASTER or AUDIENCE according to the scenario
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
        // In the live broadcast scenario, set the channel profile to BROADCASTING (live broadcast scenario)
        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
        // Set the audience latency level
        options.audienceLatencyLevel = Constants.AUDIENCE_LATENCY_LEVEL_ULTRA_LOW_LATENCY;
        // Use the temporary token to join the channel
        // Specify the user ID yourself and ensure it is unique within the channel
        mRtcEngine.joinChannel(token, channelName, 0, options);

        sendNotificationToUser();
    }

    private void sendNotificationToUser() {

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("StudentsInfo");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                    String year = userSnapshot.child("studentYear").getValue(String.class);
                    String semester = userSnapshot.child("studentSemester").getValue(String.class);
                    String token = userSnapshot.child("FCMToken").getValue(String.class);


                    sendNotification("teacher started a video streaming go watch and learn something new","Video Streaming...",token);
                        //sendNotification(token, "New PDF Available", "Check out the new PDF added.");
//                    SendNotification sendNotification = new SendNotification(token,
//                            "teacher started a video streaming go watch and learn something new " + "streaming by " +
//                                    MethodsUtils.getString(VideoStreamingActivity.this, "teacherName"),
//                            "Video Streaming...", VideoStreamingActivity.this);
//                    sendNotification.sendNotification();
                    Toast.makeText(VideoStreamingActivity.this, "Notification send", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }public void sendNotification(String title, String body, String userFCMToken) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject mainObj = new JSONObject();
        try {
            JSONObject messageObject = new JSONObject();
            JSONObject notificationObject = new JSONObject();

            // Set notification title and body
            notificationObject.put("title", title);
            notificationObject.put("body", body);
            messageObject.put("notification", notificationObject);
            messageObject.put("token", userFCMToken);

            // Create a data object to hold the click_action and pending intent
            JSONObject dataObject = new JSONObject();
            dataObject.put("click_action", "FLUTTER_NOTIFICATION_CLICK"); // Your custom click action
            dataObject.put("notificationType", "video_streaming"); // Additional data

            // Add the data object to the message object
            messageObject.put("data", dataObject);

            mainObj.put("message", messageObject);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    "https://fcm.googleapis.com/v1/projects/teacherpanelapp/messages:send", mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d("MyApp", "Response: success -> notification sent");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (volleyError.networkResponse != null) {
                        int statusCode = volleyError.networkResponse.statusCode;
                        Log.d("MyApp", "Error: " + statusCode + " " + new String(volleyError.networkResponse.data));
                    } else {
                        Log.d("MyApp", "Volley error " + volleyError.getMessage());
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    AccessToken accessToken = new AccessToken();
                    String accessKey = accessToken.getAccessToken();
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "Bearer " + accessKey);
                    return header;
                }
            };
            requestQueue.add(jsonObjectRequest);

        } catch (JSONException e) {
            Log.d("MyApp", "json exception " + e.getMessage());
        }
    }
    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        SurfaceView surfaceView = new SurfaceView (getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        // Pass the SurfaceView object to the SDK and set the remote view
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
    }
    private static final int PERMISSION_REQ_ID = 22;
    // Obtain recording, camera and other permissions required to implement real-time audio and video interaction
    private String[] getRequiredPermissions(){
        // Determine the permissions required when targetSDKVersion is 31 or above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            return new String[]{
                    Manifest.permission.RECORD_AUDIO, // Recording permission
                    Manifest.permission.CAMERA, // Camera permission
                    Manifest.permission.READ_PHONE_STATE, // Permission to read phone status
                    Manifest.permission.BLUETOOTH_CONNECT // Bluetooth connection permission
            };
        } else {
            return new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA
            };
        }
    }
    private boolean checkPermissions() {
        for (String permission : getRequiredPermissions()) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    // System permission request callback
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermissions()) {
            initializeAndJoinChannel();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_streaming);
        // If authorized, initialize RtcEngine and join the channel
        if (checkPermissions()) {
            initializeAndJoinChannel();
        } else {
            ActivityCompat.requestPermissions(this, getRequiredPermissions(), PERMISSION_REQ_ID);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop local video preview
        mRtcEngine.stopPreview();
        // Leave the channel
        mRtcEngine.leaveChannel();
    }
}