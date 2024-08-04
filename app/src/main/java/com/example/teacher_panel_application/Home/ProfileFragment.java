package com.example.teacher_panel_application.Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.Login.Login_Activity;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.FragmentEditDataBinding;
import com.example.teacher_panel_application.databinding.ProfileFragmentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ProfileFragment extends BottomSheetDialogFragment {
    private ProfileFragmentBinding binding;
    private UploadTask uploadTask;
    private Uri imageUri;
    private StorageReference imageRef;
    private BottomSheetDialog dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ProfileFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the parent view of the fragment
        View parentView = (View) view.getParent();

        // Set up the BottomSheetBehavior
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(parentView);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        // Set the callback to customize behavior
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // Handle state changes if needed
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Restrict upward movement
                if (slideOffset < 0) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                }
            }
        });

        // Set minimum height to full screen
        CoordinatorLayout coordinatorLayout = view.findViewById(R.id.bottomSheetLayoutProfile);
        coordinatorLayout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginType", Context.MODE_PRIVATE);
        boolean isTrue = sharedPreferences.getBoolean("typeBool",false);
        if (isTrue){
            binding.classCountTxt.setVisibility(View.GONE);
            binding.announceCountTxt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        String uid = auth.getUid();
        String imageName = "image_"+uid+".jpg";
        imageRef = storageReference.child("UserImages/"+imageName);

        binding.userProfilePf.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Profile Picture")
                    .setMessage("Do you want to change profile picture?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        imageLauncher.launch(intent);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();


        });
        binding.userNamePf.setOnClickListener(v -> {
            showCustomSaveDialog();
        });

        getUserInfo();

    }
    private ActivityResultLauncher<Intent> imageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    binding.userProfilePf.setImageURI(imageUri);
                    uploadTask = imageRef.putFile(imageUri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUri = uri.toString();
                                HashMap<String, Object> value = new HashMap<>();
                                value.put("image",imageUri);
                                MethodsUtils.getCurrentUserRef(getReference()).updateChildren(value).addOnSuccessListener(unused -> {

                                    Toast.makeText(getActivity(), "Profile Image Changed", Toast.LENGTH_SHORT).show();

                                }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                                Toast.makeText(getActivity(), "image send to db "+imageUri, Toast.LENGTH_SHORT).show();

                            }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                        }
                    }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                }
            });
    private void getUserInfo(){
        MethodsUtils.getCurrentUserRef(getReference()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue(String.class);
                    String imageUrl = snapshot.child("image").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    Glide.with(getActivity())
                            .load(imageUrl)
                            .into(binding.userProfilePf);
                    binding.userNamePf.setText(name);
                    binding.emailTxt.setText(email);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error "+ error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        MethodsUtils.getCurrentUserRef("PostedData").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int count  = (int) snapshot.getChildrenCount();
                    String CC = String.valueOf(count);
                    String CCstr = String.format("Total Class Taken %s",CC);
                    binding.classCountTxt.setText(CCstr);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        MethodsUtils.getCurrentUserRef("Announcement").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int count  = (int) snapshot.getChildrenCount();
                    String CC = String.valueOf(count);
                    String CCstr = String.format("Total Announcement %s",CC);
                    binding.announceCountTxt.setText(CCstr);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showCustomSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.edit_name_dialogue, null);
        builder.setView(dialogView);

        TextInputEditText editTextFileName = dialogView.findViewById(R.id.editNameEdDialogue);
        TextView buttonCancel = dialogView.findViewById(R.id.edit_name_cancel);
        TextView buttonSave  = dialogView.findViewById(R.id.textView11);

        AlertDialog dialog = builder.create();
        dialog.show();
        buttonCancel.setOnClickListener(v -> {

            dialog.dismiss();
        });
        buttonSave.setOnClickListener(v -> {
            String name = editTextFileName.getText().toString();
            if (name.isEmpty()){
                editTextFileName.setError("Name is empty");
                return;
            }
            HashMap<String, Object> value = new HashMap<>();
            value.put("name",name);
            MethodsUtils.getCurrentUserRef(getReference()).updateChildren(value).addOnSuccessListener(unused -> {

                Toast.makeText(getActivity(), "Name Changes", Toast.LENGTH_SHORT).show();
                getUserInfo();
            }).addOnFailureListener(e -> {
                Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });
            dialog.dismiss();
        });
    }

    private String getReference(){
        String child = "";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginType", Context.MODE_PRIVATE);
        boolean isTrue = sharedPreferences.getBoolean("typeBool",false);
        if (isTrue){
            child = "StudentsInfo";
        }else {
            child = "TeacherInfo";
        }
        return child;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }
}
