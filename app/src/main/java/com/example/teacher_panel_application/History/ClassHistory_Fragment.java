package com.example.teacher_panel_application.History;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.TeacherHistoryDB.TeacherDB;
import com.example.teacher_panel_application.databinding.HistoryClassBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;

public class ClassHistory_Fragment extends Fragment {
    private HistoryClassBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HistoryClassBinding.inflate(inflater, container, false);
        // getData();
        return binding.getRoot();
    }


    public void getData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        LoadClassData loadDataInBackground = new LoadClassData(binding.dataShowTxt, binding.historyShimmer, uid, getActivity());
        loadDataInBackground.execute();
        TeacherDB databaseHelper = new TeacherDB(getActivity());
        List<UploadClassModel> modelList = databaseHelper.getAllClassData();
        Log.d("MyApp","model count"+modelList.size());
        Collections.reverse(modelList);
        ClassHistoryAdapter adapter = new ClassHistoryAdapter(modelList, getActivity());
        binding.classHistoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.classHistoryRecycler.setItemAnimator(null);
        binding.classHistoryRecycler.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }
}
