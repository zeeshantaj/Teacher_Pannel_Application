package com.example.teacher_panel_application.Student;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.teacher_panel_application.Network.NetworkCheckReceiver;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Student.fragments.Ask_Question_Activity;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
        FloatingActionButton button = findViewById(R.id.qaFloatingBtn);
        button.setOnClickListener(v -> {
            startActivity(new Intent(this,Ask_Question_Activity.class));
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            MethodsUtils.setFragment(fragmentManager,new Ask_Question_Fragment());
        });
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Permission granted you will receive notification from this app", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission granted you will not receive notification from this app", Toast.LENGTH_SHORT).show();

                // Permission denied
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("StudentsInfo").child(uid);
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String imageUrl = snapshot.child("image").getValue(String.class);
                    String fcm = snapshot.child("FCMToken").getValue(String.class);
                    String year = snapshot.child("studentYear").getValue(String.class);
                    String semester = snapshot.child("studentSemester").getValue(String.class);
                    String major = snapshot.child("studentMajor").getValue(String.class);
                    MethodsUtils.putString(StudentActivity.this,"studentName",name);
                    MethodsUtils.putString(StudentActivity.this,"studentYear",year);
                    MethodsUtils.putString(StudentActivity.this,"studentSemester",semester);
                    MethodsUtils.putString(StudentActivity.this,"studentMajor",major);
                    MethodsUtils.putString(StudentActivity.this,"studentImage",imageUrl);
                    MethodsUtils.putString(StudentActivity.this,"StudentFCMToken",fcm);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentActivity.this, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}