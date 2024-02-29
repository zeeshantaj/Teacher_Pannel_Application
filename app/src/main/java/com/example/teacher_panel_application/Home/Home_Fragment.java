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

public class Home_Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment,container,false);
    }

    private TextView name,depart,subject,duration,locataion,topic,startedAt,timer;
    private CardView cardNode;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private CountDownTimer countDownTimer;
    private long timeRemainingInMillis,timeDifferenceMillis;

    private AdView adView;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.nametxt);
        depart = view.findViewById(R.id.departText);
        subject = view.findViewById(R.id.subjectTxt);
        duration = view.findViewById(R.id.durationTxt);
        locataion = view.findViewById(R.id.locationTxt);
        topic = view.findViewById(R.id.topopicTxt);
        startedAt = view.findViewById(R.id.startedTxt);
        timer = view.findViewById(R.id.countTImer);
        cardNode = view.findViewById(R.id.card_node);
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
      //      showEditDataAlertDialog();
            return true;
        });
    }
    public void getValues(){
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
                                Intent intent = new Intent(getActivity(), Upload_Details_Activity.class);
                                startActivity(intent);
                                getActivity().finish();
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
