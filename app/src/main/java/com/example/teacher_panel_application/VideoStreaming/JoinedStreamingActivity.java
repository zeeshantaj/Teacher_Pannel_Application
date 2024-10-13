package com.example.teacher_panel_application.VideoStreaming;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingConfig;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class JoinedStreamingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_joined_streaming);
      addFragment();
    }
    public void addFragment() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getUid();
        String userName = MethodsUtils.getString(this,"studentName");

        Intent intent = getIntent();
        String liveId = intent.getStringExtra("liveId");
        String uid = intent.getStringExtra("userId");

        ZegoUIKitPrebuiltLiveStreamingConfig config = ZegoUIKitPrebuiltLiveStreamingConfig.audience();
        ZegoUIKitPrebuiltLiveStreamingFragment fragment = ZegoUIKitPrebuiltLiveStreamingFragment.newInstance(
                KeyConstants.appID, KeyConstants.appSign, userID, userName, liveId,config);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();

        uploadData(uid,userID);
    }
    private void uploadData(String userID,String uid){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("videoStreaming")
                .child(userID)
                .child(getDay())
                .child("joinedUsers");
        HashMap<String ,Object > values = new HashMap<>();
        values.put(uid, uid);
        reference.updateChildren(values).addOnSuccessListener(unused -> {});
    }


    private String getDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
        Date now = new Date();
        String formattedDate = sdf.format(now);
        return formattedDate;
    }

}