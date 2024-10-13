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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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


        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("videoStreaming");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        StreamModel model = dataSnapshot.getValue(StreamModel.class);
                        if (model.live){
                            modelList.add(model);
                        }
                    }
                    ReceiveVideoStreamingAdapter adapter = new ReceiveVideoStreamingAdapter(ReceivedVideoStreamingActivity.this,modelList);
                    recyclerView.setLayoutManager(new GridLayoutManager(ReceivedVideoStreamingActivity.this,2));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReceivedVideoStreamingActivity.this, "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        if (checkPermissions()) {

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
        }
    }

}