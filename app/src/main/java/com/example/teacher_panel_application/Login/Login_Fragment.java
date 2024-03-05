package com.example.teacher_panel_application.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teacher_panel_application.Activities.Upload_Details_Activity;
import com.example.teacher_panel_application.Home.Home_Activity;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Fragment extends Fragment {
    public Login_Fragment() {
        // Required empty public constructor
    }
   private FrameLayout parentFrameLayout;
    private FirebaseAuth auth;
    private FragmentLoginBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding  = FragmentLoginBinding.inflate(inflater, container, false);
        parentFrameLayout = getActivity().findViewById(R.id.loginFrameLayout);

        binding.loginBtn.setOnClickListener(view -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            String email = binding.loginEmail.getText().toString();
            String password = binding.loginPass.getText().toString();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign-in success, update UI accordingly
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent intent = new Intent(getActivity(), Home_Activity.class);
                            startActivity(intent);
                            getActivity().finish();
                            // You can now access the signed-in user information
                        } else {
                            // Sign-in failed, display an error message
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        });

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null){
            Intent intent = new Intent(getActivity(), Home_Activity.class);
            startActivity(intent);
            getActivity().finish();

        }



        binding.signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignUp_Fragment());
            }
        });

        binding.txtForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new Reset_Pass_Fragment());
            }
        });

        return binding.getRoot();
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}