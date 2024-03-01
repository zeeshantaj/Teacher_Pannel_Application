package com.example.teacher_panel_application.Home;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.teacher_panel_application.Activities.Upload_Details_Activity;
import com.example.teacher_panel_application.Adapters.MyPagerAdapter;
import com.example.teacher_panel_application.Create_Fragments.Notification_Announcement;
import com.example.teacher_panel_application.Create_Fragments.Upload_Class_Data_Fragment;
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

public class UploadDetails_Fragment extends Fragment {
    private DatabaseReference reference1,reference;
    private FirebaseAuth auth;
    private ViewPager2 myViewPager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_upload_details,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myViewPager = view.findViewById(R.id.myViewpager);
        Fragment[] pages = {new Upload_Class_Data_Fragment(), new Notification_Announcement()}; // Replace with your fragment classes

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getActivity(),pages);
        myViewPager.setAdapter(myPagerAdapter);


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
//                    getSupportActionBar().setTitle("Upload Class Here");
//                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.darkBlue));
                } else if (position == 1) {
//                    getSupportActionBar().setTitle("Upload Announcement Here");
//                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.darkBlue));
                }
            }
        });
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
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


}
