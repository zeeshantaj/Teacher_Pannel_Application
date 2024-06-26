package com.example.teacher_panel_application.Login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.teacher_panel_application.Home.Home_Activity;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Student.StudentActivity;
import com.example.teacher_panel_application.Utils.FragmentUtils;
import com.example.teacher_panel_application.databinding.FragmentSignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

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
    private UploadTask uploadTask;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference,imageRef;
    private String downloadedImageUri;
    private DatabaseReference databaseReference;

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


        databaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        String imageUid = auth.getUid();

        String imageName = "image_"+imageUid+".jpg";

        imageRef = storageReference.child("UserImages/"+imageName);

        binding.loginText.setOnClickListener(view12 ->
                FragmentUtils.SetFragment(getActivity().getSupportFragmentManager(),new Login_Fragment(),parentFrameLayout.getId()));

        binding.userProfileSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imageLauncher.launch(intent);
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginType", Context.MODE_PRIVATE);
        boolean isTrue = sharedPreferences.getBoolean("typeBool",false);
        if (isTrue){
            binding.signUpContainer.setVisibility(View.VISIBLE);
            binding.studentId.setVisibility(View.VISIBLE);
            binding.studentCurrentYear.setVisibility(View.VISIBLE);
            binding.studentCurrentSemester.setVisibility(View.VISIBLE);
            binding.studentCurrentMajor.setVisibility(View.VISIBLE);

            binding.singUpBtn.setOnClickListener(view1 -> {
                String name = binding.SignUpName.getText().toString();
                String email = binding.SignUpEmail.getText().toString();
                String pass = binding.SignUpPass.getText().toString();
                String conPass = binding.SignUpConPass.getText().toString();
                String id = binding.studentId.getText().toString();
                String year = binding.studentCurrentYear.getText().toString();
                String semester = binding.studentCurrentSemester.getText().toString();
                String major = binding.studentCurrentMajor.getText().toString();

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
                    Toast.makeText(getActivity(), "Password and Confirm Password should be same", Toast.LENGTH_SHORT).show();
                }
                if (id.isEmpty()) {
                    //ConPass.setError("confirm password field is empty");
                    Toast.makeText(getActivity(), "student id field is empty", Toast.LENGTH_SHORT).show();
                }if (year.isEmpty()) {
                    //ConPass.setError("confirm password field is empty");
                    Toast.makeText(getActivity(), "student current year field is empty", Toast.LENGTH_SHORT).show();
                }if (semester.isEmpty()) {
                    //ConPass.setError("confirm password field is empty");
                    Toast.makeText(getActivity(), "student current semester field is empty", Toast.LENGTH_SHORT).show();
                }if (major.isEmpty()) {
                    //ConPass.setError("confirm password field is empty");
                    Toast.makeText(getActivity(), "student major field is empty", Toast.LENGTH_SHORT).show();
                }


                if (!name.isEmpty() && !email.isEmpty()
                        && !pass.isEmpty() && !conPass.isEmpty() && !id.isEmpty() && !year.isEmpty() && !semester.isEmpty() && !major.isEmpty()) {
                    dialog.setTitle("Please Wait");
                    dialog.setMessage("Creating User....");
                    dialog.setCancelable(false);
                    dialog.show();
                    uploadTask.addOnSuccessListener(taskSnapshot -> {

                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                            downloadedImageUri = uri.toString();


                        }).addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("MyApp", e.getLocalizedMessage());

                        });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("MyApp", e.getLocalizedMessage());

                    });

                    auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(authResult -> {

                        HashMap<String,String> value = new HashMap<>();
                        value.put("image",downloadedImageUri);
                        value.put("name",name);
                        value.put("email",email);
                        value.put("studentId",id);
                        value.put("studentYear",year);
                        value.put("studentSemester",semester);
                        value.put("studentMajor",major);
                        String uid = auth.getUid();
                        databaseReference.child("StudentsInfo").child(Objects.requireNonNull(uid)).setValue(value)

                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(getActivity(), "Account Created!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), StudentActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                    dialog.dismiss();
                                }).addOnFailureListener(e -> {
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("MyApp", Objects.requireNonNull(e.getLocalizedMessage()));
                                });


                    }).addOnFailureListener(e -> {

                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("MyApp", Objects.requireNonNull(e.getLocalizedMessage()));
                        dialog.dismiss();
                    });

                }

            });


        }
        else {
            binding.signUpContainer.setVisibility(View.VISIBLE);
            binding.studentId.setVisibility(View.GONE);
            binding.studentCurrentYear.setVisibility(View.GONE);
            binding.studentCurrentSemester.setVisibility(View.GONE);
            binding.studentCurrentMajor.setVisibility(View.GONE);

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
                    Toast.makeText(getActivity(), "Password and Confirm Password should be same", Toast.LENGTH_SHORT).show();
                }


                if (!name.isEmpty() && !email.isEmpty()
                        && !pass.isEmpty() && !conPass.isEmpty()) {
                    dialog.setTitle("Please Wait");
                    dialog.setMessage("Creating User....");
                    dialog.setCancelable(false);
                    dialog.show();
                    uploadTask.addOnSuccessListener(taskSnapshot -> {

                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                            downloadedImageUri = uri.toString();


                        }).addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("MyApp", e.getLocalizedMessage());

                        });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("MyApp", e.getLocalizedMessage());

                    });

                    auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(authResult -> {

                        HashMap<String,String> value = new HashMap<>();
                        value.put("image",downloadedImageUri);
                        value.put("name",name);
                        value.put("email",email);
                        String uid = auth.getUid();
                        databaseReference.child("TeacherInfo").child(Objects.requireNonNull(uid)).setValue(value)

                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(getActivity(), "Account Created!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), Home_Activity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                    dialog.dismiss();
                                }).addOnFailureListener(e -> {
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("MyApp", Objects.requireNonNull(e.getLocalizedMessage()));
                                });


                    }).addOnFailureListener(e -> {

                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("MyApp", Objects.requireNonNull(e.getLocalizedMessage()));
                        dialog.dismiss();
                    });

                }

            });

        }
    }

    private ActivityResultLauncher<Intent> imageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    binding.userProfileSignUp.setImageURI(imageUri);
                    uploadTask = imageRef.putFile(imageUri);

                }
            });
}