package com.example.teacher_panel_application.Login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.FragmentResetPassBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

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

        FirebaseAuth auth = FirebaseAuth.getInstance();
        binding.loginText.setOnClickListener(view -> setFragment(new Login_Fragment()));

        binding.resetBtn.setOnClickListener(v -> {
            String email = binding.resetPassEd.getText().toString();
            binding.resetBtn.setEnabled(false);
            if (email.isEmpty()){
                binding.resetPassEd.setError("Email is empty");
                binding.resetBtn.setEnabled(true);
                return;
            }
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    binding.resetBtn.setEnabled(false);
                    binding.checkIBTxt.setVisibility(View.VISIBLE);
                }
                else {
                    binding.resetBtn.setEnabled(true);
                    Toast.makeText(getActivity(), "Error "+task.getException(), Toast.LENGTH_SHORT).show();
                }
            });

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