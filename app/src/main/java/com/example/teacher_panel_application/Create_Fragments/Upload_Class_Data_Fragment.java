package com.example.teacher_panel_application.Create_Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teacher_panel_application.Activities.Home_Activity;
import com.example.teacher_panel_application.Activities.Upload_Details_Activity;
import com.example.teacher_panel_application.Models.NetworkUtils;
import com.example.teacher_panel_application.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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

public class Upload_Class_Data_Fragment extends Fragment {

    public Upload_Class_Data_Fragment(){

    }

    private TextInputEditText tName,depart,location,subject,topic,key;
    private EditText minutes;
    private Button uploadBtn;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference,reference1;
    private ProgressBar progressBar;

    boolean isRunning = false;
    private long timeRemainingInMillis;
    long milis = 5000;

    private TextView progressCount;
   // private BlurView blurView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_class_data, container, false);
      //  blurView = view.findViewById(R.id.blurView_uploadActivity);
//        progressBar = view.findViewById(R.id.progress);
//        progressCount = view.findViewById(R.id.progressCount);
        tName = view.findViewById(R.id.teacherName);
        depart = view.findViewById(R.id.department);
        location = view.findViewById(R.id.location);
        subject = view.findViewById(R.id.subject);
        topic = view.findViewById(R.id.todayTopic);
        key = view.findViewById(R.id.edKey);
        minutes = view.findViewById(R.id.edMinutes);
        uploadBtn = view.findViewById(R.id.uploadBtn);

        progressBar = new ProgressBar(getActivity());
        auth = FirebaseAuth.getInstance();
        // String uid = auth.getCurrentUser().getUid();
        String uid = auth.getUid();
        reference  =  FirebaseDatabase.getInstance().getReference("Teacher_Data").child(uid);

        uploadData();
        return view;
    }
    private void uploadData(){
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
                    hashMap.put("endDateTime",minute);


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
                        hashMap.put("currentTime",formattedTime);
                    }


                    int minute1 = Integer.parseInt(minute);
                    //long addMinutes = Long.parseLong(minute);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
