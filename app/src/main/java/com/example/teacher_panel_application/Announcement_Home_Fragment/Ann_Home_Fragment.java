package com.example.teacher_panel_application.Announcement_Home_Fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.example.teacher_panel_application.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Ann_Home_Fragment extends Fragment {
    public Ann_Home_Fragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ann_home,container,false);

        RecyclerView recyclerView = view.findViewById(R.id.announcement_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<AnnouncementModel> modelList = new ArrayList<>();
        AnnounceAdapter adapter = new AnnounceAdapter(modelList);
        recyclerView.setAdapter(adapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
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

                            Log.e("MyApp","title"+title);
                            Log.e("MyApp","title"+date);
                            Log.e("MyApp","title"+dueDate);
                            Log.e("MyApp","title"+des);

                            model.setDue_date(dueDate);
                            model.setTitle(title);
                            model.setDescription(des);
                            model.setCurrent_date(date);
                        }
                        if (dataSnapshot.child("imageUrl").exists()){
                            String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                            String date = dataSnapshot.child("current_date").getValue(String.class);
                            Log.e("MyApp","title"+imageUrl);
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

        return view;
    }
}
