package com.example.teacher_panel_application.History.StudyMaterial;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.History.ClassHis.ClassHistoryAdapter;
import com.example.teacher_panel_application.Models.PDFModel;
import com.example.teacher_panel_application.TeacherHistoryDB.TeacherDB;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadPdfDataClass extends AsyncTask<Void,Void, List<PDFModel>> {
    private RecyclerView recyclerView;
    private String uid;
    private Context context;
    private TextView textView;
    private ShimmerFrameLayout shimmerFrameLayout;
    private TeacherDB databaseHelper;

    public LoadPdfDataClass( TextView textView, RecyclerView recyclerView,ShimmerFrameLayout shimmerFrameLayout, String uid, Context context) {

        this.textView = textView;
        this.shimmerFrameLayout = shimmerFrameLayout;
        this.recyclerView = recyclerView;
        this.uid = uid;
        this.context = context;
        this.databaseHelper = new TeacherDB(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        shimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    protected List<PDFModel> doInBackground(Void... voids) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TeachersPDFData").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseHelper.clearAllClassData();
                List<PDFModel> modelList = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PDFModel model = dataSnapshot.getValue(PDFModel.class);
                        modelList.add(model);
                        Log.d("MyApp","models inserted "+modelList.size());
//                        for (PDFModel model1:modelList){
//                            //databaseHelper.insertClassData(moPDFModeldel1);
//                            //modelList.add(model1);
//                        }
                        // databaseHelper.clearAllClassData();
                        textView.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }

                    Collections.reverse(modelList);

                    //List<UploadClassModel> modelList = databaseHelper.getAllClassData();

                    ViewStudyHistoryAdapter adapter = new ViewStudyHistoryAdapter(context, modelList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setItemAnimator(null);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    textView.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
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