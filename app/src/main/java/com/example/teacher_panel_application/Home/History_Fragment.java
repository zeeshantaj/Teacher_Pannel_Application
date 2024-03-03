package com.example.teacher_panel_application.Home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.teacher_panel_application.Adapters.AnnounceAdapter;
import com.example.teacher_panel_application.Adapters.UploadDetailsViewPagerAdapter;
import com.example.teacher_panel_application.Create_Fragments.Notification_Announcement;
import com.example.teacher_panel_application.Create_Fragments.Upload_Class_Data_Fragment;
import com.example.teacher_panel_application.History.ClassHistory_Fragment;
import com.example.teacher_panel_application.History.NotificationHistory_Fragment;
import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.example.teacher_panel_application.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class History_Fragment extends Fragment {

    private ViewPager2 myViewPager;
    private TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_history,container,false);

        myViewPager = view.findViewById(R.id.myViewpagerHistory);
        tabLayout = view.findViewById(R.id.tabLayoutHistory);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Fragment[] pages = {new ClassHistory_Fragment(), new NotificationHistory_Fragment()}; // Replace with your fragment classes
        UploadDetailsViewPagerAdapter myPagerAdapter = new UploadDetailsViewPagerAdapter(getActivity(),pages);
        myViewPager.setAdapter(myPagerAdapter);

// Create an object of page transformer
        CardFlipPageTransformer2 cardFlipPageTransformer = new CardFlipPageTransformer2();

// Enable / Disable scaling while flipping. If false, then card will only flip as in Poker card example.
// Otherwise card will also scale like in Gallery demo. By default, its true.
        cardFlipPageTransformer.setScalable(false);

        myViewPager.setPageTransformer(cardFlipPageTransformer);


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, myViewPager,
                (tab, position) -> {
                    // Customize the tab text and titles based on your needs.
                    if (position == 0) {
                        //myViewPager.setCurrentItem(position,true);
                        tab.setText("Uploaded Classes");
                    } else if (position == 1) {
                        //myViewPager.setCurrentItem(position,true);
                        tab.setText("Announcements");
                    }
                }
        );
        tabLayoutMediator.attach();
    }
}
