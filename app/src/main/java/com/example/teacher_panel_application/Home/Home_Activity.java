package com.example.teacher_panel_application.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import com.example.teacher_panel_application.Network.NetworkCheckReceiver;
import com.example.teacher_panel_application.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class Home_Activity extends AppCompatActivity {
    public ViewPager2 viewPager2;
    private NetworkCheckReceiver networkChangeReceiver;
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


        networkChangeReceiver = new NetworkCheckReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(networkChangeReceiver);
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