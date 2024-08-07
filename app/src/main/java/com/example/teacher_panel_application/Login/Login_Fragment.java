package com.example.teacher_panel_application.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.teacher_panel_application.Home.Home_Activity;
import com.example.teacher_panel_application.Intro.IntroFragment;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.FragmentUtils;
import com.example.teacher_panel_application.Utils.ProgressHelper;
import com.example.teacher_panel_application.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Fragment extends Fragment {
    public Login_Fragment() {
        // Required empty public constructor
    }

    private FrameLayout parentFrameLayout;
    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        parentFrameLayout = getActivity().findViewById(R.id.loginFrameLayout);


        binding.loginPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.textInputLayout2.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.loginBtn.setOnClickListener(view -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            String email = binding.loginEmail.getText().toString();
            String password = binding.loginPass.getText().toString();

            if (email.isEmpty()) {
                binding.loginEmail.setError("Email is empty");
                return;
            }
            if (password.isEmpty()) {
                binding.textInputLayout2.setError("Password is empty");
                return;
            }

            //ProgressHelper.showDialog(getActivity(),"Login...");
            ProgressHelper.showDialog(getActivity(),"Login","Please Wait...");
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign-in success, update UI accordingly
                            Intent intent = new Intent(getActivity(), Home_Activity.class);
                            startActivity(intent);
                            getActivity().finish();
                            ProgressHelper.dismissDialog();

                            // You can now access the signed-in user information
                        } else {
                            // Sign-in failed, display an error message
                            Toast.makeText(getActivity(), "Authentication failed.\n"+task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            ProgressHelper.dismissDialog();
                        }
                    });

        });
        binding.signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtils.SetFragment(getActivity().getSupportFragmentManager(),new SignUp_Fragment(),parentFrameLayout.getId());
            }
        });

        binding.txtForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtils.SetFragment(getActivity().getSupportFragmentManager(),new Reset_Pass_Fragment(),parentFrameLayout.getId());
            }
        });

        return binding.getRoot();
    }
}