package com.example.teacher_panel_application.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.teacher_panel_application.Adapters.UploadDetailsViewPagerAdapter;
import com.example.teacher_panel_application.InsertData_Fragments.Upload_Announcement_Fragment;
import com.example.teacher_panel_application.InsertData_Fragments.Upload_Class_Data_Fragment;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.FragmentUploadBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2;

public class UploadDetails_Fragment extends Fragment {
    private FragmentUploadBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding  =  FragmentUploadBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Fragment[] pages = {new Upload_Class_Data_Fragment(), new Upload_Announcement_Fragment()}; // Replace with your fragment classes
        UploadDetailsViewPagerAdapter myPagerAdapter = new UploadDetailsViewPagerAdapter(getActivity(),pages);
        binding.myViewpager.setAdapter(myPagerAdapter);

// Create an object of page transformer
        CardFlipPageTransformer2 cardFlipPageTransformer = new CardFlipPageTransformer2();

// Enable / Disable scaling while flipping. If false, then card will only flip as in Poker card example.
// Otherwise card will also scale like in Gallery demo. By default, its true.
        cardFlipPageTransformer.setScalable(false);

        binding.myViewpager.setPageTransformer(cardFlipPageTransformer);


        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, binding.myViewpager,
                (tab, position) -> {
                    // Customize the tab text and titles based on your needs.
                    if (position == 0) {
                        //myViewPager.setCurrentItem(position,true);
                        tab.setText("Upload Class");
                    } else if (position == 1) {
                        //myViewPager.setCurrentItem(position,true);
                        tab.setText("Announcements");
                    }
                }
        );
        tabLayoutMediator.attach();
    }
}
