package com.example.teacher_panel_application.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.teacher_panel_application.Intro.IntroFragment;
import com.example.teacher_panel_application.Utils.FragmentUtils;
import com.example.teacher_panel_application.databinding.ActivityLoginBinding;

public class Login_Activity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        FragmentUtils.SetFragment(getSupportFragmentManager(),new IntroFragment(),binding.loginFrameLayout.getId());
    }
}