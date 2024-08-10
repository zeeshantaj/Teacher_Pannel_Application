package com.example.teacher_panel_application.History.StudyMaterial;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.teacher_panel_application.History.ClassHis.ClassHistoryAdapter;
import com.example.teacher_panel_application.History.ClassHis.LoadClassData;
import com.example.teacher_panel_application.Models.PDFModel;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.Network.NetworkUtils;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.TeacherHistoryDB.TeacherDB;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.FragmentUploadClassDataBinding;
import com.example.teacher_panel_application.databinding.FragmentViewStudyMaterialBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class View_Study_Material extends Fragment {
    public View_Study_Material() {
        // Required empty public constructor
    }
    private FragmentViewStudyMaterialBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewStudyMaterialBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData(getActivity());

    }

    private void getData(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                // Device's internet is turned on
                NetworkUtils.hasInternetAccess(hasInternetAccess -> {
                    if (hasInternetAccess){
                        // show online data
                        getOnlineData();
                    }else {
                        getOfflineData();
                        // show offline data
                    }
                });

            }

        }
    }
    private void getOfflineData(){
        binding.offlineTxt.setVisibility(View.VISIBLE);
//        TeacherDB databaseHelper = new TeacherDB(getActivity());
//        List<PDFModel> modelList = databaseHelper.getAllClassData();
//        ViewStudyHistoryAdapter adapter = new ViewStudyHistoryAdapter(getActivity(), modelList);
//        binding.studyMaterialRV.setLayoutManager(new LinearLayoutManager(getActivity()));
//        binding.studyMaterialRV.setItemAnimator(null);
//        binding.studyMaterialRV.setAdapter(adapter);
    }
    public void getOnlineData() {
        binding.offlineTxt.setVisibility(View.GONE);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        LoadPdfDataClass loadDataInBackground = new LoadPdfDataClass(binding.dataShowTxt,binding.studyMaterialRV, binding.historyShimmer, uid, getActivity());
        loadDataInBackground.execute();
    }


}