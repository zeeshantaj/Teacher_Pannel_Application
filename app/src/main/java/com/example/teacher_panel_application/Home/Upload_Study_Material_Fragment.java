package com.example.teacher_panel_application.Home;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teacher_panel_application.Access.SendNotification;
import com.example.teacher_panel_application.Models.PDFModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.FragmentUploadAnnouncementBinding;
import com.example.teacher_panel_application.databinding.FragmentUploadStudyMaterialBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Upload_Study_Material_Fragment extends Fragment {


    public Upload_Study_Material_Fragment() {
        // Required empty public constructor
    }

    private FragmentUploadStudyMaterialBinding binding;
    ActivityResultLauncher<Intent> resultLauncher;
    private Uri pdfUri;
    private int switchLayoutCounter = 1;
    private int initialOptionsCounter = 2;
    String teacherName = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUploadStudyMaterialBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.classYearArray,
                android.R.layout.simple_spinner_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.selectYear.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.programming_languages,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.planetsSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.pdf_purpose_array,
                android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.pdfPurpose.setAdapter(adapter2);


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

        binding.selectPDfBtn.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // For Android 13 and above
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    // Request READ_MEDIA_IMAGES permission
                    ActivityCompat.requestPermissions(
                            getActivity(), new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
                } else {
                    // Permission granted, proceed with selecting PDF
                    selectPDF();
                }
            } else {
                // For Android 12 and below
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Request READ_EXTERNAL_STORAGE permission
                    ActivityCompat.requestPermissions(
                            getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    // Permission granted, proceed with selecting PDF
                    selectPDF();
                }
            }


