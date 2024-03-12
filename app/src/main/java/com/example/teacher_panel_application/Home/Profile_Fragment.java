package com.example.teacher_panel_application.Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.CreateGroup.CreateGroup_Activity;
import com.example.teacher_panel_application.Login.Login_Activity;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.realpacific.clickshrinkeffect.ClickShrinkUtils;
import com.vimalcvs.materialrating.MaterialRating;

import java.util.HashMap;


public class Profile_Fragment extends Fragment {
    private FragmentProfileBinding binding;
    private UploadTask uploadTask;
    private Uri imageUri;
    private StorageReference imageRef;
    private DatabaseReference reference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);


        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        String imageName = "image_"+".jpg";
        imageRef = storageReference.child("UserImages/"+imageName);

        ClickShrinkUtils.applyClickShrink(binding.rateTxt);
        binding.rateTxt.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            MaterialRating materialRating = new MaterialRating();
            materialRating.show(fragmentManager,"Rating");
        });
        FirebaseAuth auth = FirebaseAuth.getInstance();
        ClickShrinkUtils.applyClickShrink(binding.logoutCard);
        binding.logoutCard.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Logout")
                    .setMessage("Do you want Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), Login_Activity.class);
                            startActivity(intent);
                            getActivity().finish();
                            auth.signOut();
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

        ClickShrinkUtils.applyClickShrink(binding.createGroupCard);
        binding.createGroupCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateGroup_Activity.class);
            startActivity(intent);
        });

        String uid = auth.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("UsersInfo").child(uid);

        binding.userProfilePf.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imageLauncher.launch(intent);
        });
        binding.userNamePf.setOnClickListener(v -> {
            showCustomSaveDialog();
        });
        getUserInfo();
        return binding.getRoot();
    }
    private void getUserInfo(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue(String.class);
                    String imageUrl = snapshot.child("image").getValue(String.class);
                    Log.e("MyApp","imageUrl "+imageUrl);
                    Log.e("MyApp","name "+name);
                    Glide.with(getActivity())
                            .load(imageUrl)
                            .into(binding.userProfilePf);
                    binding.userNamePf.setText(name);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error "+ error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

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

    private void showCustomSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.edit_name_dialogue, null);
        builder.setView(dialogView);

        TextInputEditText editTextFileName = dialogView.findViewById(R.id.editNameEdDialogue);
        TextView buttonSave = dialogView.findViewById(R.id.textView10);
        TextView buttonCancel = dialogView.findViewById(R.id.textView11);

        AlertDialog dialog = builder.create();
        dialog.show();

        buttonSave.setOnClickListener(v -> {
            String name = editTextFileName.getText().toString();
            if (name.isEmpty()){
                editTextFileName.setError("Name is empty");
                return;
            }
            HashMap<String, Object> value = new HashMap<>();
            value.put("image",imageUri);
            reference.updateChildren(value).addOnSuccessListener(unused -> {

                Toast.makeText(getActivity(), "Name Changes", Toast.LENGTH_SHORT).show();

            }).addOnFailureListener(e -> {
                Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });
            dialog.dismiss();
        });
        buttonCancel.setOnClickListener(v -> {

            dialog.dismiss();
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }
}
