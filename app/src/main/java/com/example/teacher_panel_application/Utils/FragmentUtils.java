package com.example.teacher_panel_application.Utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtils {
    public static void SetFragment(FragmentManager fragmentManager,Fragment fragment,int ContainerID){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(ContainerID,fragment);
        fragmentTransaction.commit();
    }
}
