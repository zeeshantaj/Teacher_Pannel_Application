package com.example.teacher_panel_application.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.teacher_panel_application.Create_Fragments.Notification_Announcement;
import com.example.teacher_panel_application.Create_Fragments.Upload_Class_Data_Fragment;
import com.example.teacher_panel_application.Home.History_Fragment;
import com.example.teacher_panel_application.Home.Home_Fragment;
import com.example.teacher_panel_application.Home.Profile_Fragment;
import com.example.teacher_panel_application.Home.UploadDetails_Fragment;

public class UploadDetailsViewPagerAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 2;

    public UploadDetailsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Upload_Class_Data_Fragment();
            case 1:
                return new Notification_Announcement();
            default:
                return new Home_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
    // Define an array of Fragment objects to hold your pages
//        private Fragment[] pages;
//
//        public UploadDetailsViewPagerAdapter(FragmentActivity fragmentActivity, Fragment[] pages) {
//            super(fragmentActivity);
//            this.pages = pages;
//        }
//
//        @Override
//        public Fragment createFragment(int position) {
//            // Return the fragment for the given page
//            return pages[position];
//        }
//
//        @Override
//        public int getItemCount() {
//            // Return the number of pages
//            return pages.length;
//        }
}
