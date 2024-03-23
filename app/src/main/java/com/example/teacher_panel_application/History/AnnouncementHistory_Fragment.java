package com.example.teacher_panel_application.History;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.example.teacher_panel_application.databinding.HistoryNotificationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementHistory_Fragment extends Fragment {
    private HistoryNotificationBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HistoryNotificationBinding.inflate(inflater,container,false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        LoadAnnouncementData loadDataInBackground = new LoadAnnouncementData(binding.announcementRecycler,binding.dataShowTxtNotification,binding.historyShimmer,uid,getActivity());
        loadDataInBackground.execute();
        return binding.getRoot();
    }
}
