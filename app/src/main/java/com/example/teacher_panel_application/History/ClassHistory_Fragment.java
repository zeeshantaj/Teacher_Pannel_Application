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
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.Adapters.AnnounceAdapter;
import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassHistory_Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.history_class,container,false);

        RecyclerView recyclerView = view.findViewById(R.id.classHistory_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<UploadClassModel> modelList = new ArrayList<>();
        ClassHistoryAdapter adapter = new ClassHistoryAdapter(modelList);
        recyclerView.setAdapter(adapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("PostedData").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                        UploadClassModel model = dataSnapshot.getValue(UploadClassModel.class);

//                        AnnouncementModel model = new AnnouncementModel();
//                        if (dataSnapshot.child("title").exists()){
//                            String title = dataSnapshot.child("title").getValue(String.class);
//                            String date = dataSnapshot.child("current_date").getValue(String.class);
//                            String dueDate = dataSnapshot.child("due_date").getValue(String.class);
//                            String des = dataSnapshot.child("description").getValue(String.class);
//
//                            model.setDue_date(dueDate);
//                            model.setTitle(title);
//                            model.setDescription(des);
//                            model.setCurrent_date(date);
//                        }
//                        if (dataSnapshot.child("imageUrl").exists()){
//                            String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
//                            String date = dataSnapshot.child("current_date").getValue(String.class);
//                            model.setImageUrl(imageUrl);
//                            model.setCurrent_date(date);
//                        }
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

        return view;
    }
}
