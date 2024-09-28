package com.example.teacher_panel_application.Home;

import static com.google.common.reflect.Reflection.getPackageName;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

import com.example.teacher_panel_application.Adapters.UploadDetailsViewPagerAdapter;
import com.example.teacher_panel_application.EditDataFragments.EditDataFragment;
import com.example.teacher_panel_application.Fragment.QA_Fragment;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.Notification.NotificationBroadcastReceiver;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Student.fragments.Announcement_Notification_Fragment;
import com.example.teacher_panel_application.Student.fragments.Poll_Fragment;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.HomeFragmentBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Home_Fragment extends Fragment {

    HomeFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Fragment[] pages = { new ClassScheduleFragment(),new QA_Fragment()}; // Replace with your fragment classes
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
                        tab.setText("Schedule Class");
                    } else if (position == 1) {
                        //myViewPager.setCurrentItem(position,true);
                        tab.setText("Q&A");
                    }
                }
        );
        tabLayoutMediator.attach();
    }

}
