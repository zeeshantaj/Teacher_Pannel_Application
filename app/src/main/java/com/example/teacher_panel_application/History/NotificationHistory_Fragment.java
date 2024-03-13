package com.example.teacher_panel_application.History;

import android.os.AsyncTask;
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

public class NotificationHistory_Fragment extends Fragment {
    private HistoryNotificationBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HistoryNotificationBinding.inflate(inflater,container,false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        LoadNotificationData loadDataInBackground = new LoadNotificationData(binding.announcementRecycler,binding.dataShowTxtNotification,uid,getActivity());
        loadDataInBackground.execute();
        return binding.getRoot();
    }
//    private class LoadAnnouncementsTask extends AsyncTask<Void, Void, List<AnnouncementModel>> {
//
//        @Override
//        protected List<AnnouncementModel> doInBackground(Void... voids) {
//            FirebaseAuth auth = FirebaseAuth.getInstance();
//            String uid = auth.getUid();
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Announcement").child(uid);
//
//            final List<AnnouncementModel> modelList = new ArrayList<>();
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()){
//                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
//
//                            AnnouncementModel model = new AnnouncementModel();
//                            if (dataSnapshot.child("title").exists()){
//                                String title = dataSnapshot.child("title").getValue(String.class);
//                                String date = dataSnapshot.child("current_date").getValue(String.class);
//                                String dueDate = dataSnapshot.child("due_date").getValue(String.class);
//                                String des = dataSnapshot.child("description").getValue(String.class);
//
//                                model.setDue_date(dueDate);
//                                model.setTitle(title);
//                                model.setDescription(des);
//                                model.setCurrent_date(date);
//                            }
//                            if (dataSnapshot.child("imageUrl").exists()){
//                                String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
//                                String date = dataSnapshot.child("current_date").getValue(String.class);
//                                model.setImageUrl(imageUrl);
//                                model.setCurrent_date(date);
//                            }
//                            modelList.add(model);
//                        }
//                        // Update UI with the loaded data
//                        AnnounceAdapter adapter = new AnnounceAdapter(modelList);
//                        binding.announcementRecycler.setAdapter(adapter);
//                        binding.announcementRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(getActivity(), "Error "+ error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//            return modelList;
//        }
//
//        @Override
//        protected void onPostExecute(List<AnnouncementModel> modelList) {
//
//        }
//    }

    private void initRecyclerData(){
        binding.announcementRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<AnnouncementModel> modelList = new ArrayList<>();
        AnnounceAdapter adapter = new AnnounceAdapter(modelList);
        binding.announcementRecycler.setAdapter(adapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        LoadNotificationData loadDataInBackground = new LoadNotificationData(binding.announcementRecycler,binding.dataShowTxtNotification,uid,getActivity());
        loadDataInBackground.execute();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Announcement").child(uid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){


                        AnnouncementModel model = new AnnouncementModel();
                        if (dataSnapshot.child("title").exists()){
                            String title = dataSnapshot.child("title").getValue(String.class);
                            String date = dataSnapshot.child("current_date").getValue(String.class);
                            String dueDate = dataSnapshot.child("due_date").getValue(String.class);
                            String des = dataSnapshot.child("description").getValue(String.class);

                            model.setDue_date(dueDate);
                            model.setTitle(title);
                            model.setDescription(des);
                            model.setCurrent_date(date);
                        }
                        if (dataSnapshot.child("imageUrl").exists()){
                            String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                            String date = dataSnapshot.child("current_date").getValue(String.class);
                            model.setImageUrl(imageUrl);
                            model.setCurrent_date(date);
                        }
                        modelList.add(model);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
     //   initRecyclerData();
    }

    @Override
    public void onStart() {
        super.onStart();
       // initRecyclerData();
    }
}
