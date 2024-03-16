package com.example.teacher_panel_application.Home;

import static com.google.common.reflect.Reflection.getPackageName;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.teacher_panel_application.EditDataFragments.EditDataFragment;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.Notification.NotificationBroadcastReceiver;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.HomeFragmentBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Home_Fragment extends Fragment {

    HomeFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private CountDownTimer countDownTimer;
    private long timeRemainingInMillis, timeDifferenceMillis;
    private String name, dep, loc, sub, topi, minute, start, dateTime, endTimeString;

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

// To Initialize Timer
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
                    name = snapshot.child("name").getValue(String.class);
                    dep = snapshot.child("department").getValue(String.class);
                    loc = snapshot.child("location").getValue(String.class);
                    sub = snapshot.child("subject").getValue(String.class);
                    topi = snapshot.child("topic").getValue(String.class);
                    minute = snapshot.child("minutes").getValue(String.class);
                    start = snapshot.child("currentTime").getValue(String.class);
                    dateTime = snapshot.child("dateTime").getValue(String.class);
                    String endDateTime = snapshot.child("endDateTime").getValue(String.class);
                   // String currentDateTime = snapshot.child("currentDateTime").getValue(String.class);

                    Log.e("MyApp","min"+minute);
                    Log.e("MyApp","endDateTime"+endDateTime);
                    //Log.e("MyApp","currentDateTime"+currentDateTime);

                    setTextView(name, dep, minute, loc, sub, topi, start);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd:hh:mm:ss:a");
                        //LocalDateTime startTime = LocalDateTime.parse(currentDateTime, formatter);
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        String currentTime = currentDateTime.format(formatter);
                        LocalDateTime startTime = LocalDateTime.parse(currentTime, formatter);
                        LocalDateTime endTime = LocalDateTime.parse(endDateTime, formatter);
                        timeDifferenceMillis = Duration.between(startTime, endTime).toMillis();

                    }


                    Log.e("MyApp","millis "+timeDifferenceMillis);
//                    try {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            LocalDateTime localDateTime = LocalDateTime.now();
//                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
//                            String formattedTime = localDateTime.format(formatter);
//                            System.out.println(formattedTime);
//                            SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
//                            Date currentTime = inputFormat.parse(formattedTime);
//                            Date endTime = inputFormat.parse(endTimeString);
//                            timeDifferenceMillis = endTime.getTime() - currentTime.getTime();
//                            Log.d("MyApp", String.valueOf(timeDifferenceMillis));
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    if (countDownTimer == null) {

                        setNotification(timeDifferenceMillis, uid, name, dep, loc, sub, topi, minute, dateTime);
                        countDownTimer = new CountDownTimer(timeDifferenceMillis, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                timeRemainingInMillis = millisUntilFinished;
                                updateCountdownText();
                                if (millisUntilFinished < 50000) {
                                    binding.countTImer.setTextColor(getResources().getColor(R.color.red));
                                }

                                long elapsedTime = timeDifferenceMillis - millisUntilFinished;
                                int progress = (int) (elapsedTime * 100 / timeDifferenceMillis);

                                binding.progressBarCircle.setProgress(100 - progress);

                            }

                            @Override
                            public void onFinish() {
                                // Perform any actions after the countdown finishes
                                binding.progressBarCircle.setProgress(0);
                                UploadClassModel uploadClassModel = new UploadClassModel(name, dep, loc, sub, topi, minute, dateTime);
                                DatabaseReference addToQueueReference = FirebaseDatabase.getInstance().getReference("PostedData").child(uid).child(dateTime);
                                addToQueueReference.setValue(uploadClassModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                          //  reference.removeValue();
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

    private void setNotification(long milis, String uid, String name, String dep, String loc, String sub, String topic, String min, String dateTime) {
        long deliveryTimeMillis = System.currentTimeMillis() + milis;
        Intent intent = new Intent(requireContext(), NotificationBroadcastReceiver.class);
        intent.putExtra("CurrentUID", uid);
        intent.putExtra("name", name);
        intent.putExtra("dep", dep);
        intent.putExtra("loc", loc);
        intent.putExtra("sub", sub);
        intent.putExtra("topic", topic);
        intent.putExtra("min", min);
        intent.putExtra("dateTime", dateTime);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, deliveryTimeMillis, pendingIntent);
        Log.e("MyApp", "NotificationSchedule");
    }

    private void updateCountdownText() {
        int hours = (int) (timeRemainingInMillis / 1000) / 3600;
        int minutes = (int) ((timeRemainingInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeRemainingInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
        binding.countTImer.setText(timeFormatted);
    }

    private void setTextView(String name, String dep, String minute, String loc, String sub, String topi, String start) {
        binding.nametxt.setText(name);
        binding.nametxt.setSelected(true);

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

        UploadClassModel model = new UploadClassModel();

        binding.durationTxt.setText(model.minutesBuilder(minute));
        binding.durationTxt.setSelected(true);

        binding.textView9.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Class Details can be seen on LED TV!", Toast.LENGTH_SHORT).show();
        });

    }

    private void showEditDataAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.share_and_edit_dialugue, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView buttonCancel = dialogView.findViewById(R.id.cancelBtn);
        MaterialButton editData = dialogView.findViewById(R.id.editDataBtn);
        MaterialButton share = dialogView.findViewById(R.id.shareOnWhatsapp);
        editData.setOnClickListener(v -> {
            dialog.dismiss();
            openEditDataFragment();

        });
        share.setOnClickListener(v -> {
            MethodsUtils.shareOnWhatsapp(getActivity(),
                    name, loc, minute, dep, sub, topi, start);

        });
        buttonCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
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
