package com.example.teacher_panel_application.Student.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.teacher_panel_application.History.Announcement.AnnounceAdapter;
import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.FragmentStudentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentHome extends Fragment {
    public StudentHome() {
        // Required empty public constructor
    }
    private FragmentStudentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentHomeBinding.inflate(inflater,container,false);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Announcement");
        List<AnnouncementModel> modelList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        AnnouncementModel model = new AnnouncementModel();
                        if (dataSnapshot.child("title").exists()) {
                            String title = dataSnapshot.child("title").getValue(String.class);
                            String date = dataSnapshot.child("current_date").getValue(String.class);
                            String dueDate = dataSnapshot.child("due_date").getValue(String.class);
                            String key = dataSnapshot.child("key").getValue(String.class);
                            String des = dataSnapshot.child("description").getValue(String.class);
                            String id = dataSnapshot.child("id").getValue(String.class);

                            model.setDue_date(dueDate);
                            model.setTitle(title);
                            model.setKey(key);
                            model.setDescription(des);
                            model.setCurrent_date(date);
                            model.setId(id);

                        }
                        if (dataSnapshot.child("imageUrl").exists()) {
                            String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                            String date = dataSnapshot.child("current_date").getValue(String.class);
                            String dueDate = dataSnapshot.child("due_date").getValue(String.class);
                            String key = dataSnapshot.child("key").getValue(String.class);
                            String id = dataSnapshot.child("id").getValue(String.class);
                            model.setImageUrl(imageUrl);
                            model.setCurrent_date(date);
                            model.setKey(key);
                            model.setDue_date(dueDate);
                            model.setId(id);
                        }
                        //textView.setVisibility(View.GONE);
                        modelList.add(model);

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