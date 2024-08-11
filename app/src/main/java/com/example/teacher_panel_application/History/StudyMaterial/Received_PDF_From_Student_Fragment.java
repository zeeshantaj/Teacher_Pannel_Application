package com.example.teacher_panel_application.History.StudyMaterial;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.History.StudyMaterial.Adapter.SubmittedModel;
import com.example.teacher_panel_application.History.StudyMaterial.Adapter.SubmittedPDFAdapter;
import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.example.teacher_panel_application.Models.PDFModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.FragmentReceivedPDFFromStudentBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.rajat.pdfviewer.PdfViewerActivity;
import com.rajat.pdfviewer.util.saveTo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Received_PDF_From_Student_Fragment extends BottomSheetDialogFragment {

    public Received_PDF_From_Student_Fragment() {
        // Required empty public constructor
    }

    FragmentReceivedPDFFromStudentBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentReceivedPDFFromStudentBinding.inflate(inflater,container, false);

        if (getArguments() != null) {
            String json = getArguments().getString("PDFData");
            PDFModel model = new Gson().fromJson(json, PDFModel.class);
            if (model != null) {

                // Use the data as needed
                binding.pdfFileName.setText(model.getPDFName());
                binding.pdfUploadDate.setText(model.getDateTime());
                binding.pdfUploadedPur.setText(model.getPurpose());
                binding.yearSemesterTxt.setText("Group: "+model.getYear()+" ("+model.getSemester()+")");

                binding.cardView3.setOnClickListener(v -> {
                    launchPDf(model.getPDFUrl(),model.getPDFName());
                });

            }
        }



        return binding.getRoot();
    }
    private void setData(){
        List<SubmittedModel> modelList = new ArrayList<>();




        binding.submittedPdfRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        SubmittedPDFAdapter adapter = new SubmittedPDFAdapter(getActivity(),modelList);
        binding.submittedPdfRecycler.setAdapter(adapter);
    }
    private void launchPDf(String url,String name){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("empty","empty");
        startActivity(PdfViewerActivity.Companion.launchPdfFromUrl(getActivity(),url,name, saveTo.ASK_EVERYTIME,true,hashMap));
    }
}