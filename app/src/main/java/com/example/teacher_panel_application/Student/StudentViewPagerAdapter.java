package com.example.teacher_panel_application.Student;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.teacher_panel_application.Home.History_Fragment;
import com.example.teacher_panel_application.Home.Settings_Fragment;
import com.example.teacher_panel_application.Student.fragments.StudentHome;
import com.example.teacher_panel_application.Student.fragments.Student_PDF_Fragment;

public class StudentViewPagerAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 4;
    public StudentViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new Student_PDF_Fragment();
            case 2:
                return new Settings_Fragment();
//            case 3:
//                return new Settings_Fragment();
            case 0:
            default:
                return new StudentHome();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
