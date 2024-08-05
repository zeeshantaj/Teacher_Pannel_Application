package com.example.teacher_panel_application.Student.fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.teacher_panel_application.History.StudyMaterial.ViewStudyHistoryAdapter;
import com.example.teacher_panel_application.Models.PDFModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.FragmentStudentPDFBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Student_PDF_Fragment extends Fragment {

    public Student_PDF_Fragment() {
        // Required empty public constructor
    }


    private FragmentStudentPDFBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentStudentPDFBinding.inflate(inflater,container, false);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TeachersPDFData");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //databaseHelper.clearAllClassData();
                List<PDFModel> modelList = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            PDFModel model = dataSnapshot1.getValue(PDFModel.class);

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("StudentInfoShared", Context.MODE_PRIVATE);
                            String year = sharedPreferences.getString("studentYear","");
                            String semester = sharedPreferences.getString("studentSemester","");

                            if (model != null){
                                if (model.getYear().equals(year) && model.getSemester().equals(semester)){
                                    modelList.add(model);
                                    for (PDFModel model1:modelList){
                                     //   downloadAndSavePDF(model1.getPDFUrl(),model1.getPDFName());
                                    }
                                }
                            }
                        }

                    }

                    Collections.reverse(modelList);


                    ViewStudyHistoryAdapter adapter = new ViewStudyHistoryAdapter(getActivity(), modelList);
                    binding.studentPDFRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.studentPDFRV.setItemAnimator(null);
                    binding.studentPDFRV.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error "+error
                        .getMessage(), Toast.LENGTH_SHORT).show();
                MethodsUtils.showFlawDialog(getActivity(),R.drawable.icon_error,"Error",error.getMessage(),1);
            }
        });
        return binding.getRoot();
    }

    private void downloadAndSavePDF(String url, String fileName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(fileName);
        request.setDescription("Downloading PDF");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }

}