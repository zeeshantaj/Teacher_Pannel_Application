package com.example.teacher_panel_application.History;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.HistoryClassBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassHistory_Fragment extends Fragment {
    private HistoryClassBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding  =  HistoryClassBinding.inflate(inflater,container,false);
        getData();
        return binding.getRoot();
    }
    private void getData(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        LoadClassData loadDataInBackground = new LoadClassData(binding.classHistoryRecycler,binding.dataShowTxt,uid,getActivity());
        loadDataInBackground.execute();

    }
    @Override
    public void onStart() {
        super.onStart();
        getData();
    }
    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
