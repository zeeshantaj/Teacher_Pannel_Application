package com.example.teacher_panel_application.Home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Student.fragments.Ask_Question_Activity;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.ActivityPostQuestionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PostAnswerActivity extends AppCompatActivity {

    private ActivityPostQuestionBinding binding;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        String key = intent.getStringExtra("key");
        String question = intent.getStringExtra("question");


        binding.question.setText(question);

        binding.questionEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.asnwer.setText(String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.questionPostBtn.setOnClickListener(v -> {
           if (binding.asnwer.getText().toString().isEmpty()){
               binding.questionEdt.setError("Write Answer First! can't post empty");
               return;
           }

            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd:hh:mm:ss:a");
            String currentDateTimeString = currentDateTime.format(dateTimeFormatter);


            DatabaseReference reference = FirebaseDatabase.getInstance()
                    .getReference("Students_Posted_Questions")
                    .child(uid)
                    .child(key);
            HashMap<String ,Object > values = new HashMap<>();
            values.put("teacherUid", MethodsUtils.getCurrentUID());
            values.put("answer", binding.questionEdt.getText().toString());
            values.put("teacherName", MethodsUtils.getString(PostAnswerActivity.this, "teacherName"));
            values.put("answerDate", currentDateTimeString);
            reference.updateChildren(values).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    MethodsUtils.showSuccessDialog(PostAnswerActivity.this, "Success", "Answer Posted Successfully", SweetAlertDialog.SUCCESS_TYPE);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    MethodsUtils.showSuccessDialog(PostAnswerActivity.this, "Error ", "Failed to post Answer", SweetAlertDialog.SUCCESS_TYPE);

                }
            });
        });

    }
}