//            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                // When permission is not granted
//                // Result permission
//                ActivityCompat.requestPermissions(
//                        getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//            } else {
//                // When permission is granted
//                // Create method
//                selectPDF();
//            }
        });

        binding.pdfUploadBtn.setOnClickListener(v -> {
            int selectedYearPosition = binding.selectYear.getSelectedItemPosition();
            int selectedSemesterPosition = binding.planetsSpinner.getSelectedItemPosition();
            int selectedPurposePosition = binding.pdfPurpose.getSelectedItemPosition();

            if (selectedYearPosition <= 0) {
                Toast.makeText(getActivity(), "Please Select Year", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedSemesterPosition <= 0) {
                Toast.makeText(getActivity(), "Please Select Semester", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedPurposePosition <= 0) {
                Toast.makeText(getActivity(), "Please Select Purpose", Toast.LENGTH_SHORT).show();
                return;
            }
            String year = binding.selectYear.getSelectedItem().toString();
            String semester  = binding.planetsSpinner.getSelectedItem().toString();
            String purpose = binding.pdfPurpose.getSelectedItem().toString();
            uploadFile(year,semester,purpose);
        });

        binding.switchToBtn.setOnClickListener(v -> {

            if (switchLayoutCounter == 1){
                switchLayoutCounter = 2;
                binding.pdfLayout.setVisibility(View.GONE);
                binding.videoLinkLayout.setVisibility(View.VISIBLE);
                binding.createPoolLayout.setVisibility(View.GONE);
                binding.switchToBtn.setText("Switch To Create Poll");
            }
            else if (switchLayoutCounter == 2){
                switchLayoutCounter = 3;
                binding.switchToBtn.setText("Switch To Upload PDF File");
                binding.createPoolLayout.setVisibility(View.VISIBLE);
                binding.videoLinkLayout.setVisibility(View.GONE);
                binding.pdfLayout.setVisibility(View.GONE);

            }
            else if (switchLayoutCounter == 3){
                switchLayoutCounter = 1;
                binding.switchToBtn.setText("Switch to Upload Video Link");
                binding.pdfLayout.setVisibility(View.VISIBLE);
                binding.createPoolLayout.setVisibility(View.GONE);
                binding.videoLinkLayout.setVisibility(View.GONE);
            }
        });


        addTextWatcher(binding.option2);
        binding.uploadPool.setOnClickListener(v -> {
            HashMap<String,String> hashMap = new HashMap<>();

            String option1 = binding.option1.getText().toString();
            String option2 = binding.option2.getText().toString();
            String question = binding.pollQuestion.getText().toString();
            if (question.isEmpty()){
                binding.pollQuestion.setError("Question can not be empty!");
                return;
            }
            if (option1.isEmpty()){
                binding.option1.setError("Set Option Please");
                return;
            }
            if (option2.isEmpty()){
                binding.option1.setError("Set Option Please");
                return;
            }
            for (int i = 0; i<getAllOptionTexts().size(); i++){
                hashMap.put("option"+i,getAllOptionTexts().get(i));
            }
            hashMap.put("question",question);



            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TeachersCreatedPoll")
                    .child(MethodsUtils.getCurrentUID())
                    .child(getMillis());
            reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    MethodsUtils.showFlawDialog(getActivity(),R.drawable.success_png,"Success ","Your Poll is Created",1);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    MethodsUtils.showFlawDialog(getActivity(),R.drawable.icon_error,"Error",e.getMessage(),1);
                }
            });

        });
        binding.uploadVideo.setOnClickListener(v -> {
            String videoLink = binding.videoLinkEdt.getText().toString();
            if (videoLink.isEmpty()){
                binding.videoLinkEdt.setError("Please Add Video Link");
                return;
            }


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TeachersUploadedVideoLink")
                    .child(MethodsUtils.getCurrentUID())
                    .child(getMillis());
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("videoLink",videoLink);
            reference.setValue(hashMap).addOnSuccessListener(unused ->
                    MethodsUtils.showFlawDialog(getActivity(),R.drawable.success_png,"Success ","Your Video Link Uploaded",1))
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    MethodsUtils.showFlawDialog(getActivity(),R.drawable.icon_error,"Error",e.getMessage(),1);
                }
            });
        });
    }

    private void addTextWatcher(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && isLastEditText(editText)) {
                    addNewOption();
                } else if (s.length() == 0 && isSecondLastEditText(editText)) {
                    removeLastOption();
                }
            }
        });
    }

    private boolean isLastEditText(EditText editText) {
        int index = binding.optionContainer.indexOfChild(editText);
        return index == binding.optionContainer.getChildCount() - 1;
    }

    private boolean isSecondLastEditText(EditText editText) {
        int index = binding.optionContainer.indexOfChild(editText);
        return index == binding.optionContainer.getChildCount() - 2;
    }

    private void addNewOption() {
        EditText newOption = new EditText(getActivity());
        newOption.setId(View.generateViewId());
        newOption.setLayoutParams(getLayoutParams());
        newOption.setHint("+ Add");

        binding.optionContainer.addView(newOption);
        addTextWatcher(newOption);
    }

    private void removeLastOption() {
        int lastIndex = binding.optionContainer.getChildCount() - 1;
        if (lastIndex > 0) {
            binding.optionContainer.removeViewAt(lastIndex);
        }
    }

    private LinearLayout.LayoutParams getLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(60);
        params.setMarginEnd(60);
        return params;
    }
    private List<String> getAllOptionTexts() {
        List<String> optionTexts = new ArrayList<>();
        int childCount = binding.optionContainer.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = binding.optionContainer.getChildAt(i);
            if (child instanceof EditText) {
                EditText editText = (EditText) child;
                String text = editText.getText().toString();
                if (!text.isEmpty()){
                    optionTexts.add(text);
                }

            }
        }

        return optionTexts;
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadFile(String year, String semester, String purpose) {
        if (pdfUri != null) {

            FirebaseAuth auth = FirebaseAuth.getInstance();
            String uid = auth.getUid();
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("TeacherInfo").child(uid);
            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        teacherName = snapshot.child("name").getValue(String.class);
                        String token = snapshot.child("FCMToken").getValue(String.class);
                        MethodsUtils.putString(getActivity(),"teacherName",teacherName);
                        MethodsUtils.putString(getActivity(),"FCMToken",token);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    teacherName = "";
                }
            });

            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd:hh:mm:ss:a");
            String currentDateTimeString = currentDateTime.format(dateTimeFormatter);


            String pdfName = getFileName(pdfUri);

            PDFModel pdfModel = new PDFModel();
            pdfModel.setYear(year);
            pdfModel.setSemester(semester);
            pdfModel.setPurpose(purpose);
            pdfModel.setDateTime(currentDateTimeString);
            pdfModel.setPDFName(pdfName);
            pdfModel.setTeacherName(MethodsUtils.getString(getActivity(),"teacherName"));
            pdfModel.setFCMToken(MethodsUtils.getString(getActivity(),"FCMToken"));
            pdfModel.setIdentifierForPDF(getMillis());

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TeachersPDFData")
                    .child(MethodsUtils.getCurrentUID())
                    .child(getMillis());

            StorageReference storageReference = FirebaseStorage.getInstance().getReference("Teacher_Uploaded_PDF");
            String fileName = System.currentTimeMillis() + ".pdf";
            StorageReference fileReference = storageReference.child(fileName);
            fileReference.putFile(pdfUri)
                    .addOnSuccessListener(taskSnapshot -> {

                        ///Toast.makeText(getActivity(), "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            pdfModel.setPDFUrl(downloadUrl);
                            reference.setValue(pdfModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    MethodsUtils.showSuccessDialog(getActivity(),"Success","Data Posted Successfully", SweetAlertDialog.SUCCESS_TYPE);
                                    notifyUsers(pdfModel);
                                    binding.selectYear.setSelection(0);
                                    binding.pdfPurpose.setSelection(0);
                                    binding.planetsSpinner.setSelection(0);
                                    binding.selectPDfBtn.setText("Click to Select A PDF");
                                    binding.pdfProgressTxt.setVisibility(View.INVISIBLE);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
    private void notifyUsers(PDFModel newPdfModel) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("StudentsInfo");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                    String year = userSnapshot.child("studentYear").getValue(String.class);
                    String semester = userSnapshot.child("studentSemester").getValue(String.class);
                    String token = userSnapshot.child("FCMToken").getValue(String.class);

                    if (newPdfModel.getYear().equals(year) && newPdfModel.getSemester().equals(semester)) {
                        //sendNotification(token, "New PDF Available", "Check out the new PDF added.");
                        SendNotification sendNotification = new SendNotification(token,
                                "check out new pdf\n"+"by sir "+MethodsUtils.getString(getActivity(),"teacherName"),
                                "new pdf uploaded!",getActivity());
                        sendNotification.sendNotification();


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }
    private String getTeacherName(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TeacherInfo").child(MethodsUtils.getCurrentUID());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    teacherName = snapshot.child("name").getValue(String.class);
                    String token = snapshot.child("FCMToken").getValue(String.class);
                    MethodsUtils.putString(getActivity(),"teacherName",teacherName);
                    MethodsUtils.putString(getActivity(),"FCMToken",token);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                teacherName = "";
            }
        });
        return teacherName;
    }

    private String getMillis(){
        Calendar calendar = Calendar.getInstance();
        long milli = calendar.getTimeInMillis();
        return String.valueOf(milli);
    }
}