package com.example.teacher_panel_application.Student.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.teacher_panel_application.History.Announcement.AnnounceAdapter;
import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Student.Adapter.PollAdapter;
import com.example.teacher_panel_application.Student.PollModel;
import com.example.teacher_panel_application.databinding.FragmentAnnouncementNotificationBinding;
import com.example.teacher_panel_application.databinding.FragmentStudentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Announcement_Notification_Fragment extends Fragment {


    public Announcement_Notification_Fragment() {
        // Required empty public constructor
    }

    FragmentAnnouncementNotificationBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAnnouncementNotificationBinding.inflate(inflater,container,false);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Announcement");
        List<AnnouncementModel> modelList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList.clear();
                if (snapshot.exists()) {
                    String dueDate = "";
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot keySnapshot : dataSnapshot.getChildren()) {
                            AnnouncementModel model = new AnnouncementModel();
                            if (keySnapshot.child("title").exists()) {
                                String title = keySnapshot.child("title").getValue(String.class);
                                String date = keySnapshot.child("current_date").getValue(String.class);
                                dueDate = keySnapshot.child("due_date").getValue(String.class);
                                String key = keySnapshot.child("key").getValue(String.class);
                                String des = keySnapshot.child("description").getValue(String.class);
                                String id = keySnapshot.child("id").getValue(String.class);

                                model.setDue_date(dueDate);
                                model.setTitle(title);
                                model.setKey(key);
                                model.setDescription(des);
                                model.setCurrent_date(date);
                                model.setId(id);

                            }
                            if (keySnapshot.child("imageUrl").exists()) {
                                String imageUrl = keySnapshot.child("imageUrl").getValue(String.class);
                                String date = keySnapshot.child("current_date").getValue(String.class);
                                dueDate = keySnapshot.child("due_date").getValue(String.class);
                                String key = keySnapshot.child("key").getValue(String.class);
                                String id = keySnapshot.child("id").getValue(String.class);
                                model.setImageUrl(imageUrl);
                                model.setCurrent_date(date);
                                model.setKey(key);
                                model.setDue_date(dueDate);
                                model.setId(id);
                            }
//                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
//                            LocalDateTime currentDateTime = LocalDateTime.now();
//                            String currentTime = currentDateTime.format(formatter);
//                            LocalDateTime startTime = LocalDateTime.parse(currentTime, formatter);
//                            LocalDateTime endTime = LocalDateTime.parse(dueDate, formatter);
//                            long timeDifferenceMillis = Duration.between(startTime, endTime).toMillis();
//                            Log.e("MyApp","announcement due date"+endTime);
//                            Log.e("MyApp","announcement start time "+startTime);
//                            Log.e("MyApp","announcement into millis  "+timeDifferenceMillis);



                            modelList.add(model);
                        }
                        //textView.setVisibility(View.GONE);
                    }
                    Collections.reverse(modelList);
                    AnnounceAdapter adapter = new AnnounceAdapter(modelList,getActivity());
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                    binding.studentHomeRecycler.setLayoutManager(staggeredGridLayoutManager);
                    binding.studentHomeRecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
//                    shimmerFrameLayout.stopShimmerAnimation();
//                    shimmerFrameLayout.setVisibility(View.GONE);

                }
                else {
//                    shimmerFrameLayout.stopShimmerAnimation();
//                    shimmerFrameLayout.setVisibility(View.GONE);
//                    textView.setVisibility(View.VISIBLE);
                }
                // dismiss shimmer here
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                shimmerFrameLayout.stopShimmerAnimation();
//                shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }
}