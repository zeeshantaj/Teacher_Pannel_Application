package com.example.teacher_panel_application.History.StudyMaterial;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Html;
import android.util.Log;
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
import androidx.core.content.ContextCompat;

import com.example.teacher_panel_application.Access.SendNotification;
import com.example.teacher_panel_application.History.StudyMaterial.Adapter.SubmittedModel;
import com.example.teacher_panel_application.Models.PDFModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.StudentSubmitPdfFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.rajat.pdfviewer.PdfViewerActivity;
import com.rajat.pdfviewer.util.saveTo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.internal.cache.DiskLruCache;

public class SubmitePDF_Fragment extends BottomSheetDialogFragment {
    public SubmitePDF_Fragment() {
    }
    StudentSubmitPdfFragmentBinding binding;
    ActivityResultLauncher<Intent> resultLauncher;
    private Uri pdfUri;
    private String teacherName,teacherFCMToken,pdfIdentifier,year,semester,purpose;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = StudentSubmitPdfFragmentBinding.inflate(inflater,container,false);

        if (getArguments() != null) {
            String json = getArguments().getString("PDFDataFromTeacher");
            PDFModel model = new Gson().fromJson(json, PDFModel.class);
            if (model != null) {
                 teacherName = model.getTeacherName();
                 teacherFCMToken = model.getFCMToken();
                 purpose = model.getPurpose();
                 year = model.getYear();
                 semester = model.getSemester();

                binding.cardView3.setOnClickListener(v -> {
                    launchPDf(model.getPDFUrl(),model.getPDFName());
                });

                 binding.pdfUploadDate.setText("Date : "+model.getDateTime());
                 binding.pdfUploadedPur.setText("Purpose "+model.getPurpose());
                 binding.pdfUploadedPur.setText("Group: "+model.getYear()+" ("+model.getSemester()+")");
                 binding.pdfFileName.setText(model.getPDFName());
                 pdfIdentifier = model.getIdentifierForPDF();

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String uid = auth.getUid();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("StudentsSubmittedPDF")
                        .child(uid);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                if (dataSnapshot.exists()){
                                    String identifier = dataSnapshot.child("pdfIdentifier").getValue(String.class);
                                    boolean isChecked = dataSnapshot.child("checked").getValue(Boolean.class);
                                    String fromMark = dataSnapshot.child("fromMark").getValue(String.class);
                                    String outOfMark = dataSnapshot.child("outOfMark").getValue(String.class);
                                    String remark = dataSnapshot.child("remark").getValue(String.class);

                                    if (identifier != null && pdfIdentifier != null){
                                        Log.e("MyApp","identifier "+identifier);
                                        Log.e("MyApp","identifier "+pdfIdentifier);
                                        if (!identifier.isEmpty()&&!pdfIdentifier.isEmpty()){
                                            if (identifier.equals(pdfIdentifier)){
                                                binding.submittedTxt.setVisibility(View.VISIBLE);
                                                binding.submittedTxt.setText("Your Assignment is Submitted to sir "+teacherName);
                                                binding.assignmentSubmissionLay.setVisibility(View.GONE);
                                                binding.subProgress.setVisibility(View.GONE);
                                                binding.imageView3.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.green2)));
                                                binding.imageView4.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.green2)));

                                                if (isChecked){
                                                    binding.imageView5.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.green2)));
                                                    binding.resultLayout.setVisibility(View.VISIBLE);
                                                    binding.resultNumber.setText("Your Obtained marks "+outOfMark + " out of "+""+fromMark);
                                                    if (!remark.equals("")){
                                                        binding.remartxt.setText("Remark given by Sir "+teacherName+" "+remark);
                                                    }

                                                }

                                            }else {
                                                binding.subProgress.setVisibility(View.GONE);
                                                binding.submittedTxt.setVisibility(View.GONE);
                                                binding.assignmentSubmissionLay.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }
                                }else {
                                    binding.subProgress.setVisibility(View.GONE);
                                    binding.submittedTxt.setVisibility(View.GONE);
                                    binding.assignmentSubmissionLay.setVisibility(View.VISIBLE);
                                }

                            }
                        }else {
                            binding.subProgress.setVisibility(View.GONE);
                            binding.submittedTxt.setVisibility(View.GONE);
                            binding.assignmentSubmissionLay.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

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
                        }
                    }
                });

        binding.selectPDfBtn.setOnClickListener(v -> {
            selectPDF();
        });

        binding.pdfUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    uploadFile();
                }
            }
        });
        return binding.getRoot();
    }
    private void launchPDf(String url,String name){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("empty","empty");
        startActivity(PdfViewerActivity.Companion.launchPdfFromUrl(getActivity(),url,name, saveTo.ASK_EVERYTIME,true,hashMap));
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadFile() {
        if (pdfUri != null) {


            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd:hh:mm:ss:a");
            String currentDateTimeString = currentDateTime.format(dateTimeFormatter);


            String pdfName = getFileName(pdfUri);



            SubmittedModel model = new SubmittedModel();
            model.setDateTime(currentDateTimeString);
            model.setPDFName(pdfName);
            model.setPdfIdentifier(pdfIdentifier);
            model.setChecked(false);
            model.setRemark("");
            model.setFromMark("");
            model.setOutOfMark("");
            model.setSemester(semester);
            model.setYear(year);
            model.setPurpose(purpose);
            model.setOutOfMark("");
            model.setUid(MethodsUtils.getCurrentUID());
            model.setUserName(MethodsUtils.getString(getActivity(),"studentName"));
            model.setImgUrl(MethodsUtils.getString(getActivity(),"studentImage"));
            model.setStudentFCMTOKEN(MethodsUtils.getString(getActivity(),"StudentFCMToken"));


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

                                    SendNotification sendNotification = new SendNotification(teacherFCMToken,
                                            "check out Assignment\n",
                                            MethodsUtils.getString(getActivity(),"studentName") +"Submitted the assignment!",getActivity());
                                    sendNotification.sendNotification();

                                    binding.pdfUploadBtn.setEnabled(false);

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
                        int progressInt = (int) progress;
                        binding.pdfProgressTxt.setText("Uploading... " + progressInt + "%");
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
    private String getMillis(){
        Calendar calendar = Calendar.getInstance();
        long milli = calendar.getTimeInMillis();
        return String.valueOf(milli);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("StudentsInfo").child(uid);
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String imageUrl = snapshot.child("image").getValue(String.class);
                    String fcm = snapshot.child("FCMToken").getValue(String.class);
                    MethodsUtils.putString(getActivity(),"studentName",name);
                    MethodsUtils.putString(getActivity(),"studentImage",imageUrl);
                    MethodsUtils.putString(getActivity(),"StudentFCMToken",fcm);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}

