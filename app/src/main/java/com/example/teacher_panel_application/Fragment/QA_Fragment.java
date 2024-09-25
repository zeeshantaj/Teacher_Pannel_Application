package com.example.teacher_panel_application.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.teacher_panel_application.Adapters.QAAdapter;
import com.example.teacher_panel_application.Models.PDFModel;
import com.example.teacher_panel_application.Models.QAModel;
import com.example.teacher_panel_application.databinding.FragmentQABinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QA_Fragment extends Fragment {

    public QA_Fragment() {
    }
    private FragmentQABinding binding;
    private QAAdapter adapter;
    private List<QAModel> modelList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQABinding.inflate(inflater,container,false);

        modelList = new ArrayList<>();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Students_Posted_Questions");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    modelList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            QAModel model = dataSnapshot1.getValue(QAModel.class);
                            modelList.add(model);
                        }
                    }
                    Collections.reverse(modelList);
                    adapter = new QAAdapter(getContext(),modelList);
                    binding.QARecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.QARecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();

    }
}
