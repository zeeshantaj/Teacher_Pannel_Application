package com.example.teacher_panel_application.History.StudyMaterial.AssignmentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.ActivityAssignmentCheckingActvityBinding;
import com.rajat.pdfviewer.PdfViewerActivity;
import com.rajat.pdfviewer.util.saveTo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AssignmentCheckingActivity extends AppCompatActivity {

    private ActivityAssignmentCheckingActvityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentCheckingActvityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Check Assignment");

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        String name = intent.getStringExtra("name");
        String imgUrl = intent.getStringExtra("imageUrl");
        String purpose = intent.getStringExtra("pdfPurpose");
        String pdfUrl = intent.getStringExtra("pdfUrl");
        String pdfIdentifier = intent.getStringExtra("pdfIdentifier");
        String pdfName = intent.getStringExtra("pdfName");
        String pdfChecked = intent.getStringExtra("pdfChecked");
        String year = intent.getStringExtra("year");
        String semester = intent.getStringExtra("semester");


        Glide.with(this)
                .load(imgUrl)
                .into(binding.submittedPdfUserImg);
        binding.submittedPdfUserName.setText(name);
        binding.submittedPdfDate.setText("Date : "+date);

        binding.pdfUploadDate.setText("Date : "+date);
        binding.pdfUploadedPur.setText(purpose);
        binding.yearSemesterTxt.setText("Group: "+year+" ("+semester+")");
        binding.pdfFileName.setText(pdfName);

        binding.cardView3.setOnClickListener(v -> {
            launchPDf(pdfUrl,pdfName);
        });

        List<String> arr = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            if (i == 0) {
                arr.add("Select Marks");
            }
            arr.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                arr
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.outOfMarks.setAdapter(adapter);

        List<String> arr1 = new ArrayList<>();
        arr1.add("Select Marks");
        for (int i = 5; i <= 100; i += 5) {
            arr1.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                arr1
        );

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.fromMarks.setAdapter(adapter1);


        binding.SignUpName.requestFocus();
// Suppress the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.SignUpName.getWindowToken(), 0);
    }
    private void launchPDf(String url,String name){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("empty","empty");
        startActivity(PdfViewerActivity.Companion.launchPdfFromUrl(this,url,name, saveTo.ASK_EVERYTIME,true,hashMap));
    }
}