package com.example.teacher_panel_application.VideoStreaming;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;

import com.example.teacher_panel_application.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;

public class ReceivedVideoStreamingActivity extends AppCompatActivity {
    private String appId = KeyConstants.APPID;
    private String channelName;
    private String token = KeyConstants.TOKEN;
    private RtcEngine mRtcEngine;
    private String studentId;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference viewersRef;

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            super.onJoinChannelSuccess(channel, uid, elapsed);
            runOnUiThread(() -> {
                Toast.makeText(ReceivedVideoStreamingActivity.this, "Joined stream", Toast.LENGTH_SHORT).show();
                trackViewerInFirebase(channel, uid); // Track the student in Firebase
            });
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            runOnUiThread(() -> {
                Toast.makeText(ReceivedVideoStreamingActivity.this, "Teacher joined", Toast.LENGTH_SHORT).show();
                setupRemoteVideo(uid); // Show teacher's stream
            });
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            runOnUiThread(() -> {
                Toast.makeText(ReceivedVideoStreamingActivity.this, "Teacher left", Toast.LENGTH_SHORT).show();
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_video_streaming);

        // Get the channel name passed from the notification
        channelName = getIntent().getStringExtra("channelName");
        studentId = UUID.randomUUID().toString(); // Generate a unique ID for the student

        // Initialize Firebase Database reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        viewersRef = firebaseDatabase.getReference("streams").child(KeyConstants.CHANNELNAME).child("viewers");

        // Initialize Agora SDK and join the channel
        initializeAndJoinChannel();
//        if (checkPermissions()) {
//
//        } else {
//            ActivityCompat.requestPermissions(this, getRequiredPermissions(), PERMISSION_REQ_ID);
//        }
    }

    private void initializeAndJoinChannel() {
        try {
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = getBaseContext();
            config.mAppId = appId;
            config.mEventHandler = mRtcEventHandler;
            mRtcEngine = RtcEngine.create(config);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing Agora SDK.");
        }

        mRtcEngine.enableVideo();

        FrameLayout container = findViewById(R.id.remote_video_view_container);
        SurfaceView surfaceView = new SurfaceView(getBaseContext());
        container.addView(surfaceView);

        ChannelMediaOptions options = new ChannelMediaOptions();
        options.clientRoleType = Constants.CLIENT_ROLE_AUDIENCE;
        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
        options.audienceLatencyLevel = Constants.AUDIENCE_LATENCY_LEVEL_ULTRA_LOW_LATENCY;

        // Join the channel as audience
        mRtcEngine.joinChannel(token, channelName, 0, options);
    }

    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        SurfaceView surfaceView = new SurfaceView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);

        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
    }

    // Track the viewer in Firebase
    private void trackViewerInFirebase(String channel, int uid) {
        viewersRef.child(studentId).setValue(true);
    }

    private void removeViewerFromFirebase() {
        viewersRef.child(studentId).removeValue();
    }

    private String[] getRequiredPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            return new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.BLUETOOTH_CONNECT
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (checkPermissions()) {
//
//        }
        initializeAndJoinChannel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRtcEngine.leaveChannel();
        removeViewerFromFirebase(); // Remove student from the viewer list when leaving
    }
}