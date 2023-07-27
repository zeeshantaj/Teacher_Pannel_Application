package com.example.teacher_panel_application.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teacher_panel_application.Models.NetworkUtils;
import com.example.teacher_panel_application.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.MultiplePulse;
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

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class Upload_Details_Activity extends AppCompatActivity {


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

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_details);

        getSupportActionBar().setTitle("Upload Details Here");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.darkBlue));

        progressBar = findViewById(R.id.progress);
        progressCount = findViewById(R.id.progressCount);
        tName = findViewById(R.id.teacherName);
        depart = findViewById(R.id.department);
        location = findViewById(R.id.location);
        subject = findViewById(R.id.subject);
        topic = findViewById(R.id.todayTopic);
        key = findViewById(R.id.edKey);
        minutes = findViewById(R.id.edMinutes);
        uploadBtn = findViewById(R.id.uploadBtn);

        auth = FirebaseAuth.getInstance();
       // String uid = auth.getCurrentUser().getUid();
        String uid = auth.getUid();
        reference  =  FirebaseDatabase.getInstance().getReference("Teacher_Data").child(uid);



        uploadData();
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

                    progressBar.setVisibility(View.VISIBLE);
                    progressCount.setVisibility(View.VISIBLE);
                    progressBar.setProgress(0);

                    CountDownTimer countDownTimer = new CountDownTimer(milis,1000) {
                        @Override
                        public void onTick(long l) {

                            int progress = (int) (100 * l / milis);
                            progressBar.incrementProgressBy(progress);
                            progressCount.setText("Uploading Data....\n"+"Progress "+progress);
                            progressBar.setProgress(progress);
                        }

                        @Override
                        public void onFinish() {

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
                                        Intent intent = new Intent(Upload_Details_Activity.this,Home_Activity.class);
                                        startActivity(intent);
                                        Toast.makeText(Upload_Details_Activity.this, "Details Uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Upload_Details_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    };
                    countDownTimer.start();


                }
            }
        });
    }
    private void updateProgressBar() {

        // Calculate the percentage of time remaining and update the ProgressBar

    }
    @Override
    protected void onStart() {
        super.onStart();


        checkInternetAccess();
//        if (NetworkUtils.isNetworkAvailable(this)) {
//            if (NetworkUtils.hasInternetAccess()) {
//
//                Toast.makeText(this, "YOu have internet", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "YOu have internet service", Toast.LENGTH_SHORT).show();
//
//
//            }
//            else {
//                //for internet service
//                Snackbar snackbar = Snackbar.make(this.getCurrentFocus(), "You have no internet connection", Snackbar.LENGTH_LONG);
//                snackbar.setActionTextColor(getResources().getColor(R.color.darkBlueshade));
//                snackbar.setAction("Check Again", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // perform any action when the button on the snackbar is clicked here in this case
//                        // it shows a simple toast
//                        snackbar.dismiss();
//                        if (NetworkUtils.hasInternetAccess()){
//
//                        }
//
//                    }
//                });
//                snackbar.show();
//            }
//        }
//        else {
//            // for check if device is connection to internet
//            Snackbar snackbar = Snackbar.make(this.getCurrentFocus(), "Make sure you connected to the internet", Snackbar.LENGTH_LONG);
//            snackbar.setActionTextColor(getResources().getColor(R.color.darkBlueshade));
//            snackbar.setAction("DISMISS", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // perform any action when the button on the snackbar is clicked here in this case
//                    // it shows a simple toast
//                    snackbar.dismiss();
//                }
//            });
//            snackbar.show();
//        }
    }
    private void checkInternetAccess() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            NetworkUtils.hasInternetAccess(new NetworkUtils.InternetAccessCallback() {
                @Override
                public void onInternetAccessResult(boolean hasInternetAccess) {
                    if (hasInternetAccess) {
                        // You have internet access, do your other operations here
                        // ...
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(Upload_Details_Activity.this, "You have internet", Toast.LENGTH_SHORT).show();
                                // Perform other operations
                                // ...
                                auth = FirebaseAuth.getInstance();
                                String uid = auth.getCurrentUser().getUid();
                                reference1 = FirebaseDatabase.getInstance().getReference("Teacher_Data").child(uid);
                                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (!snapshot.exists()) {
                                            Toast.makeText(Upload_Details_Activity.this, "You can Upload Details Here", Toast.LENGTH_SHORT).show();
                                            } else {

                                            String endTimeString = snapshot.child("endDateTime").getValue(String.class);
//                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                                                LocalTime localTime = LocalTime.now();
//                                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss:a");
//                                                String formattedTime = localTime.format(formatter);
//                                                System.out.println(formattedTime);
//
//                                                try {
//
//                                                    SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm:ss:a");
//
//                                                    Date currentTime = inputFormat.parse(formattedTime);
//                                                    Date endTime = inputFormat.parse(endTimeString);
//
//
//                                                    if (currentTime.after(endTime)) {
//                                                        reference.removeValue();
//                                                        dialog.dismiss();
//                                                        Toast.makeText(Upload_Details_Activity.this, "Time is greater data should be deleted", Toast.LENGTH_SHORT).show();
//                                                        //finish();
//                                                    } else if (currentTime.before(endTime)) {
//                                                        Intent intent = new Intent(Upload_Details_Activity.this, Home_Activity.class);
//                                                        startActivity(intent);
//                                                        Toast.makeText(Upload_Details_Activity.this, "Time is not greater data should not be deleted", Toast.LENGTH_SHORT).show();
//                                                        finish();
//
//                                                        dialog.dismiss();
//                                                    }
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }

//                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss:a");
//                                                LocalTime localTime = LocalTime.now();
//                                                String formattedTime = localTime.format(formatter);
//                                                System.out.println(formattedTime);
//
//                                                try {
//                                                    LocalTime currentTime = LocalTime.parse(formattedTime, formatter);
//                                                    LocalTime endTime = LocalTime.parse(endTimeString, formatter);
//
//                                                    if (currentTime.isAfter(endTime)) {
//                                                        reference.removeValue();
//                                                        dialog.dismiss();
//                                                        Toast.makeText(Upload_Details_Activity.this, "Time is greater data should be deleted", Toast.LENGTH_SHORT).show();
//                                                        //finish();
//                                                    } else if (currentTime.isBefore(endTime)) {
//                                                        Intent intent = new Intent(Upload_Details_Activity.this, Home_Activity.class);
//                                                        startActivity(intent);
//                                                        Toast.makeText(Upload_Details_Activity.this, "Time is not greater data should not be deleted", Toast.LENGTH_SHORT).show();
//                                                        finish();
//                                                        dialog.dismiss();
//                                                    }
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd:hh:mm:ss:a");
                                                LocalDateTime localTime = LocalDateTime.now();
                                                String formattedTime = localTime.format(formatter);

                                                //System.out.println(formattedTime);

                                                try {
                                                    LocalDateTime currentTime = LocalDateTime.parse(formattedTime, formatter);
                                                    LocalDateTime endTime = LocalDateTime.parse(endTimeString, formatter);
                                                    Log.e("endTimeDate",endTime.toString());
                                                    Log.e("currentTime",currentTime.toString());
                                                    if (currentTime.isAfter(endTime)) {
                                                        reference.removeValue();
                                                        Toast.makeText(Upload_Details_Activity.this, "Time is greater data should be deleted", Toast.LENGTH_SHORT).show();
                                                        //finish();
                                                    } else if (currentTime.isBefore(endTime)) {
                                                        Intent intent = new Intent(Upload_Details_Activity.this, Home_Activity.class);
                                                        startActivity(intent);
                                                        Toast.makeText(Upload_Details_Activity.this, "Time is not greater data should not be deleted", Toast.LENGTH_SHORT).show();
                                                        finish();

                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.d("MyApp", error.getMessage());

                                    }
                                });
                            }
                        });
                    } else {
                        // You have no internet connection
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "You have no internet connection", Snackbar.LENGTH_INDEFINITE);
                                snackbar.setActionTextColor(getResources().getColor(R.color.darkBlueshade));
                                snackbar.setAction("Check Again", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        checkInternetAccess();
                                    }
                                });
                                snackbar.show();
                            }
                        });
                    }
                }
            });
        } else {
            // No network connectivity
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Make sure you are connected to the internet", Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(getResources().getColor(R.color.darkBlueshade));
            snackbar.setAction("DISMISS", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }

}