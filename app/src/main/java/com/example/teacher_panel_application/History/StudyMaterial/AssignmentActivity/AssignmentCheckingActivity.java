package com.example.teacher_panel_application.History.StudyMaterial.AssignmentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.Access.SendNotification;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.ActivityAssignmentCheckingActvityBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rajat.pdfviewer.PdfViewerActivity;
import com.rajat.pdfviewer.util.saveTo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
        boolean pdfChecked = intent.getBooleanExtra("pdfChecked",false);
        String year = intent.getStringExtra("year");
        String semester = intent.getStringExtra("semester");
        String key = intent.getStringExtra("key");
        String uid = intent.getStringExtra("uid");
        String fromStr = intent.getStringExtra("from");
        String outStr = intent.getStringExtra("out");
        String StudentFCMTOKEN = intent.getStringExtra("StudentFCMTOKEN");

        if (pdfChecked){
            binding.uploadLayout.setVisibility(View.GONE);
            binding.subProgress.setVisibility(View.GONE);
            binding.checkedLayout.setVisibility(View.VISIBLE);
            binding.yourAlreadyCheckTxt.setText("You already Checked this assignment\n" +
                    "marks given "+outStr+" out of "+fromStr);
            binding.recheckBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.uploadLayout.setVisibility(View.VISIBLE);
                }
            });
        }else {
            binding.uploadLayout.setVisibility(View.VISIBLE);
            binding.subProgress.setVisibility(View.GONE);
        }


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

        binding.singUpBtn.setOnClickListener(v -> {
            String remark = "";
            int fromMarksPos = binding.fromMarks.getSelectedItemPosition();
            int outMarksPos = binding.outOfMarks.getSelectedItemPosition();

            if (outMarksPos <= 0) {
                Toast.makeText(this, "Please Select Out", Toast.LENGTH_SHORT).show();
                return;
            }

            if (fromMarksPos <= 0) {
                Toast.makeText(this, "Please Select From", Toast.LENGTH_SHORT).show();
                return;
            }

            remark = binding.SignUpName.getText().toString();
            String from = binding.fromMarks.getSelectedItem().toString();
            String out = binding.outOfMarks.getSelectedItem().toString();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("StudentsSubmittedPDF")
                    .child(uid)
                    .child(key);
            HashMap<String,Object> update = new HashMap<>();
            update.put("checked",true);
            update.put("fromMark",from);
            update.put("outOfMark",out);
            update.put("remark",remark);
            reference.updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    MethodsUtils.showSuccessDialog(AssignmentCheckingActivity.this,"Assignment","Assignment Checking Posted Successfully", SweetAlertDialog.SUCCESS_TYPE);
                    SendNotification sendNotification = new SendNotification(StudentFCMTOKEN,
                            "Your Assignment is checked\n",
                            "check out your marks or remark given by Teacher",AssignmentCheckingActivity.this);
                    sendNotification.sendNotification();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    MethodsUtils.showSuccessDialog(AssignmentCheckingActivity.this,"Error",e.getMessage(), SweetAlertDialog.ERROR_TYPE);
                }
            });



        });

    }
    private void launchPDf(String url,String name){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("empty","empty");
        startActivity(PdfViewerActivity.Companion.launchPdfFromUrl(this,url,name, saveTo.ASK_EVERYTIME,true,hashMap));
    }
}