package com.example.teacher_panel_application.Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.Login.Login_Activity;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.ProfileFragmentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private DatabaseReference reference;
    private UploadTask uploadTask;
    private Uri imageUri;
    private StorageReference imageRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ProfileFragmentBinding.inflate(inflater,container,false);


        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        String uid = auth.getUid();
        String imageName = "image_"+uid+".jpg";
        imageRef = storageReference.child("UserImages/"+imageName);
        reference = FirebaseDatabase.getInstance().getReference().child("UsersInfo").child(uid);

        binding.userProfilePf.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Profile Picture")
                    .setMessage("Do you want to change profile picture?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            imageLauncher.launch(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();


        });
        binding.userNamePf.setOnClickListener(v -> {
            showCustomSaveDialog();
        });

        getUserInfo();
        return binding.getRoot();
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
                                reference.updateChildren(value).addOnSuccessListener(unused -> {

                                    Toast.makeText(getActivity(), "Profile Image Changed", Toast.LENGTH_SHORT).show();

                                }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                                Toast.makeText(getActivity(), "image send to db "+imageUri, Toast.LENGTH_SHORT).show();

                            }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                        }
                    }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                }
            });
    private void getUserInfo(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue(String.class);
                    String imageUrl = snapshot.child("image").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    Log.e("MyApp","imageUrl "+imageUrl);
                    Log.e("MyApp","name "+name);
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
            reference.updateChildren(value).addOnSuccessListener(unused -> {

                Toast.makeText(getActivity(), "Name Changes", Toast.LENGTH_SHORT).show();
                getUserInfo();
            }).addOnFailureListener(e -> {
                Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });
            dialog.dismiss();
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }
}
