package com.example.teacher_panel_application.VideoStreaming;





import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingConfig;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingFragment;

import java.util.ArrayList;
import java.util.List;

public class ReceivedVideoStreamingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_video_streaming); // Create this layout for video watching
        RecyclerView recyclerView = findViewById(R.id.videoRecycler);
        List<StreamModel> modelList = new ArrayList<>();

        ReceiveVideoStreamingAdapter adapter = new ReceiveVideoStreamingAdapter(this,modelList);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);
        if (checkPermissions()) {
            addFragment();

        } else {
            ActivityCompat.requestPermissions(this, getRequiredPermissions(), 12);
        }
    }

    private String[] getRequiredPermissions() {
        return new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        };
    }

    private boolean checkPermissions() {
        for (String permission : getRequiredPermissions()) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermissions()) {
            addFragment();
        }
    }
    public void addFragment() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getUid();
        String userName = MethodsUtils.getString(this,"studentName");
        ZegoUIKitPrebuiltLiveStreamingConfig config = ZegoUIKitPrebuiltLiveStreamingConfig.audience();
        ZegoUIKitPrebuiltLiveStreamingFragment fragment = ZegoUIKitPrebuiltLiveStreamingFragment.newInstance(
                KeyConstants.appID, KeyConstants.appSign, userID, userName,"123456",config);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();

    }
}