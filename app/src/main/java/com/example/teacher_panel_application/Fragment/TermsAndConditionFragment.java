package com.example.teacher_panel_application.Fragment;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teacher_panel_application.databinding.TermConditionLayoutBinding;

import eightbitlab.com.blurview.RenderScriptBlur;

public class TermsAndConditionFragment extends Fragment {

    private TermConditionLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TermConditionLayoutBinding.inflate(inflater,container,false);

        binding.termTxt.setMovementMethod(new ScrollingMovementMethod());
        binding.termDismissBtn.setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity != null) {
                activity.onBackPressed();
            }
        });

        setBlurView();
        return binding.getRoot();
    }
    private void setBlurView() {
        float radius = 15f;

        View decorView = getActivity().getWindow().getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);

        Drawable windowBackground = decorView.getBackground();

        binding.termsBlurView.setupWith(rootView, new RenderScriptBlur(getActivity())) // or RenderEffectBlur
                .setFrameClearDrawable(windowBackground) // Optional
                .setBlurRadius(radius);
    }
}
