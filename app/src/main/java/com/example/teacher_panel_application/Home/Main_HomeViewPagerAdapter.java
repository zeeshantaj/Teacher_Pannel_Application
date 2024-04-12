package com.example.teacher_panel_application.Home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Main_HomeViewPagerAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 4;

    public Main_HomeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new UploadDetails_Fragment();
            case 2:
                return new History_Fragment();
            case 3:
                return new Settings_Fragment();
            case 0:
            default:
                return new Home_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
