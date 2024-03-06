package com.example.teacher_panel_application.Home;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.teacher_panel_application.Fragments.EditDataFragment;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.Notification.ClassEnded_NotificationService;
import com.example.teacher_panel_application.Notification.NotificationBroadcastReceiver;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.HomeFragmentBinding;
import com.google.android.gms.ads.AdRequest;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.uzairiqbal.circulartimerview.CircularTimerListener;
import com.uzairiqbal.circulartimerview.TimeFormatEnum;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Home_Fragment extends Fragment {

    HomeFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        //return inflater.inflate(R.layout.home_fragment,container,false);

        return binding.getRoot();
    }

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private CountDownTimer countDownTimer;
    private long timeRemainingInMillis, timeDifferenceMillis;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                //Toast.makeText(getActivity(), " successful ", Toast.LENGTH_SHORT).show();
            }
        });
//        adView = view.findViewById(R.id.adView);

        //adView.setAdUnitId("ca-app-pub-4144305165966580/3630690721");
        AdRequest adRequest = new AdRequest.Builder().build();

        binding.adView.loadAd(adRequest);
        binding.cardNode.setOnLongClickListener(view1 -> {
            showEditDataAlertDialog();
            return true;
        });

        binding.progressCircular.setProgress(0);
        binding.progressCircular.setCircularTimerListener(new CircularTimerListener() {
            @Override
            public String updateDataOnTick(long remainingTimeInMs) {
                return String.valueOf((int) Math.ceil((remainingTimeInMs / 1000.f)));
            }

            @Override
            public void onTimerFinished() {
                Toast.makeText(getActivity(), "FINISHED", Toast.LENGTH_SHORT).show();
                binding.progressCircular.setPrefix("");
                binding.progressCircular.setSuffix("");
                binding.progressCircular.setText("FINISHED THANKS!");
            }
        }, 2, TimeFormatEnum.MINUTES, 10);

        binding.progressCircular.startTimer();

// To Initialize Timer

//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//            @Override
//            public void onComplete(@NonNull Task<String> task) {
//                if (task.isSuccessful()) {
//                    String fcmToken = task.getResult();
//
//                    Map<String, String> notificationMessage = new HashMap<>();
//                    notificationMessage.put("title", "Your notification title");
//                    notificationMessage.put("body", "Your notification message");
//
//                    // Specify the delivery time (in UNIX time milliseconds)
//                    long deliveryTimeMillis = System.currentTimeMillis() + (2 * 60 * 1000); // 5 minutes from now
//
//                    // Build the message payload
//                    Map<String, String> messagePayload = new HashMap<>();
//                    messagePayload.put("message", new Gson().toJson(notificationMessage));
//                    messagePayload.put("schedule", String.valueOf(deliveryTimeMillis));
//
//                    // Send the message to the FCM token
//                    FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(fcmToken)
//                            .setData(messagePayload)
//                            .build());
//                } else {
//                    Log.e("MyApp", "Failed to get FCM token");
//                }
//            }
//        });

