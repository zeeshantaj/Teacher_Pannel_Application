package com.example.teacher_panel_application.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.teacher_panel_application.Network.NetworkCheckReceiver;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Home_Activity extends AppCompatActivity {
    public ViewPager2 viewPager2;
    private NetworkCheckReceiver networkChangeReceiver;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager2 = findViewById(R.id.homeViewPager);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.darkBlue));


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.setSelectedItemId(getNavigationItem(position));

                viewPager2.setCurrentItem(position,true);
            }
        });
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            int pos = getPositionForNavigationItem(itemId);
            if (pos != -1) {
                viewPager2.setCurrentItem(pos, true);
                return true;
            }
            return false;
        });
        Main_HomeViewPagerAdapter viewPagerAdapter = new Main_HomeViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);


        networkChangeReceiver = new NetworkCheckReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
        getStatus();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE }, 2);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(networkChangeReceiver);
    }

    private void getStatus(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()){
                    String token = task.getResult();
                    //FirebaseUtils.currentUserDetails().update("FCMToken",token);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TeacherInfo").child(MethodsUtils.getCurrentUID());
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("FCMToken", token);
                    updates.put("uid",MethodsUtils.getCurrentUID());
                    reference.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });

                }
                else {
                    Log.e("MyApp","Notification Error "+ task.getException());
                }
            }
        });
    }
    private int getNavigationItem(int position) {
        switch (position) {
            case 0:
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.classDetails);
                return R.id.navHome;
            case 1:
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.postDetails);
                return R.id.navUploadDetails;
            case 2:
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.uploadedHistory);
                return R.id.navHistory;
            case 3:
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.profile);
                return R.id.navProfile;

            // Add more cases as needed
        }
        return 0;
    }

    private int getPositionForNavigationItem(int itemId) {
        if (itemId == R.id.navHome) {
            return 0;
        } else if (itemId == R.id.navUploadDetails) {
            return 1;
        } else if (itemId == R.id.navHistory) {
            return 2;
        } else if (itemId == R.id.navProfile) {
            return 3;
        }

            return -1;
        }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                // Permission denied
            }
        }
    }
}