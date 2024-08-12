package com.example.teacher_panel_application.Login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.teacher_panel_application.Home.Home_Activity;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Student.StudentActivity;
import com.example.teacher_panel_application.Utils.FragmentUtils;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.FragmentSignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                    getActivity(),
                    R.array.classYearArray,
                    android.R.layout.simple_spinner_item);

            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.studentCurrentYear.setAdapter(adapter1);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    getActivity(),
                    R.array.programming_languages,
                    android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.studentCurrentSemester.setAdapter(adapter);

            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                    getActivity(),
                    R.array.major_array,
                    android.R.layout.simple_spinner_item);

            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.studentCurrentMajor.setAdapter(adapter2);


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

                int selectedYearPosition = binding.studentCurrentYear.getSelectedItemPosition();
                int selectedSemesterPosition = binding.studentCurrentSemester.getSelectedItemPosition();
                int selectedPurposePosition = binding.studentCurrentMajor.getSelectedItemPosition();




                if (email.isEmpty() && !email.matches(EMAIL_PATTERN)) {
                    binding.SignUpEmail.setError("Email should be in correct pattern and can not be empty");
                    return;
                }
                if (name.isEmpty()) {
                    binding.SignUpName.setError("Name is Empty");
                    return;
                }
                if (pass.isEmpty()) {
                    //Pass.setError("password field is empty");
                    Toast.makeText(getActivity(), "password field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (conPass.isEmpty()) {
                    //ConPass.setError("confirm password field is empty");
                    Toast.makeText(getActivity(), "confirm password field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass.equals(conPass)) {
                    Toast.makeText(getActivity(), "Password and Confirm Password should be same", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (id.isEmpty()) {
                    //ConPass.setError("confirm password field is empty");
                    Toast.makeText(getActivity(), "student id field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedYearPosition <= 0) {
                    Toast.makeText(getActivity(), "Please Select Year", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedSemesterPosition <= 0) {
                    Toast.makeText(getActivity(), "Please Select Semester", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedPurposePosition <= 0) {
                    Toast.makeText(getActivity(), "Please Select Major", Toast.LENGTH_SHORT).show();
                    return;
                }

                String year = binding.studentCurrentYear.getSelectedItem().toString();
                String semester  = binding.studentCurrentSemester.getSelectedItem().toString();
                String major = binding.studentCurrentMajor.getSelectedItem().toString();

                    dialog.setTitle("Please Wait");
                    dialog.setMessage("Creating User....");
                    dialog.setCancelable(false);
                    dialog.show();

                    downloadedImageUri = "";

                    if (imageUri != null){

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
                    }


                    auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(authResult -> {

                        HashMap<String,String> value = new HashMap<>();
                        value.put("image",downloadedImageUri);
                        value.put("name",name);
                        value.put("email",email);
                        value.put("studentId",id);
                        value.put("studentYear",year);
                        value.put("studentSemester",semester);
                        value.put("studentMajor",major);

                        MethodsUtils.setCurrentUserYearAndSemester(getActivity(),year,semester);

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

                    downloadedImageUri = "";
                    if (imageUri != null){
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
                    }


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
//    private void sendNotification(String message) {
//        MethodsUtils.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//                    UserModel currentUser = task.getResult().toObject(UserModel.class);
//                    try {
//                        JSONObject jsonObject = new JSONObject();
//                        JSONObject notificationObject = new JSONObject();
//                        notificationObject.put("title",currentUser.getName());
//                        notificationObject.put("body",message);
//
//                        JSONObject dataObj = new JSONObject();
//                        dataObj.put("userId",currentUser.getAssociatedId());
//
//                        jsonObject.put("notification",notificationObject);
//                        jsonObject.put("data",dataObj);
//                        jsonObject.put("to",receiverFCMToken);
//                        callApi(jsonObject);
//
//                    }catch (Exception e){
//                        e.getLocalizedMessage();
//                    }
//                }
//                else {
//                    Log.e("MyApp","FCM ERROR "+task.getException());
//                }
//            }
//        });
//
//    }

    private ActivityResultLauncher<Intent> imageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    binding.userProfileSignUp.setImageURI(imageUri);
                    uploadTask = imageRef.putFile(imageUri);
                }
            });
}