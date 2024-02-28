package com.example.teacher_panel_application.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teacher_panel_application.Activities.Upload_Details_Activity;
import com.example.teacher_panel_application.Announcement_Home_Fragment.Ann_Home_Fragment;
import com.example.teacher_panel_application.Fragments.EditDataFragment;
import com.example.teacher_panel_application.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Home_Activity extends AppCompatActivity {
    private TextView name,depart,subject,duration,locataion,topic,startedAt,timer;
    private CardView cardNode;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private CountDownTimer countDownTimer;
    private long timeRemainingInMillis,timeDifferenceMillis;

    private AdView adView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Class Details");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.darkBlue));

        name = findViewById(R.id.nametxt);
        depart = findViewById(R.id.departText);
        subject = findViewById(R.id.subjectTxt);
        duration = findViewById(R.id.durationTxt);
        locataion = findViewById(R.id.locationTxt);
        topic = findViewById(R.id.topopicTxt);
        startedAt = findViewById(R.id.startedTxt);
        timer = findViewById(R.id.countTImer);
        cardNode = findViewById(R.id.card_node);
        getValues();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Toast.makeText(Home_Activity.this, " successful ", Toast.LENGTH_SHORT).show();
            }
        });
        adView = findViewById(R.id.adView);

        //adView.setAdUnitId("ca-app-pub-4144305165966580/3630690721");
        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);
        cardNode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showEditDataAlertDialog();
                return true;
            }
        });
    }
    private void getValues(){
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        reference  = FirebaseDatabase.getInstance().getReference("Teacher_Data").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name1 = snapshot.child("Name").getValue(String.class);
                    String dep = snapshot.child("department").getValue(String.class);
                    String loc = snapshot.child("location").getValue(String.class);
                    String sub = snapshot.child("subject").getValue(String.class);
                    String topi = snapshot.child("topic").getValue(String.class);
                    String minute1 = snapshot.child("minutes").getValue(String.class);
                    String start = snapshot.child("currentTime").getValue(String.class);
                    String endTimeString = snapshot.child("endTime").getValue(String.class);
                    name.setText(name1);
                    name.setSelected(true);
                    duration.setText(minute1+" Minutes");
                    duration.setSelected(true);
                    depart.setText(dep);
                    depart.setSelected(true);
                    locataion.setText(loc);
                    locataion.setSelected(true);
                    subject.setText(sub);
                    subject.setSelected(true);
                    topic.setText(topi);
                    topic.setSelected(true);
                    startedAt.setText(start);
                    startedAt.setSelected(true);

                    try {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            LocalTime localTime = LocalTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss:a");
                            String formattedTime = localTime.format(formatter);
                            System.out.println(formattedTime);
                            SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm:ss:a");
                            Date currentTime = inputFormat.parse(formattedTime);
                            Date endTime = inputFormat.parse(endTimeString);
                            timeDifferenceMillis = endTime.getTime() - currentTime.getTime();
                            Log.d("MyApp", String.valueOf(timeDifferenceMillis));
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    if (countDownTimer == null){
                        countDownTimer = new CountDownTimer(timeDifferenceMillis, 1000) {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onTick(long millisUntilFinished) {
                                timeRemainingInMillis = millisUntilFinished;
                                updateCountdownText();
                                if (millisUntilFinished < 50000){
                                    timer.setTextColor(R.color.red);
                                }
                            }
                            @Override
                            public void onFinish() {
                                // Perform any actions after the countdown finishes
                                reference.removeValue();
                                Intent intent = new Intent(Home_Activity.this, Upload_Details_Activity.class);
                                startActivity(intent);
                                finish();
                            }
                        };
                        countDownTimer.start();
                        Log.e("countDownStarted place",countDownTimer.toString());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("MyApp",error.toString());
            }
        });
    }
    private void updateCountdownText() {
        int hours = (int) (timeRemainingInMillis / 1000) / 3600;
        int minutes = (int) ((timeRemainingInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeRemainingInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.US,"%02d:%02d:%02d", hours, minutes, seconds);
        timer.setText(timeFormatted);
    }

    private void showEditDataAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Data")
                .setMessage("Do you want to edit the data?")
                .setPositiveButton("Edit Data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openEditDataFragment();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void openEditDataFragment() {
        // Create an instance of the transparent fragment
        EditDataFragment editDataFragment = new EditDataFragment();

        // Add the fragment to the fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(android.R.id.content, editDataFragment); // Use android.R.id.content to add the fragment above all views
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.announcementData){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(android.R.id.content,new Ann_Home_Fragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }

        if (countDownTimer == null) {
            getValues();
            //startCountdown();
        }

//        Log.e("countDownResume",countDownTimer.toString());
        getValues();
    }
    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
//        if (countDownTimer!=null){
//            countDownTimer.cancel();
//            Log.e("countDownPause",countDownTimer.toString());
//        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null; // Set countDownTimer to null to indicate it was canceled
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
        if (adView != null) {
            adView.destroy();
        }
    }
}