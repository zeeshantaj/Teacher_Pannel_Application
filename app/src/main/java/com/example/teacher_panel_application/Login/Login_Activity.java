package com.example.teacher_panel_application.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.teacher_panel_application.Home.Home_Activity;
import com.example.teacher_panel_application.Intro.IntroFragment;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.FragmentUtils;
import com.example.teacher_panel_application.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private View splashView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashView = getLayoutInflater().inflate(R.layout.splash_screen, null);
        setContentView(splashView);

        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_login);
                binding = ActivityLoginBinding.inflate(getLayoutInflater());
                setContentView(binding.getRoot());

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            startActivity(new Intent(Login_Activity.this, Home_Activity.class));
                            finish(); // Finish the current activity to prevent going back to it on back press
                        }
                    });
                    thread.start();

                } else {
                    //show intro when user first install app
                    FragmentUtils.SetFragment(getSupportFragmentManager(), new IntroFragment(), binding.loginFrameLayout.getId());
                }
            }
        }, 2000); // Delay for 2 seconds before loading the actual layout
    }
}