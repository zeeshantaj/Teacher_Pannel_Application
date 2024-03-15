package com.example.teacher_panel_application.InsertData_Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teacher_panel_application.Home.Home_Activity;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.FragmentUploadClassDataBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Upload_Class_Data_Fragment extends Fragment {

    public Upload_Class_Data_Fragment(){

    }
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private FragmentUploadClassDataBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUploadClassDataBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        reference  =  FirebaseDatabase.getInstance().getReference("Teacher_Data").child(uid);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = binding.teacherName.getText().toString();
                String department = binding.department.getText().toString();
                String location = binding.location.getText().toString();
                String subject = binding.subject.getText().toString();
                String topic = binding.todayTopic.getText().toString();
                String key = binding.edKey.getText().toString();
                String minute = binding.edMinutes.getText().toString();



                if (name.isEmpty() ){
                    binding.teacherName.setError("name is empty");
                }
                if (department.isEmpty() ){
                    binding.department.setError("department is empty");
                }
                if (location.isEmpty() ){
                    binding.location.setError("location is empty");
                }
                if (subject.isEmpty() ){
                    binding.subject.setError("subject is empty");
                }
                if (topic.isEmpty() ){
                    binding.todayTopic.setError("topic is empty");
                }
                if (key.isEmpty() ){
                    binding.edKey.setError("key is empty");
                }
                if (minute.isEmpty() ){
                    binding.edMinutes.setError("name is empty");
                }

                else {
                    UploadClassModel model = new UploadClassModel();

                    model.setName(name);
                    model.setDepartment(department);
                    model.setLocation(location);
                    model.setSubject(subject);
                    model.setTopic(topic);
                    model.setKey(key);
                    model.setMinutes(minute);
                    model.setEndDateTime(minute);

                    String formattedTime;
                    LocalTime currentTime = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        currentTime = LocalTime.now();
                    }
                    DateTimeFormatter formatter = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        formatter = DateTimeFormatter.ofPattern("hh:mm:ss:a");
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        formattedTime = currentTime.format(formatter);
                        model.setCurrentTime(formattedTime);
                    }


                    int minute1 = Integer.parseInt(minute);
                    //long addMinutes = Long.parseLong(minute);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalTime updateTime = currentTime.plusMinutes(minute1);
                        String endTime = updateTime.format(formatter);
                        model.setEndTime(endTime);
                    }

                    LocalDateTime dateTime = null; // Use LocalDateTime instead of LocalTime
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        dateTime = LocalDateTime.now();
                    }
                    DateTimeFormatter dateTimeFormatter = null; // Include date and time pattern
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd:hh:mm:ss:a");

                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalDateTime updateTime = dateTime.plusMinutes(minute1);
                        String dateTimeString = updateTime.format(dateTimeFormatter);
                        model.setEndDateTime(dateTimeString);
                    }

                    LocalDateTime dateTime1 = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        dateTime1 = LocalDateTime.now();

                        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d:MMMM:yyyy hh:mm:a");
                        String formattedDateTime = dateTime1.format(formatter1);
                        model.setDateTime(formattedDateTime);
                    }
                    reference.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(getActivity(), Home_Activity.class);
                                startActivity(intent);
                                getActivity().finish();
                                Toast.makeText(getActivity(), "Details Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }

}
