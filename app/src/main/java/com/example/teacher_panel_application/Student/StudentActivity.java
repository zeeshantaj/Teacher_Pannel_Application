package com.example.teacher_panel_application.Student;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.teacher_panel_application.Home.Main_HomeViewPagerAdapter;
import com.example.teacher_panel_application.Network.NetworkCheckReceiver;
import com.example.teacher_panel_application.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class StudentActivity extends AppCompatActivity {

    public ViewPager2 viewPager2;
    private NetworkCheckReceiver networkChangeReceiver;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        viewPager2 = findViewById(R.id.studentViewPager);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewStudent);


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
    private int getNavigationItem(int position) {
        switch (position) {
            case 0:
                Objects.requireNonNull(getSupportActionBar()).setTitle("CM");
                return R.id.studentResources;
            case 1:
                Objects.requireNonNull(getSupportActionBar()).setTitle("Announcement");
                return R.id.studentAnnouncement;
            case 2:
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.profile);
                return R.id.navProfileStudent;
            // Add more cases as needed
        }
        return 0;
    }

    private int getPositionForNavigationItem(int itemId) {
        if (itemId == R.id.studentResources) {
            return 0;
        } else if (itemId == R.id.studentAnnouncement) {
            return 1;
        } else if (itemId == R.id.navProfileStudent) {
            return 2;
        }

        return -1;
    }
}