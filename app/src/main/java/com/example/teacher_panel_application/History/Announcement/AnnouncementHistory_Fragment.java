package com.example.teacher_panel_application.History.Announcement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teacher_panel_application.databinding.HistoryNotificationBinding;
import com.google.firebase.auth.FirebaseAuth;

public class AnnouncementHistory_Fragment extends Fragment {
    private HistoryNotificationBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HistoryNotificationBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }
    private void getData(){


        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        LoadAnnouncementData loadDataInBackground = new LoadAnnouncementData(binding.announcementRecycler,binding.dataShowTxtNotification,binding.historyShimmer,uid,getActivity());
        loadDataInBackground.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

}
