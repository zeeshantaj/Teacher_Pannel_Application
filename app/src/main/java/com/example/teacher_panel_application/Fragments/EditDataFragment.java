package com.example.teacher_panel_application.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teacher_panel_application.Home.Home_Activity;
import com.example.teacher_panel_application.R;
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

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class EditDataFragment extends Fragment {


    public EditDataFragment() {
        // Required empty public constructor
    }

    private BlurView blurView;
    private TextInputEditText tName,depart,location,subject,topic,key;
    private EditText minutes;
    private Button uploadBtn;
    private DatabaseReference reference;
    private FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_data, container, false);

            blurView = view.findViewById(R.id.blurView);
        tName = view.findViewById(R.id.teacherNameEditFragment);
        depart = view.findViewById(R.id.departmentEditFragment);
        location = view.findViewById(R.id.locationEditFragment);
        subject = view.findViewById(R.id.subjectEditFragment);
        topic = view.findViewById(R.id.todayTopicEditFragment);
        key = view.findViewById(R.id.edKeyEditFragment);
        minutes = view.findViewById(R.id.edMinutesEditFragment);
        uploadBtn = view.findViewById(R.id.uploadBtnEditFragment);
        auth = FirebaseAuth.getInstance();
        //String uid = auth.getCurrentUser().getUid();
        String uid = auth.getUid();
        reference  =  FirebaseDatabase.getInstance().getReference("Teacher_Data").child(uid);
        float radius = 5f;

        View decorView = getActivity().getWindow().getDecorView();
        // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);

        // Optional:
        // Set drawable to draw in the beginning of each blurred frame.
        // Can be used in case your layout has a lot of transparent space and your content
        // gets a too low alpha value after blur is applied.
        Drawable windowBackground = decorView.getBackground();

        blurView.setupWith(rootView, new RenderScriptBlur(getActivity())) // or RenderEffectBlur
                .setFrameClearDrawable(windowBackground) // Optional
                .setBlurRadius(radius);

        getData();
        return view;

    }

    private void getData(){

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = tName.getText().toString();
                String dep = depart.getText().toString();
                String loc = location.getText().toString();
                String sub = subject.getText().toString();
                String topi = topic.getText().toString();
                String key1 = key.getText().toString();
                String minute = minutes.getText().toString();

                String formattedTime;

                if (name.isEmpty() ){
                    tName.setError("name is empty");
                }
                if (dep.isEmpty() ){
                    depart.setError("department is empty");
                }
                if (loc.isEmpty() ){
                    location.setError("location is empty");
                }
                if (sub.isEmpty() ){
                    subject.setError("subject is empty");
                }
                if (topi.isEmpty() ){
                    topic.setError("topic is empty");
                }
                if (key1.isEmpty() ){
                    key.setError("key is empty");
                }
                if (minute.isEmpty() ){
                    minutes.setError("name is empty");
                }

                else {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("Name",name);
                    hashMap.put("department",dep);
                    hashMap.put("location",loc);
                    hashMap.put("subject",sub);
                    hashMap.put("topic",topi);
                    hashMap.put("key",key1);
                    hashMap.put("minutes",minute);

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
                        hashMap.put("currentTime",formattedTime);
                    }
                    int minute1 = Integer.parseInt(minute);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        LocalTime updateTime = currentTime.plusMinutes(minute1);
                        String endTime = updateTime.format(formatter);
                        hashMap.put("endTime",endTime);
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
                        hashMap.put("endDateTime", dateTimeString);
                    }


                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
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
            }
        });

    }
}