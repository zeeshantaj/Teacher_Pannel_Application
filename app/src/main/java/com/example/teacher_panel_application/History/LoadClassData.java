package com.example.teacher_panel_application.History;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class LoadClassData extends AsyncTask<Void,Void, List<UploadClassModel>> {
    private RecyclerView recyclerView;
    private String uid;
    private Context context;
    private TextView textView;
    private ShimmerFrameLayout shimmerFrameLayout;
    public LoadClassData(RecyclerView recyclerView, TextView textView, ShimmerFrameLayout shimmerFrameLayout, String uid, Context context) {
        this.recyclerView = recyclerView;
        this.textView = textView;
        this.shimmerFrameLayout = shimmerFrameLayout;
        this.uid = uid;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        shimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    protected List<UploadClassModel> doInBackground(Void... voids) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PostedData").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<UploadClassModel> modelList = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UploadClassModel model = dataSnapshot.getValue(UploadClassModel.class);
                        modelList.add(model);
                    }
                    Collections.reverse(modelList);
                    ClassHistoryAdapter adapter = new ClassHistoryAdapter(modelList, context);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setItemAnimator(null);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    textView.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                } else {
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(context, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return null;
    }
}
