package com.example.teacher_panel_application.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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
import java.util.Objects;

public class Home_Activity extends AppCompatActivity {

    private String homeTitle = "Class Details";
    private String historyTitle = "Uploaded History";
    private String profileTitle = "Profile";
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ViewPager2 viewPager2 = findViewById(R.id.homeViewPager);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.darkBlue));


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.setSelectedItemId(getNavigationItem(position));

            }
        });
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            int pos = getPositionForNavigationItem(itemId);
            if (pos != -1){
                viewPager2.setCurrentItem(pos,true);
                return true;
            }
            return false;
        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
    }

    private int getNavigationItem(int position) {
        switch (position) {
            case 0:
                Objects.requireNonNull(getSupportActionBar()).setTitle(homeTitle);
                return R.id.navHome;
            case 1:
                Objects.requireNonNull(getSupportActionBar()).setTitle(historyTitle);
                return R.id.navHistory;
            case 2:
                Objects.requireNonNull(getSupportActionBar()).setTitle(profileTitle);
                return R.id.navProfile;
            // Add more cases as needed
        }
        return 0;
    }
    private int getPositionForNavigationItem(int itemId) {
        if (itemId == R.id.navHome){
            return 0;
        }else if (itemId == R.id.navHistory){
            return 1;
        }else if (itemId == R.id.navProfile){
            return 2;
        }
        return -1;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.home_menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.announcementData){
//            FragmentManager manager = getSupportFragmentManager();
//            FragmentTransaction transaction = manager.beginTransaction();
//            transaction.replace(android.R.id.content,new Ann_Home_Fragment());
//            transaction.addToBackStack(null);
//            transaction.commit();
//        }
//        return false;
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Home_Fragment homeFragment = new Home_Fragment();
//
//        if (adView != null) {
//            adView.resume();
//        }
//
//        if (countDownTimer == null) {
//            homeFragment.getValues();
//            //startCountdown();
//        }
//        homeFragment.getValues();
////        Log.e("countDownResume",countDownTimer.toString());
//
//    }
//    @Override
//    protected void onPause() {
//        if (adView != null) {
//            adView.pause();
//        }
////        if (countDownTimer!=null){
////            countDownTimer.cancel();
////            Log.e("countDownPause",countDownTimer.toString());
////        }
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//            countDownTimer = null; // Set countDownTimer to null to indicate it was canceled
//        }
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (countDownTimer!=null){
//            countDownTimer.cancel();
//        }
//        if (adView != null) {
//            adView.destroy();
//        }
//    }
}