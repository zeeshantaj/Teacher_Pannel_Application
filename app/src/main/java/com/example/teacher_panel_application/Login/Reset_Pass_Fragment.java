package com.example.teacher_panel_application.Login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.FragmentResetPassBinding;

public class Reset_Pass_Fragment extends Fragment {



    public Reset_Pass_Fragment() {
        // Required empty public constructor
    }

    private FragmentResetPassBinding binding;
    private FrameLayout parentFrameLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding  = FragmentResetPassBinding.inflate(inflater, container, false);


        //backLogin = view.findViewById(R.id.backLoginText);
        parentFrameLayout = getActivity().findViewById(R.id.loginFrameLayout);

        binding.loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new Login_Fragment());
            }
        });

        return binding.getRoot();
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}