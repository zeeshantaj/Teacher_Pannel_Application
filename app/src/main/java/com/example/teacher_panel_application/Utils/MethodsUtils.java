package com.example.teacher_panel_application.Utils;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.teacher_panel_application.EditDataFragments.EditDataFragment;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MethodsUtils {
    public static void shareOnWhatsapp(Context context,String name, String loc, String dura, String dep, String sub, String top, String start){
        String teacherName = "Teacher: " + name + "\n";
        String location = "Location: " + loc + "\n";
        String duration = "Duration: " + dura + " minutes\n";
        String depart = "Major: " + dep + "\n";
        String subject = "Subject: " + sub + "\n";
        String topic = "Today's Topic: " + top + "\n";
        String started = "Started At: " + start;

        String message = teacherName + location + duration + depart + subject + topic + started;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        shareIntent.setPackage("com.whatsapp");
        context.startActivity(shareIntent);
    }
    public static void getData(Context context, TextInputEditText edName, TextInputEditText edDepartment,
                         TextInputEditText edLocation, TextInputEditText edSubject,
                         TextInputEditText edTopic, TextInputEditText edKey, EditText edMinute) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Teacher_Data").child(uid);
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
                    edName.setText(name);
                    edDepartment.setText(dep);
                    edLocation.setText(loc);
                    edSubject.setText(sub);
                    edTopic.setText(topi);
                    edMinute.setText(minute);
                    edKey.setText(key);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void setFragment(FragmentManager fragmentManager, Fragment fragment){
        // Create an instance of the transparent fragment

        // Add the fragment to the fragment manager
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(android.R.id.content, fragment); // Use android.R.id.content to add the fragment above all views
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static DatabaseReference getCurrentUserRef(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        return FirebaseDatabase.getInstance().getReference().child("UsersInfo").child(uid);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void uploadTimeIntoDB(String currentDateTimeString,String endDateTimeString,String formattedDateTime,String minute){

        LocalDateTime startedTime = LocalDateTime.now();
        DateTimeFormatter startTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss:a");
        String startedTimeStr = startedTime.format(startTimeFormatter);

// Get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd:hh:mm:ss:a");

// Set current date and time
        currentDateTimeString = currentDateTime.format(dateTimeFormatter);


// Calculate end date and time and set it
        int minute1 = Integer.parseInt(minute);
        LocalDateTime updateDateTime = currentDateTime.plusMinutes(minute1);
        endDateTimeString = updateDateTime.format(dateTimeFormatter);

// Set formatted date and time
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d:MMMM:yyyy hh:mm:a");
        formattedDateTime = currentDateTime.format(formatter1);



    }
}
