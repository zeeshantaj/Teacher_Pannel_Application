package com.example.teacher_panel_application.InsertData_Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teacher_panel_application.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;

public class Notification_Announcement extends Fragment {

    public Notification_Announcement(){

    }

    private TextInputEditText title,description,lastDate;
    private ImageView announceImage;
    private Button uploadBtn;
    private Uri selectedImageUri;

    private DatabaseReference reference;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fagment_notification_annoucement, container, false);

        title = view.findViewById(R.id.announceTitle);
        description = view.findViewById(R.id.announceDescription);
        lastDate = view.findViewById(R.id.lastDate);
        announceImage = view.findViewById(R.id.announcementImage);
        uploadBtn = view.findViewById(R.id.announcementUploadBtn);
        announceImage.setOnClickListener((v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imageLauncher.launch(intent);
        }));


        auth = FirebaseAuth.getInstance();
        String  uid = auth.getUid();

        announceImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (selectedImageUri != null){
                    selectedImageUri = null;
                    Toast.makeText(getActivity(), "Image removed", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleStr = title.getText().toString();
                String desStr = description.getText().toString();
                String lastDateStr = lastDate.getText().toString();

                //todo for image
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference imageRef = storageRef.child("images/myImage.jpg");

                //

                Calendar calendar = Calendar.getInstance();
                long milli = calendar.getTimeInMillis();
                String child = String.valueOf(milli);
                reference = FirebaseDatabase.getInstance().getReference("Announcement").child(uid).child(child);

                HashMap<String, String> hashMap = new HashMap<>();
                if (selectedImageUri != null && !titleStr.isEmpty()) {
                        // Both image and text are selected, show a Snackbar.
                    Toast.makeText(getActivity(), "you can either upload image or text data", Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(v ,"you can either upload image or text data! touch and hold to remove image", BaseTransientBottomBar.LENGTH_INDEFINITE);
                        snackbar.setAction("DISMISS", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }

                        });
                        snackbar.show();
                    } else if (selectedImageUri != null) {
                    UploadTask uploadTask = imageRef.putFile(selectedImageUri);

                    uploadTask.addOnSuccessListener(taskSnapshot -> {

                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy");
                                String formattedDate = LocalDate.now().format(formatter);
                                hashMap.put("current_date",formattedDate);
                            }
                            hashMap.put("imageUrl",downloadUrl);
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getActivity(), "Image Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Errror "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        });
                    }).addOnFailureListener(exception -> {
                        Toast.makeText(getActivity(), "Error "+exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    });

                } else if (!TextUtils.isEmpty(titleStr)) {
                        if (!desStr.isEmpty()){



                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy");
                                String formattedDate = LocalDate.now().format(formatter);
                                hashMap.put("current_date",formattedDate);
                            }

                            hashMap.put("title",titleStr);
                            hashMap.put("due_date",lastDateStr);
                            hashMap.put("description",desStr);


                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getActivity(), "Uploaded ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            Snackbar snackbar = Snackbar.make(v,"date is empty", BaseTransientBottomBar.LENGTH_INDEFINITE);
                            snackbar.setAction("DISMISS", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        }


                    }

            }


        });

        return view;
    }
    private ActivityResultLauncher<Intent> imageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                    selectedImageUri = result.getData().getData();
                    announceImage.setImageURI(selectedImageUri);
                    //uploadTask = imageRef.putFile(selectedImageUri);
                }
            });
}
