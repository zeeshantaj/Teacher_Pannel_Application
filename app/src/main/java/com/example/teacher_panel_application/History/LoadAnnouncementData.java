package com.example.teacher_panel_application.History;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadAnnouncementData extends AsyncTask<Void,Void, List<AnnouncementModel>> {
    private RecyclerView recyclerView;
    private String uid;
    private Context context;
    private TextView textView;
    private ShimmerFrameLayout shimmerFrameLayout;
    public LoadAnnouncementData(RecyclerView recyclerView, TextView textView, ShimmerFrameLayout shimmerFrameLayout,String uid, Context context) {
        this.recyclerView = recyclerView;
        this.textView = textView;
        this.uid = uid;
        this.shimmerFrameLayout = shimmerFrameLayout;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // show shimmer here
        shimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    protected List<AnnouncementModel> doInBackground(Void... voids) {

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Announcement").child(uid);
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
                        textView.setVisibility(View.GONE);
                        modelList.add(model);

                    }
                    Collections.reverse(modelList);
                    AnnounceAdapter adapter = new AnnounceAdapter(modelList,context);
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(staggeredGridLayoutManager);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);

                }
                else {
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
                // dismiss shimmer here
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(context, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return modelList;
    }
}