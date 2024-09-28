package com.example.teacher_panel_application.Student.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.teacher_panel_application.Models.QAModel;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.FragmentAskQuestionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Ask_Question_Activity extends AppCompatActivity {
    private FragmentAskQuestionBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Ask Question");
        binding = FragmentAskQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.questionPostBtn.setOnClickListener(v -> {


            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd:hh:mm:ss:a");
            String currentDateTimeString = currentDateTime.format(dateTimeFormatter);


            if (binding.questionEdt.getText().toString().isEmpty()){
                binding.questionEdt.setError("Write Question First! can't post empty");
                return;
            }
            String question = binding.questionEdt.getText().toString();


            FirebaseAuth auth = FirebaseAuth.getInstance();
            String uid = auth.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance()
                    .getReference("Students_Posted_Questions")
                    .child(uid)
                    .child(getMillis());
            QAModel model = new QAModel();
            model.setQuestion(question);
            model.setAnswer("");
            model.setPostedData(currentDateTimeString);
            model.setStudentName(MethodsUtils.getString(this,"studentName"));
            model.setStudentUid(uid);
            model.setYear(MethodsUtils.getString(this,"studentYear"));
            model.setSemester(MethodsUtils.getString(this,"studentSemester"));
            model.setMajor(MethodsUtils.getString(this,"studentMajor"));
            model.setTeacherName("");
            model.setTeacherUid("");
            model.setKey(reference.getKey());
            reference.setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    MethodsUtils.showSuccessDialog(Ask_Question_Activity.this, "Success", "Question Posted Successfully", SweetAlertDialog.SUCCESS_TYPE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },1000);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    MethodsUtils.showSuccessDialog(Ask_Question_Activity.this,"Error","Question not Posted", SweetAlertDialog.ERROR_TYPE);
                }
            });

        });

        binding.questionEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.question.setText(String.valueOf(s));
                if (s.toString().isEmpty()){
                    binding.textView31.setVisibility(View.VISIBLE);
                }else {
                    binding.textView31.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private String getMillis() {
        Calendar calendar = Calendar.getInstance();
        long milli = calendar.getTimeInMillis();
        return String.valueOf(milli);
    }
}
