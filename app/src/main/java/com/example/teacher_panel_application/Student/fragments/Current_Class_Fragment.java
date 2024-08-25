package com.example.teacher_panel_application.Student.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.FragmentCurrentClassBinding;

public class Current_Class_Fragment extends Fragment {

    public Current_Class_Fragment() {
        // Required empty public constructor
    }


    FragmentCurrentClassBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCurrentClassBinding.inflate(inflater, container, false);



        return binding.getRoot();
    }
}