package com.example.teacher_panel_application.VideoStreaming;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.teacher_panel_application.R;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingConfig;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingFragment;

public class ZegoCloudVideoStreamingActivity extends AppCompatActivity {

    String appSign = "95ae7921ce3ed8f0957bf4ffe383a3933491b47574d375e871392c3e10f93ba2";
    long appID = 707255301;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zego_cloud_video_streaming);
        addFragment();

    }
    public void addFragment() {
        String userID = "12345678";
        String userName = "zeeshan";

//        String liveID = getIntent().getStringExtra("liveID");

        ZegoUIKitPrebuiltLiveStreamingConfig config = ZegoUIKitPrebuiltLiveStreamingConfig.host();;
        ZegoUIKitPrebuiltLiveStreamingFragment fragment = ZegoUIKitPrebuiltLiveStreamingFragment.newInstance(
                appID, appSign, userID, userName,"12345",config);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
    }
}