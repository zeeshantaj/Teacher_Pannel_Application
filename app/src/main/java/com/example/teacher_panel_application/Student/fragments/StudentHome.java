package com.example.teacher_panel_application.Student.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.teacher_panel_application.Adapters.UploadDetailsViewPagerAdapter;
import com.example.teacher_panel_application.History.Announcement.AnnounceAdapter;
import com.example.teacher_panel_application.History.Announcement.AnnouncementHistory_Fragment;
import com.example.teacher_panel_application.History.ClassHis.ClassHistory_Fragment;
import com.example.teacher_panel_application.History.StudyMaterial.View_Study_Material;
import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Student.Adapter.PollAdapter;
import com.example.teacher_panel_application.Student.PollModel;
import com.example.teacher_panel_application.databinding.FragmentStudentHomeBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentHome extends Fragment {
    public StudentHome() {
        // Required empty public constructor
    }
    private FragmentStudentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentHomeBinding.inflate(inflater,container,false);



        Fragment[] pages = { new Announcement_Notification_Fragment(),new Poll_Fragment()}; // Replace with your fragment classes
        UploadDetailsViewPagerAdapter myPagerAdapter = new UploadDetailsViewPagerAdapter(getActivity(),pages);
        binding.myViewpagerHistory.setAdapter(myPagerAdapter);

// Create an object of page transformer
        CardFlipPageTransformer2 cardFlipPageTransformer = new CardFlipPageTransformer2();

// Enable / Disable scaling while flipping. If false, then card will only flip as in Poker card example.
// Otherwise card will also scale like in Gallery demo. By default, its true.
        cardFlipPageTransformer.setScalable(false);

        binding.myViewpagerHistory.setPageTransformer(cardFlipPageTransformer);

        binding.tabLayoutHistory.setTabGravity(TabLayout.GRAVITY_FILL);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabLayoutHistory, binding.myViewpagerHistory,
                (tab, position) -> {
                    // Customize the tab text and titles based on your needs.
                    if (position == 0) {
                        //myViewPager.setCurrentItem(position,true);
                        tab.setText("Announcements");
                    } else if (position == 1) {
                        //myViewPager.setCurrentItem(position,true);
                        tab.setText("Poll");
                    }
                }
        );
        tabLayoutMediator.attach();
        return binding.getRoot();


    }
}