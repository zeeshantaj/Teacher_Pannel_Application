package com.example.teacher_panel_application.VideoStreaming;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingConfig;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingFragment;

import java.util.HashMap;
import java.util.Random;

public class ZegoCloudVideoStreamingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zego_cloud_video_streaming);
        addFragment();

    }
    public void addFragment() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getUid();
        String userName = MethodsUtils.getString(this,"teacherName");

        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);
        String liveId = String.valueOf(randomNumber);

        uploadToFirebase(userName,liveId);
        ZegoUIKitPrebuiltLiveStreamingConfig config = ZegoUIKitPrebuiltLiveStreamingConfig.host();
        ZegoUIKitPrebuiltLiveStreamingFragment fragment = ZegoUIKitPrebuiltLiveStreamingFragment.newInstance(
                KeyConstants.appID, KeyConstants.appSign, userID, userName,liveId,config);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();

    }
    public void uploadToFirebase(String name,String liveID){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("videoStreaming")
                .child(uid);
        HashMap<String ,Object > values = new HashMap<>();
        values.put("streamerName", name);
        values.put("liveId", liveID);
        values.put("isLive", true);
        reference.setValue(values).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ZegoCloudVideoStreamingActivity.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ZegoCloudVideoStreamingActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateIsLive(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("videoStreaming")
                .child(uid);
        HashMap<String ,Object > values = new HashMap<>();
        values.put("isLive", false);
        reference.updateChildren(values).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        updateIsLive();
    }
}