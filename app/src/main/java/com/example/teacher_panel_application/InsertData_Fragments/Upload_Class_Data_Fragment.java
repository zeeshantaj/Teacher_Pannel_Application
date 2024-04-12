package com.example.teacher_panel_application.InsertData_Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.Home.Home_Activity;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.FragmentUploadClassDataBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Upload_Class_Data_Fragment extends Fragment {

    public Upload_Class_Data_Fragment() {

    }

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private FragmentUploadClassDataBinding binding;
    private ProgressDialog dialog;
    private int count = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUploadClassDataBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Teacher_Data").child(uid);
        dialog = new ProgressDialog(getActivity());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();
        binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String name = binding.teacherName.getText().toString();
                String department = binding.department.getText().toString();
                String location = binding.location.getText().toString();
                String subject = binding.subject.getText().toString();
                String topic = binding.todayTopic.getText().toString();
                String key = binding.edKey.getText().toString();
                String minute = binding.edMinutes.getText().toString();


                if (name.isEmpty()) {
                    binding.teacherName.setError("name is empty");
                    return;
                }
                if (department.isEmpty()) {
                    binding.department.setError("department is empty");
                    return;
                }
                if (location.isEmpty()) {
                    binding.location.setError("location is empty");
                    return;
                }
                if (subject.isEmpty()) {
                    binding.subject.setError("subject is empty");
                    return;
                }
                if (topic.isEmpty()) {
                    binding.todayTopic.setError("topic is empty");
                    return;
                }
                if (key.isEmpty()) {
                    binding.edKey.setError("key is empty");
                    return;
                }
                if (minute.isEmpty()) {
                    binding.edMinutes.setError("name is empty");
                    return;
                }

                dialog.setTitle("Please Wait");
                dialog.setMessage("Uploading....");
                dialog.setCancelable(false);
                dialog.show();
                UploadClassModel model = new UploadClassModel();

                model.setName(name);
                model.setDepartment(department);
                model.setLocation(location);
                model.setSubject(subject);
                model.setTopic(topic);
                model.setKey(key);
                model.setMinutes(minute);

                LocalDateTime startedTime = LocalDateTime.now();
                DateTimeFormatter startTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss:a");
                String startedTimeStr = startedTime.format(startTimeFormatter);
                model.setStartedTime(startedTimeStr);

// Get current date and time
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd:hh:mm:ss:a");

// Set current date and time
                String currentDateTimeString = currentDateTime.format(dateTimeFormatter);
                model.setCurrentDateTime(currentDateTimeString);

// Calculate end date and time and set it
                int minute1 = Integer.parseInt(minute);
                LocalDateTime updateDateTime = currentDateTime.plusMinutes(minute1);
                String endDateTimeString = updateDateTime.format(dateTimeFormatter);
                model.setEndDateTime(endDateTimeString);

// Set formatted date and time
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d:MMMM:yyyy hh:mm:a");
                String formattedDateTime = currentDateTime.format(formatter1);
                model.setDateTime(formattedDateTime);
                ;

                reference.setValue(model).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        dialog.dismiss();
                        Intent intent = new Intent(getActivity(), Home_Activity.class);
                        startActivity(intent);
                        getActivity().finish();
                        Toast.makeText(getActivity(), "Details Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });

            }
        });

    }
}