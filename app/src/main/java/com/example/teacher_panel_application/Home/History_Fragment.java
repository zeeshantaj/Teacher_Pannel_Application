package com.example.teacher_panel_application.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teacher_panel_application.Adapters.UploadDetailsViewPagerAdapter;
import com.example.teacher_panel_application.History.ClassHis.ClassHistory_Fragment;
import com.example.teacher_panel_application.History.Announcement.AnnouncementHistory_Fragment;
import com.example.teacher_panel_application.History.StudyMaterial.View_Study_Material;
import com.example.teacher_panel_application.databinding.FragmentHistoryBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2;

public class History_Fragment extends Fragment {
    private FragmentHistoryBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding  =  FragmentHistoryBinding.inflate(inflater,container,false);;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Fragment[] pages = {new ClassHistory_Fragment(), new AnnouncementHistory_Fragment(),new View_Study_Material()}; // Replace with your fragment classes
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
                        tab.setText("Uploaded Class");
                    } else if (position == 1) {
                        //myViewPager.setCurrentItem(position,true);
                        tab.setText("Announcements");
                    }
                    else if (position == 2) {
                        //myViewPager.setCurrentItem(position,true);
                        tab.setText("PDF History");
                    }
                }
        );
        tabLayoutMediator.attach();
    }
    @Override
    public void onResume() {
        super.onResume();

        Log.e("MyApp","onresume history");
    }
}
