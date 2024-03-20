package com.example.teacher_panel_application.InsertData_Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.FragmentUploadAnnouncementBinding;
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

public class Upload_Announcement_Fragment extends Fragment {

    public Upload_Announcement_Fragment(){

    }
    private Uri selectedImageUri;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FragmentUploadAnnouncementBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUploadAnnouncementBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.announcementImage.setOnClickListener((v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imageLauncher.launch(intent);
        }));


        auth = FirebaseAuth.getInstance();
        String  uid = auth.getUid();

        binding.announcementImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (selectedImageUri != null){
                    selectedImageUri = null;
                    Toast.makeText(getActivity(), "Image removed", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        binding.dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setOnDateSetListener(new OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int month, int day) {
                        String selectedDate = year + ":" + (month + 1) + ":" + day;
                        binding.dueDate.setText(selectedDate);

                    }
                });
                timePickerFragment.show(getActivity().getSupportFragmentManager(),"date picker");

            }
        });
        binding.announcementUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleStr = binding.announceTitle.getText().toString();
                String desStr = binding.announceDescription.getText().toString();
                String lastDateStr = binding.dueDate.getText().toString();

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
                    Toast.makeText(getActivity(), "you can either upload image or text data", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(v ,"you can either upload image or text data! touch and hold to remove image", BaseTransientBottomBar.LENGTH_INDEFINITE);
                    snackbar.setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }

                    });
                    snackbar.show();

                }
                if (selectedImageUri != null) {
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
                            reference.setValue(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    Toast.makeText(getActivity(), "Image Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Errror "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());


                        });
                    }).addOnFailureListener(exception -> {
                        Toast.makeText(getActivity(), "Error "+exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    });

                }
                if (!TextUtils.isEmpty(titleStr)) {
                    if (!desStr.isEmpty()){



                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy");
                            String formattedDate = LocalDate.now().format(formatter);
                            hashMap.put("current_date",formattedDate);
                        }




                        hashMap.put("title",titleStr);
                        hashMap.put("due_date",lastDateStr);
                        hashMap.put("description",desStr);


                        reference.setValue(hashMap).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity(), "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                    }
                    else {
                        Snackbar snackbar = Snackbar.make(v,"date is empty", BaseTransientBottomBar.LENGTH_INDEFINITE);
                        snackbar.setAction("DISMISS", v1 -> snackbar.dismiss());
                        snackbar.show();
                    }
                }
            }
        });
    }




    private ActivityResultLauncher<Intent> imageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                    selectedImageUri = result.getData().getData();
                    binding.announcementImage.setImageURI(selectedImageUri);
                    //uploadTask = imageRef.putFile(selectedImageUri);
                }
            });
}
