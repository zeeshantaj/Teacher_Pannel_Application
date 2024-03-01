package com.example.teacher_panel_application.Activities;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.teacher_panel_application.Adapters.UploadDetailsViewPagerAdapter;
import com.example.teacher_panel_application.Create_Fragments.Notification_Announcement;
import com.example.teacher_panel_application.Create_Fragments.Upload_Class_Data_Fragment;
import com.example.teacher_panel_application.Home.Home_Activity;
import com.example.teacher_panel_application.Models.NetworkUtils;
import com.example.teacher_panel_application.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Upload_Details_Activity extends AppCompatActivity {

    private DatabaseReference reference1,reference;
    private FirebaseAuth auth;
    private ViewPager2 myViewPager;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_details);


        myViewPager = findViewById(R.id.myViewpager);
        Fragment[] pages = {new Upload_Class_Data_Fragment(), new Notification_Announcement()}; // Replace with your fragment classes

        //UploadDetailsViewPagerAdapter myPagerAdapter = new UploadDetailsViewPagerAdapter(this,pages);
        //myViewPager.setAdapter(myPagerAdapter);


// Create an object of page transformer
        CardFlipPageTransformer2 cardFlipPageTransformer = new CardFlipPageTransformer2();

// Enable / Disable scaling while flipping. If false, then card will only flip as in Poker card example.
// Otherwise card will also scale like in Gallery demo. By default, its true.
        cardFlipPageTransformer.setScalable(false);

        myViewPager.setPageTransformer(cardFlipPageTransformer);


        myViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // The 'position' parameter indicates the currently selected page.
                // You can use it to identify the currently displayed fragment.

                if (position == 0) {
                    getSupportActionBar().setTitle("Upload Class Here");
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.darkBlue));
                } else if (position == 1) {
                    getSupportActionBar().setTitle("Upload Announcement Here");
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.darkBlue));
                }
            }
        });
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, myViewPager,
                (tab, position) -> {
                    // Customize the tab text and titles based on your needs.
                    if (position == 0) {
                        tab.setText("Upload Class");
                    } else if (position == 1) {
                        tab.setText("Announcements");
                    }
                }
        );
        tabLayoutMediator.attach();
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
                                reference  =  FirebaseDatabase.getInstance().getReference("Teacher_Data").child(uid);
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
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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