package com.example.teacher_panel_application.History;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoadClassData extends AsyncTask<Void,Void, List<UploadClassModel>> {
    private RecyclerView recyclerView;
    private String uid;
    private Context context;

    public LoadClassData(RecyclerView recyclerView, String uid, Context context) {
        this.recyclerView = recyclerView;
        this.uid = uid;
        this.context = context;
    }

    @Override
    protected List<UploadClassModel> doInBackground(Void... voids) {

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PostedData").child(uid);
        final List<UploadClassModel> modelList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        UploadClassModel model = dataSnapshot.getValue(UploadClassModel.class);
                        modelList.add(model);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return modelList;
    }

    @Override
    protected void onPostExecute(List<UploadClassModel> modelList) {
        super.onPreExecute();
        ClassHistoryAdapter adapter = new ClassHistoryAdapter(modelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }
}
