package com.example.teacher_panel_application.Login;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.teacher_panel_application.Home.Home_Activity;
import com.example.teacher_panel_application.Home.Home_Fragment;
import com.example.teacher_panel_application.Intro.IntroFragment;
import com.example.teacher_panel_application.Network.NetworkCheckReceiver;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Student.StudentActivity;
import com.example.teacher_panel_application.Utils.FragmentUtils;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Activity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private View splashView;
    private NetworkCheckReceiver networkChangeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashView = getLayoutInflater().inflate(R.layout.splash_screen, null);
        setContentView(splashView);

        getSupportActionBar().hide();
        new Handler().postDelayed(() -> {
            setContentView(R.layout.activity_login);
            binding = ActivityLoginBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();

            if (user != null) {
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    String email = user.getEmail();
                    checkUserRole(email);
//                    startActivity(new Intent(Login_Activity.this, Home_Activity.class));
//                    finish(); // Finish the current activity to prevent going back to it on back press
                });
                thread.start();

            } else {
                //show intro when user first install app
                FragmentUtils.SetFragment(getSupportFragmentManager(), new IntroFragment(), binding.loginFrameLayout.getId());
            }
        }, 1000); // Delay for 1 seconds before loading the actual layout

        networkChangeReceiver = new NetworkCheckReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);

    }
    public void checkUserRole(String email) {
        Log.d("MyApp","check user role");
        DatabaseReference teacherRef = FirebaseDatabase.getInstance().getReference().child("TeacherInfo");
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("StudentsInfo");
        // Query the TeacherInfo reference
        teacherRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Intent intent = new Intent(Login_Activity.this, Home_Activity.class);
                    startActivity(intent);
                   finish();
                } else {
                    // Email does not exist in TeacherInfo, check StudentsInfo
                    studentRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Email exists in StudentsInfo
                                // Send user to StudentActivity
                                Intent intent = new Intent(Login_Activity.this, StudentActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Email does not exist in both databases
                                MethodsUtils.showFlawDialog(Login_Activity.this,R.drawable.icon_error,"Error","Email Not Found!",1);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle possible errors
                            //Toast.makeText(CurrentActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            MethodsUtils.showFlawDialog(Login_Activity.this,R.drawable.icon_error,"Error",databaseError.getMessage(),1);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
                MethodsUtils.showFlawDialog(Login_Activity.this,R.drawable.icon_error,"Error",databaseError.getMessage(),1);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }
}