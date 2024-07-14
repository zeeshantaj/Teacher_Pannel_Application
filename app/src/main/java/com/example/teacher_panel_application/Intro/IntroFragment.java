package com.example.teacher_panel_application.Intro;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.teacher_panel_application.Login.Login_Fragment;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.IntroScreenBinding;

public class IntroFragment extends Fragment {

    private IntroScreenBinding binding;
    private FrameLayout parentFrameLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = IntroScreenBinding.inflate(inflater,container,false);

        parentFrameLayout = getActivity().findViewById(R.id.loginFrameLayout);

        binding.classConnectTxt.setText("Class\n Connect".toUpperCase());

        TextPaint paint = binding.classConnectTxt.getPaint();
        float width = paint.measureText("Class\n Connect");

        Shader textShader = new LinearGradient(0, 0, width, binding.classConnectTxt.getTextSize(),
                new int[]{
                        Color.parseColor("#6246c0"),
                        Color.parseColor("#9981f3"),
//                        Color.parseColor("#64B678"),
//                        Color.parseColor("#478AEA"),
//                        Color.parseColor("#8446CC"),
                }, null, Shader.TileMode.CLAMP);
        binding.classConnectTxt.getPaint().setShader(textShader);


        binding.getStartedBtnAsTeacher.setOnClickListener(v -> {
            setFragment(new Login_Fragment());
            setSharedBool(false);
        });
//        binding.getStartedBtnAsStudent.setOnClickListener(v -> {
//            setFragment(new Login_Fragment());
//            setSharedBool(true);
//        });

        return binding.getRoot();
    }

    private void setSharedBool(Boolean bool){
        SharedPreferences sharedPreference = getActivity().getSharedPreferences("loginType", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putBoolean("typeBool",bool);
        editor.apply();
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}
