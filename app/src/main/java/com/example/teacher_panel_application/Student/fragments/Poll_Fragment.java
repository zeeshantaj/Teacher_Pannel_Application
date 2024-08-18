package com.example.teacher_panel_application.Student.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Student.Adapter.PollAdapter;
import com.example.teacher_panel_application.Student.PollModel;
import com.example.teacher_panel_application.databinding.FragmentAnnouncementNotificationBinding;
import com.example.teacher_panel_application.databinding.FragmentPollBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Poll_Fragment extends Fragment {

    public Poll_Fragment() {
    }

    FragmentPollBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPollBinding.inflate(inflater,container,false);

        List<PollModel> pollModelList = new ArrayList<>();
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TeachersCreatedPoll");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pollModelList.clear();
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot keySnapshot : dataSnapshot.getChildren()) {
                            if (keySnapshot.child("question").exists()) {

                                String question = keySnapshot.child("question").getValue(String.class);
                                String op1 = keySnapshot.child("option0").getValue(String.class);
                                String op2 = keySnapshot.child("option1").getValue(String.class);
                                String op3 = keySnapshot.child("option2").getValue(String.class);
                                String op4 = keySnapshot.child("option3").getValue(String.class);
                                String pollId = keySnapshot.child("pollId").getValue(String.class);
                                String uid = keySnapshot.child("uid").getValue(String.class);
                                //String postedId = keySnapshot.child("uid").getValue(String.class);



                                PollModel model = new PollModel();
                                model.setQuestion(question);
                                model.setPollId(pollId);
                                model.setUid(uid);
                                model.setKey(keySnapshot.getKey());

                                // Check and set each option if it exists
                                if (op1 != null && !op1.isEmpty()) {
                                    model.setOption1(op1);
                                }
                                if (op2 != null && !op2.isEmpty()) {
                                    model.setOption2(op2);
                                }
                                if (op3 != null && !op3.isEmpty()) {
                                    model.setOption3(op3);
                                }
                                if (op4 != null && !op4.isEmpty()) {
                                    model.setOption4(op4);
                                }
                                pollModelList.add(model);

                            }
                        }
                    }
                    Collections.reverse(pollModelList);
                    PollAdapter adapter = new PollAdapter(pollModelList,getActivity());
                    binding.studentPollRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.studentPollRecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error getting poll ", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }
}