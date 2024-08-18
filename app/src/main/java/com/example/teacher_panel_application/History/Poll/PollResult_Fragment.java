package com.example.teacher_panel_application.History.Poll;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.teacher_panel_application.Models.PDFModel;
import com.example.teacher_panel_application.Models.ResultModel;
import com.example.teacher_panel_application.Student.Adapter.PollAdapter;
import com.example.teacher_panel_application.Student.PollModel;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.PollResultLayoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PollResult_Fragment extends Fragment {
    public PollResult_Fragment() {
    }

    private PollResultLayoutBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PollResultLayoutBinding.inflate(inflater,container
        ,false);

        List<ResultModel> pollModelList = new ArrayList<>();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TeachersCreatedPoll").child(uid);
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pollModelList.clear();
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.child("question").exists()) {
                            ResultModel model = dataSnapshot.getValue(ResultModel.class);
                            pollModelList.add(model);
                        }

                    }
                    Collections.reverse(pollModelList);
                    PollAdapter adapter = new PollAdapter(getActivity(),pollModelList);
                    binding.studentPollRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.studentPollRecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    binding.nodata.setVisibility(View.GONE);
                }else {
                    binding.nodata.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error getting poll ", Toast.LENGTH_SHORT).show();
                binding.nodata.setText("Error "+error.getMessage());
                binding.nodata.setVisibility(View.VISIBLE);
            }
        });
        return binding.getRoot();
    }
}
