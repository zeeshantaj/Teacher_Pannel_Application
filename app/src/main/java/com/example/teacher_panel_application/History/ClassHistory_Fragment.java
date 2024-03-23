package com.example.teacher_panel_application.History;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.teacher_panel_application.databinding.HistoryClassBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ClassHistory_Fragment extends Fragment {
    private HistoryClassBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding  =  HistoryClassBinding.inflate(inflater,container,false);
       // getData();
        return binding.getRoot();
    }


    public void getData(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        LoadClassData loadDataInBackground = new LoadClassData(binding.classHistoryRecycler,binding.dataShowTxt,binding.historyShimmer,uid,getActivity());
        loadDataInBackground.execute();

    }
    @Override
    public void onStart() {
        super.onStart();
        getData();
    }
}
