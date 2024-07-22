package com.example.teacher_panel_application.Student;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.teacher_panel_application.Home.Home_Activity;
import com.example.teacher_panel_application.Home.Main_HomeViewPagerAdapter;
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

public class StudentActivity extends AppCompatActivity {

    public ViewPager2 viewPager2;
    private NetworkCheckReceiver networkChangeReceiver;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        viewPager2 = findViewById(R.id.studentViewPager);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewStudent);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.darkBlue));
        getStatus();

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
        StudentViewPagerAdapter viewPagerAdapter = new StudentViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);


        networkChangeReceiver = new NetworkCheckReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }
    private int getNavigationItem(int position) {
        switch (position) {
            case 0:
                Objects.requireNonNull(getSupportActionBar()).setTitle("Announcement");
                return R.id.studentAnnouncement;
            case 1:
                Objects.requireNonNull(getSupportActionBar()).setTitle("Study Material");
                return R.id.navHistory;
            case 2:
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.profile);
                return R.id.navProfileStudent;
            // Add more cases as needed
        }
        return 0;
    }

    private int getPositionForNavigationItem(int itemId) {
        if (itemId == R.id.studentAnnouncement) {
            return 0;
        } else if (itemId == R.id.navHistory) {
            return 1;
        } else if (itemId == R.id.navProfileStudent) {
            return 2;
        }

        return -1;
    }
    private void getStatus(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()){
                    String token = task.getResult();
                    //FirebaseUtils.currentUserDetails().update("FCMToken",token);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("StudentsInfo").child(MethodsUtils.getCurrentUID());
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("uid",MethodsUtils.getCurrentUID());
                    updates.put("FCMToken", token);
                    reference.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(StudentActivity.this, "fcm added", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(StudentActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {
                    Log.e("MyApp","Notification Error "+ task.getException());
                }
            }
        });
    }
}