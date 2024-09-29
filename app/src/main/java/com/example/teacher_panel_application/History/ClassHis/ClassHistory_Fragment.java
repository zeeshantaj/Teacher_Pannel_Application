package com.example.teacher_panel_application.History.ClassHis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.Network.NetworkUtils;
import com.example.teacher_panel_application.TeacherHistoryDB.TeacherDB;
import com.example.teacher_panel_application.databinding.HistoryClassBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ClassHistory_Fragment extends Fragment {
    private HistoryClassBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HistoryClassBinding.inflate(inflater, container, false);
        getData(getActivity());
        return binding.getRoot();
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
                        //getOfflineData();
                        // show offline data
                    }
                });

            }

        }
    }

    private void getOfflineData(){
        TeacherDB databaseHelper = new TeacherDB(getActivity());
        List<UploadClassModel> modelList = databaseHelper.getAllClassData();
        ClassHistoryAdapter adapter = new ClassHistoryAdapter(modelList, getActivity());
        binding.classHistoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.classHistoryRecycler.setItemAnimator(null);
        binding.classHistoryRecycler.setAdapter(adapter);
    }
    public void getOnlineData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        LoadClassData loadDataInBackground = new LoadClassData(binding.dataShowTxt,binding.classHistoryRecycler, binding.historyShimmer, uid, getActivity());
        loadDataInBackground.execute();
    }
}
