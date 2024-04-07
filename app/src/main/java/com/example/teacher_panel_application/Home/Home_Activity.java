package com.example.teacher_panel_application.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.teacher_panel_application.Network.InternetAccessCallBack;
import com.example.teacher_panel_application.Network.NetworkUtils;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Objects;

public class Home_Activity extends AppCompatActivity {
    public ViewPager2 viewPager2;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager2 = findViewById(R.id.homeViewPager);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.darkBlue));


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.setSelectedItemId(getNavigationItem(position));

                viewPager2.setCurrentItem(position,true);
            }
        });
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            int pos = getPositionForNavigationItem(itemId);
            if (pos != -1) {
                viewPager2.setCurrentItem(pos, true);
                return true;
            }
            return false;
        });
        Main_HomeViewPagerAdapter viewPagerAdapter = new Main_HomeViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        checkInternet();
    }
    private void checkInternet(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                // Device's internet is turned on
                Toast.makeText(Home_Activity.this, "Device's internet is turned on", Toast.LENGTH_SHORT).show();
                NetworkUtils.hasInternetAccess(new InternetAccessCallBack() {
                    @Override
                    public void onInternetAccessResult(boolean hasInternetAccess) {
                        if (hasInternetAccess){
                            Toast.makeText(Home_Activity.this, "Internet has service", Toast.LENGTH_SHORT).show();

                        }else {
                            Snackbar.make(findViewById(android.R.id.content), "Internet don't have service", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Check Again", view -> {
                                        // Retry checking internet connectivity
                                        checkInternet();
                                    }).show();
                        }
                    }
                });

            } else {
                // Device's internet is turned off
                Toast.makeText(Home_Activity.this, "Device's internet is turned off", Toast.LENGTH_SHORT).show();
                // Show dialog to prompt user to turn on internet
                MethodsUtils.showAlertDialogue(this);
            }
        } else {
            // Error checking internet connection
            Toast.makeText(Home_Activity.this, "Error checking internet connection", Toast.LENGTH_SHORT).show();
        }
    }
    private int getNavigationItem(int position) {
        switch (position) {
            case 0:
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.classDetails);
                return R.id.navHome;
            case 1:
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.postDetails);
                return R.id.navUploadDetails;
            case 2:
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.uploadedHistory);
                return R.id.navHistory;
            case 3:
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.profile);
                return R.id.navProfile;

            // Add more cases as needed
        }
        return 0;
    }

    private int getPositionForNavigationItem(int itemId) {
        if (itemId == R.id.navHome) {
            return 0;
        } else if (itemId == R.id.navUploadDetails) {
            return 1;
        } else if (itemId == R.id.navHistory) {
            return 2;
        } else if (itemId == R.id.navProfile) {
            return 3;
        }

            return -1;
        }
}