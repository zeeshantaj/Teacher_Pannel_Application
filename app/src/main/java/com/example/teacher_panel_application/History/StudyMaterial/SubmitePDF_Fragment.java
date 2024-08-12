package com.example.teacher_panel_application.History.StudyMaterial;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.History.StudyMaterial.Adapter.SubmittedModel;
import com.example.teacher_panel_application.Models.PDFModel;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.StudentSubmitPdfFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SubmitePDF_Fragment extends DialogFragment {
    public SubmitePDF_Fragment() {
    }

    StudentSubmitPdfFragmentBinding binding;
    ActivityResultLauncher<Intent> resultLauncher;
    private Uri pdfUri;
    private String userName,userImg,teacherName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = StudentSubmitPdfFragmentBinding.inflate(inflater,container,false);

        if (getArguments() != null) {
            String json = getArguments().getString("PDFDataFromTeacher");
            PDFModel model = new Gson().fromJson(json, PDFModel.class);
            if (model != null) {
                 teacherName = model.getTeacherName();
            }
        }

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts
                        .StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(
                            ActivityResult result) {
                        // Initialize result data
                        Intent data = result.getData();
                        // check condition
                        if (data != null) {
                            // When data is not equal to empty
                            // Get PDf uri
                            Uri sUri = data.getData();
                            pdfUri = data.getData();
                            String pdfName = getFileName(sUri);
                            // set Uri on text view

                            binding.selectPDfBtn.setVisibility(View.VISIBLE);
                            binding.pdfUploadBtn.setVisibility(View.VISIBLE);
                            binding.selectPDfBtn.setText(Html.fromHtml(
                                    "<big><b>Selected PDF</b></big><br>"
                                            + pdfName));

                            // Get PDF path
                            String sPath = sUri.getPath();
                            // Set path on text view
//                            tvPath.setText(Html.fromHtml(
//                                    "<big><b>PDF Path</b></big><br>"
//                                            + sPath));
                        }
                    }
                });


        binding.pdfUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadFile() {
        if (pdfUri != null) {

            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd:hh:mm:ss:a");
            String currentDateTimeString = currentDateTime.format(dateTimeFormatter);


            String pdfName = getFileName(pdfUri);

            getUserInfo();

            SubmittedModel model = new SubmittedModel();
            model.setDateTime(currentDateTimeString);
            model.setPDFName(pdfName);
            model.setUid(MethodsUtils.getCurrentUID());
            model.setUserName(userName);
            model.setImgUrl(userImg);


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("StudentsSubmittedPDF")
                    .child(MethodsUtils.getCurrentUID())
                    .child(getMillis());

            StorageReference storageReference = FirebaseStorage.getInstance().getReference("Students_Uploaded_PDF");
            String fileName = System.currentTimeMillis() + ".pdf";
            StorageReference fileReference = storageReference.child(fileName);
            fileReference.putFile(pdfUri)
                    .addOnSuccessListener(taskSnapshot -> {

                        ///Toast.makeText(getActivity(), "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            model.setPDFUrl(downloadUrl);
                            reference.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    MethodsUtils.showSuccessDialog(getActivity(),"Success","Data Posted Successfully", SweetAlertDialog.SUCCESS_TYPE);

                                    notifyUsers(pdfModel);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    MethodsUtils.showSuccessDialog(getActivity(),"Error","PDF not Posted", SweetAlertDialog.ERROR_TYPE);
                                }
                            });
                        });
                    })
                    .addOnFailureListener(e -> {
                        binding.pdfProgressTxt.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "File Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        binding.pdfProgressTxt.setVisibility(View.VISIBLE);
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        binding.pdfProgressTxt.setText("Uploading... "+progress+"%");
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    private void selectPDF() {
        // Initialize intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // set type
        intent.setType("application/pdf");
        // Launch intent
        resultLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // When permission is granted
            // Call method
            selectPDF();
        } else {
            // When permission is denied
            // Display toast
            Toast.makeText(getActivity(), "Permission Denied",Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    private void getUserInfo() {
        MethodsUtils.getCurrentUserRef("StudentsInfo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String imageUrl = snapshot.child("image").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    userName = name;
                    userImg = imageUrl;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private String getMillis(){
        Calendar calendar = Calendar.getInstance();
        long milli = calendar.getTimeInMillis();
        return String.valueOf(milli);
    }
}
