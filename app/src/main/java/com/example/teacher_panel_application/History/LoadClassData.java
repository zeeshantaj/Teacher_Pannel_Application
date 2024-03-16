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
    private ProgressDialog progressDialog;
    private TextView textView;
    public LoadClassData(RecyclerView recyclerView, TextView textView, String uid, Context context) {
        this.recyclerView = recyclerView;
        this.textView = textView;
        this.uid = uid;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected List<UploadClassModel> doInBackground(Void... voids) {



        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PostedData").child(uid);
        List<UploadClassModel> modelList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        UploadClassModel model = dataSnapshot.getValue(UploadClassModel.class);
                        modelList.add(model);
                    }
                    Collections.reverse(modelList);
                    ClassHistoryAdapter adapter = new ClassHistoryAdapter(modelList,context);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(adapter);
                    textView.setVisibility(View.GONE);
                }
                else {
                    textView.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(context, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return modelList;
    }
}
