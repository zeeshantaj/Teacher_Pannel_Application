package com.example.teacher_panel_application.VideoStreaming;

import android.os.Bundle;
import android.os.Handler;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class ZegoCloudVideoStreamingActivity extends AppCompatActivity implements OnTitleListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zego_cloud_video_streaming);
//        addFragment();
        AddTitleDialog dialog = new AddTitleDialog();
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(),"title dialog");


    }
    public void addFragment(String title) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getUid();
        String userName = MethodsUtils.getString(this,"teacherName");
        String img = MethodsUtils.getString(this,"teacherImg");
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);
        String liveId = String.valueOf(randomNumber);

        uploadToFirebase(userName,img,liveId,title);
        ZegoUIKitPrebuiltLiveStreamingConfig config = ZegoUIKitPrebuiltLiveStreamingConfig.host();
        ZegoUIKitPrebuiltLiveStreamingFragment fragment = ZegoUIKitPrebuiltLiveStreamingFragment.newInstance(
                KeyConstants.appID, KeyConstants.appSign, userID, userName,liveId,config);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();

    }
    public void uploadToFirebase(String name,String img,String liveID,String title){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("videoStreaming")
                .child(uid)
                .child(getDay());

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:a dd:MM:yyyy");
        Date now = new Date();
        String formattedDate = sdf.format(now);

        StreamModel model = new StreamModel(name,liveID,title,img,formattedDate,uid,true);
        reference.setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                .child(uid)
                .child(getDay());
        HashMap<String ,Object > values = new HashMap<>();
        values.put("live", false);
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

    @Override
    public void onSuccess(String title) {
        addFragment(title);
    }

    @Override
    public void onFailed() {
        Toast.makeText(this, "You Have to add title to start live video", Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },1000);
    }

    private String getDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
        Date now = new Date();
        String formattedDate = sdf.format(now);
        return formattedDate;
    }
}
