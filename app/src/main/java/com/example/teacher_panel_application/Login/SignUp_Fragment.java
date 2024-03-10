package com.example.teacher_panel_application.Login;

import android.app.Activity;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teacher_panel_application.Activities.Upload_Details_Activity;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.FragmentSignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.net.URI;

public class SignUp_Fragment extends Fragment {


    public SignUp_Fragment() {
        // Required empty public constructor
    }

    private FrameLayout parentFrameLayout;
    private FirebaseAuth auth;
    private ProgressDialog dialog;
    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private FragmentSignUpBinding binding;
    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false);

        dialog = new ProgressDialog(getActivity());
        parentFrameLayout = getActivity().findViewById(R.id.loginFrameLayout);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();


        binding.loginText.setOnClickListener(view12 ->
                setFragment(new Login_Fragment()));

        binding.userProfileSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imageLauncher.launch(intent);
        });
        binding.singUpBtn.setOnClickListener(view1 -> {
            String name = binding.SignUpName.getText().toString();
            String email = binding.SignUpEmail.getText().toString();
            String pass = binding.SignUpPass.getText().toString();
            String conPass = binding.SignUpConPass.getText().toString();

            if (email.isEmpty() && !email.matches(EMAIL_PATTERN)) {
                binding.SignUpEmail.setError("Email should be in correct pattern and can not be empty");
            }
            if (name.isEmpty()) {
                binding.SignUpName.setError("Name is Empty");


            }
            if (pass.isEmpty()) {
                //Pass.setError("password field is empty");
                Toast.makeText(getActivity(), "password field is empty", Toast.LENGTH_SHORT).show();
            }
            if (conPass.isEmpty()) {
                //ConPass.setError("confirm password field is empty");
                Toast.makeText(getActivity(), "confirm password field is empty", Toast.LENGTH_SHORT).show();
            }


            if (!pass.equals(conPass)) {
                Toast.makeText(getActivity(), "Password should be equal to confirm password", Toast.LENGTH_SHORT).show();
            }


            if (!name.isEmpty() && !email.isEmpty()
                    && !pass.isEmpty() && !conPass.isEmpty()) {
                dialog.setTitle("Please Wait");
                dialog.setMessage("Creating User....");
                dialog.setCancelable(false);
                dialog.show();
                auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getActivity(), "User Created", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), Upload_Details_Activity.class);
                        startActivity(intent);
                        getActivity().finish();
                        dialog.dismiss();
                    }
                }).addOnFailureListener(e -> {

                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("MyApp", e.getLocalizedMessage());
                    dialog.dismiss();
                });

            }

        });
    }

    private ActivityResultLauncher<Intent> imageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    binding.userProfileSignUp.setImageURI(imageUri);

                }
            });

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}