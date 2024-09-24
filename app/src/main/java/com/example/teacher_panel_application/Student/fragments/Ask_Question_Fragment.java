package com.example.teacher_panel_application.Student.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teacher_panel_application.databinding.FragmentAskQuestionBinding;

public class Ask_Question_Fragment extends Fragment {


    public Ask_Question_Fragment() {
    }
    private FragmentAskQuestionBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAskQuestionBinding.inflate(inflater,container,false);



        return binding.getRoot();
    }
}
