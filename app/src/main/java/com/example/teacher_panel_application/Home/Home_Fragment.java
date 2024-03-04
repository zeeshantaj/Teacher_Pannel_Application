package com.example.teacher_panel_application.Home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.teacher_panel_application.Activities.Upload_Details_Activity;
import com.example.teacher_panel_application.Fragments.EditDataFragment;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uzairiqbal.circulartimerview.CircularTimerListener;
import com.uzairiqbal.circulartimerview.CircularTimerView;
import com.uzairiqbal.circulartimerview.TimeFormatEnum;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Home_Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment,container,false);
    }

    private TextView name,depart,subject,duration,locataion,topic,startedAt,timer,noClassTxt;
    private CardView cardNode;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private CountDownTimer countDownTimer;
    private long timeRemainingInMillis,timeDifferenceMillis;

    private AdView adView;
    private CircularTimerView progressBar;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.nametxt);
        depart = view.findViewById(R.id.departText);
        subject = view.findViewById(R.id.subjectTxt);
        duration = view.findViewById(R.id.durationTxt);
        locataion = view.findViewById(R.id.locationTxt);
        topic = view.findViewById(R.id.topopicTxt);
        noClassTxt = view.findViewById(R.id.noClass);
        startedAt = view.findViewById(R.id.startedTxt);
        timer = view.findViewById(R.id.countTImer);
        cardNode = view.findViewById(R.id.card_node);
         progressBar = view.findViewById(R.id.progress_circular);
        //getValues();
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Toast.makeText(getActivity(), " successful ", Toast.LENGTH_SHORT).show();
            }
        });
        adView = view.findViewById(R.id.adView);

        //adView.setAdUnitId("ca-app-pub-4144305165966580/3630690721");
        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);
        cardNode.setOnLongClickListener(view1 -> {
            showEditDataAlertDialog();
            return true;
        });

        progressBar.setProgress(0);
        progressBar.setCircularTimerListener(new CircularTimerListener() {
            @Override
            public String updateDataOnTick(long remainingTimeInMs) {
                return String.valueOf((int)Math.ceil((remainingTimeInMs / 1000.f)));
            }

            @Override
            public void onTimerFinished() {
                Toast.makeText(getActivity(), "FINISHED", Toast.LENGTH_SHORT).show();
                progressBar.setPrefix("");
                progressBar.setSuffix("");
                progressBar.setText("FINISHED THANKS!");
            }
        }, 2, TimeFormatEnum.MINUTES, 10);

        progressBar.startTimer();

// To Initialize Timer


        getValues();
    }
    public void getValues(){
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        reference  = FirebaseDatabase.getInstance().getReference("Teacher_Data").child(uid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    noClassTxt.setVisibility(View.GONE);
                    cardNode.setVisibility(View.VISIBLE);
                    String name1 = snapshot.child("name").getValue(String.class);
                    String dep = snapshot.child("department").getValue(String.class);
                    String loc = snapshot.child("location").getValue(String.class);
                    String sub = snapshot.child("subject").getValue(String.class);
                    String topi = snapshot.child("topic").getValue(String.class);
                    String minute1 = snapshot.child("minutes").getValue(String.class);
                    String start = snapshot.child("currentTime").getValue(String.class);
                    String dateTime = snapshot.child("dateTime").getValue(String.class);
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

// To start timer
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

                                UploadClassModel uploadClassModel = new UploadClassModel(name1,dep,loc,sub,topi,minute1,dateTime);
                                DatabaseReference addToQueueReference  = FirebaseDatabase.getInstance().getReference("PostedData").child(uid).child(dateTime);
                                addToQueueReference.setValue(uploadClassModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            reference.removeValue();
                                            cardNode.setVisibility(View.GONE);
                                            noClassTxt.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
//                                Intent intent = new Intent(getActivity(), Upload_Details_Activity.class);
//                                startActivity(intent);
//                                getActivity().finish();
                            }
                        };
                        countDownTimer.start();
                        Log.e("countDownStarted place",countDownTimer.toString());
                    }

                }
                else {
//                    Toast.makeText(getActivity(), "You have not posted class yet", Toast.LENGTH_SHORT).show();
                    cardNode.setVisibility(View.GONE);
                    noClassTxt.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    @Override
    public void onStart() {
        super.onStart();
        Log.e("MyApp","onstart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("MyApp","onstop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("MyApp","onresume");
    }

    private void openEditDataFragment() {
        // Create an instance of the transparent fragment
        EditDataFragment editDataFragment = new EditDataFragment();

        // Add the fragment to the fragment manager
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(android.R.id.content, editDataFragment); // Use android.R.id.content to add the fragment above all views
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
