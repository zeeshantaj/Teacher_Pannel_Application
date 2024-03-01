package com.example.teacher_panel_application.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.teacher_panel_application.Adapters.UploadDetailsViewPagerAdapter;
import com.example.teacher_panel_application.Create_Fragments.Notification_Announcement;
import com.example.teacher_panel_application.Create_Fragments.Upload_Class_Data_Fragment;
import com.example.teacher_panel_application.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2;

public class UploadDetails_Fragment extends Fragment {
    private DatabaseReference reference1,reference;
    private FirebaseAuth auth;
    private ViewPager2 myViewPager;
    private TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_upload_details,container,false);

        myViewPager = view.findViewById(R.id.myViewpager);




        tabLayout = view.findViewById(R.id.tabLayout);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Fragment[] pages = {new Upload_Class_Data_Fragment(), new Notification_Announcement()}; // Replace with your fragment classes
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
                        myViewPager.setCurrentItem(position,true);
                        tab.setText("Upload Class");
                    } else if (position == 1) {
                        myViewPager.setCurrentItem(position,true);
                        tab.setText("Announcements");
                    }
                }
        );
        tabLayoutMediator.attach();
        myViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // The 'position' parameter indicates the currently selected page.
                // You can use it to identify the currently displayed fragment.
                //((Home_Activity)getActivity()).getActivityViewPager().setCurrentItem(position, true);
                Toast.makeText(getContext(), "Child viewpger ", Toast.LENGTH_SHORT).show();
                if (position == 0) {

                //    myViewPager.setCurrentItem(position,true);
                    Toast.makeText(getContext(), "pos"+position, Toast.LENGTH_SHORT).show();
                    //Home_Activity.viewPager2.setCurrentItem(position,true);
//                    getSupportActionBar().setTitle("Upload Class Here");
//                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.darkBlue));
                } else if (position == 1) {
                  //  myViewPager.setCurrentItem(position,true);

                    Toast.makeText(getContext(), "pos"+position, Toast.LENGTH_SHORT).show();
                    //Home_Activity.viewPager2.setCurrentItem(position,true);
//                    getSupportActionBar().setTitle("Upload Announcement Here");
//                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.darkBlue));
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
