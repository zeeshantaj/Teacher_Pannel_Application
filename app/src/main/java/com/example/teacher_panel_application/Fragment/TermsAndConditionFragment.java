package com.example.teacher_panel_application.Fragment;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teacher_panel_application.databinding.TermConditionLayoutBinding;

public class TermsAndConditionFragment extends Fragment {

    private TermConditionLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TermConditionLayoutBinding.inflate(inflater,container,false);

        binding.termTxt.setMovementMethod(new ScrollingMovementMethod());

        return binding.getRoot();
    }
}
