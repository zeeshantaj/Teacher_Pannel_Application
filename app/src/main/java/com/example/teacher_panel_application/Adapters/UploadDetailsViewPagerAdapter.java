package com.example.teacher_panel_application.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class UploadDetailsViewPagerAdapter extends FragmentStateAdapter {
    // Define an array of Fragment objects to hold your pages
        private Fragment[] pages;

        public UploadDetailsViewPagerAdapter(FragmentActivity fragmentActivity, Fragment[] pages) {
            super(fragmentActivity);
            this.pages = pages;
        }

        @Override
        public Fragment createFragment(int position) {
            // Return the fragment for the given page
            return pages[position];
        }

        @Override
        public int getItemCount() {
            // Return the number of pages
            return pages.length;
        }
}
