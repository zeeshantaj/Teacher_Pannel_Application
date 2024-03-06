package com.example.teacher_panel_application.History;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoadNotificationData extends AsyncTask<Void,Void, List<AnnouncementModel>> {
    private RecyclerView recyclerView;
    private String uid;
    private Context context;

    public LoadNotificationData(RecyclerView recyclerView, String uid, Context context) {
        this.recyclerView = recyclerView;
        this.uid = uid;
        this.context = context;
    }
    @Override
    protected List<AnnouncementModel> doInBackground(Void... voids) {

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Announcement").child(uid);
        List<AnnouncementModel> modelList = new ArrayList<>();
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
                    AnnounceAdapter adapter = new AnnounceAdapter(modelList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return modelList;
    }
}