//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 14);
//        calendar.set(Calendar.MINUTE, 13);
//        calendar.set(Calendar.SECOND, 0);



        getValues();
    }

    public void getValues() {
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Teacher_Data").child(uid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.cardNode.setVisibility(View.VISIBLE);
                    binding.noClass.setVisibility(View.GONE);
                    String name = snapshot.child("name").getValue(String.class);
                    String dep = snapshot.child("department").getValue(String.class);
                    String loc = snapshot.child("location").getValue(String.class);
                    String sub = snapshot.child("subject").getValue(String.class);
                    String topi = snapshot.child("topic").getValue(String.class);
                    String minute = snapshot.child("minutes").getValue(String.class);
                    String start = snapshot.child("currentTime").getValue(String.class);
                    String dateTime = snapshot.child("dateTime").getValue(String.class);
                    String endTimeString = snapshot.child("endTime").getValue(String.class);

                setTextView(name,dep,minute,loc,sub,topi,start);

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (countDownTimer == null) {

//                        Intent alarmIntent = new Intent(getActivity(), ClassEnded_NotificationService.class);
//
//                        alarmIntent.setAction("START_ALARM");
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent,
//                                PendingIntent.FLAG_UPDATE_CURRENT);
//
//                        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(getActivity().ALARM_SERVICE);
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                            if (alarmManager.canScheduleExactAlarms()) {
//                                // App has permission to schedule exact alarms
//                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeDifferenceMillis, pendingIntent);
//                            } else {
//                                // App does not have permission, handle SecurityException
//                                try {
//                                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(timeDifferenceMillis, pendingIntent), pendingIntent);
//                                } catch (SecurityException e) {
//                                    // Handle the exception
//                                    Log.e("AlarmManager", "Failed to schedule exact alarm", e);
//                                }
//                            }
//                        }

                        //    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeDifferenceMillis, pendingIntent);

// To start timer



                        setNotification(timeDifferenceMillis,uid,name,dep,loc,sub,topi,minute,dateTime);
                        countDownTimer = new CountDownTimer(timeDifferenceMillis, 1000) {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onTick(long millisUntilFinished) {
                                timeRemainingInMillis = millisUntilFinished;
                                updateCountdownText();
                                if (millisUntilFinished < 50000) {
                                    binding.countTImer.setTextColor(R.color.red);
                                }
                            }

                            @Override
                            public void onFinish() {
                                // Perform any actions after the countdown finishes

                                UploadClassModel uploadClassModel = new UploadClassModel(name, dep, loc, sub, topi, minute, dateTime);
                                DatabaseReference addToQueueReference = FirebaseDatabase.getInstance().getReference("PostedData").child(uid).child(dateTime);
                                addToQueueReference.setValue(uploadClassModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            reference.removeValue();
                                            binding.cardNode.setVisibility(View.GONE);
                                            binding.noClass.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                            }
                        };
                        countDownTimer.start();
                        Log.e("countDownStarted place", countDownTimer.toString());
                    }
                } else {
                    binding.cardNode.setVisibility(View.GONE);
                    binding.noClass.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setNotification(long milis,String uid,String name,String dep,String loc,String sub,String topic,String min,String dateTime){
        long deliveryTimeMillis = System.currentTimeMillis() + milis;
        Intent intent = new Intent(requireContext(), NotificationBroadcastReceiver.class);
        intent.putExtra("CurrentUID",uid);
        intent.putExtra("name",name);
        intent.putExtra("dep",dep);
        intent.putExtra("loc",loc);
        intent.putExtra("sub",sub);
        intent.putExtra("topic",topic);
        intent.putExtra("min",min);
        intent.putExtra("dateTime",dateTime);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, deliveryTimeMillis, pendingIntent);
        Log.e("MyApp","NotificationSchedule");
    }
    private void updateCountdownText() {
        int hours = (int) (timeRemainingInMillis / 1000) / 3600;
        int minutes = (int) ((timeRemainingInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeRemainingInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
        binding.countTImer.setText(timeFormatted);
    }
    private void setTextView(String name,String dep,String minute,String loc,String sub,String topi,String start){
        binding.nametxt.setText(name);
        binding.nametxt.setSelected(true);
        binding.durationTxt.setText(minute);
        binding.durationTxt.setSelected(true);
        binding.departText.setText(dep);
        binding.departText.setSelected(true);
        binding.locationTxt.setText(loc);
        binding.locationTxt.setSelected(true);
        binding.subjectTxt.setText(sub);
        binding.subjectTxt.setSelected(true);
        binding.topopicTxt.setText(topi);
        binding.topopicTxt.setSelected(true);
        binding.startedTxt.setText(start);
        binding.startedTxt.setSelected(true);
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

        Log.e("MyApp", "onstart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("MyApp", "onstop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("MyApp", "onresume");
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
