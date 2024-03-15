package com.example.teacher_panel_application.EditDataFragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teacher_panel_application.Home.Home_Activity;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.FragmentEditDataBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class EditDataFragment extends Fragment {


    public EditDataFragment() {
        // Required empty public constructor
    }

    private DatabaseReference reference;
    private FirebaseAuth auth;

    FragmentEditDataBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditDataBinding.inflate(inflater, container, false);


        auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Teacher_Data").child(uid);


        getUploadData();
        setBlurView();
        return binding.getRoot();

    }

    private void setBlurView() {
        float radius = 5f;

        View decorView = getActivity().getWindow().getDecorView();
        // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);

        // Optional:
        // Set drawable to draw in the beginning of each blurred frame.
        // Can be used in case your layout has a lot of transparent space and your content
        // gets a too low alpha value after blur is applied.
        Drawable windowBackground = decorView.getBackground();

        binding.blurView.setupWith(rootView, new RenderScriptBlur(getActivity())) // or RenderEffectBlur
                .setFrameClearDrawable(windowBackground) // Optional
                .setBlurRadius(radius);
    }

    private void getUploadData() {
        getData();
        binding.uploadBtnEditFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.teacherNameEditFragment.getText().toString();
                String department = binding.departmentEditFragment.getText().toString();
                String location = binding.locationEditFragment.getText().toString();
                String subject = binding.subjectEditFragment.getText().toString();
                String topic = binding.todayTopicEditFragment.getText().toString();
                String key = binding.edKeyEditFragment.getText().toString();
                String minute = binding.edMinutesEditFragment.getText().toString();

                String formattedTime;

                UploadClassModel model = new UploadClassModel();
                model.setName(name);
                model.setDepartment(department);
                model.setLocation(location);
                model.setSubject(subject);
                model.setTopic(topic);
                model.setKey(key);
                model.setMinutes(minute);
                model.setEndDateTime(minute);

                LocalTime currentTime = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    currentTime = LocalTime.now();
                }
                DateTimeFormatter formatter = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    formatter = DateTimeFormatter.ofPattern("hh:mm:ss:a");
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    formattedTime = currentTime.format(formatter);
                    model.setCurrentTime(formattedTime);
                    //hashMap.put("currentTime", formattedTime);
                }
                int minute1 = Integer.parseInt(minute);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalTime updateTime = currentTime.plusMinutes(minute1);
                    String endTime = updateTime.format(formatter);
                    model.setEndTime(endTime);
                    //hashMap.put("endTime", endTime);
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
                    //hashMap.put("endDateTime", dateTimeString);
                }


                reference.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getActivity(), Home_Activity.class);
                            startActivity(intent);
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
        });
    }

    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String name = snapshot.child("name").getValue(String.class);
                    String dep = snapshot.child("department").getValue(String.class);
                    String loc = snapshot.child("location").getValue(String.class);
                    String sub = snapshot.child("subject").getValue(String.class);
                    String topi = snapshot.child("topic").getValue(String.class);
                    String minute = snapshot.child("minutes").getValue(String.class);
                    String key = snapshot.child("key").getValue(String.class);
                    binding.teacherNameEditFragment.setText(name);
                    binding.departmentEditFragment.setText(dep);
                    binding.locationEditFragment.setText(loc);
                    binding.subjectEditFragment.setText(sub);
                    binding.todayTopicEditFragment.setText(topi);
                    binding.edMinutesEditFragment.setText(minute);
                    binding.edKeyEditFragment.setText(key);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}