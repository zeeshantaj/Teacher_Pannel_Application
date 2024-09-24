package com.example.teacher_panel_application.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.teacher_panel_application.Adapters.QAAdapter;
import com.example.teacher_panel_application.Models.QAModel;
import com.example.teacher_panel_application.databinding.FragmentQABinding;

import java.util.ArrayList;
import java.util.List;

public class QA_Fragment extends Fragment {

    public QA_Fragment() {
    }
    private FragmentQABinding binding;
    private QAAdapter adapter;
    private List<QAModel> modelList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQABinding.inflate(inflater,container,false);

        modelList = new ArrayList<>();

        adapter = new QAAdapter(getContext(),modelList);
        binding.QARecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.QARecyclerView.setAdapter(adapter);

        return binding.getRoot();

    }
}
