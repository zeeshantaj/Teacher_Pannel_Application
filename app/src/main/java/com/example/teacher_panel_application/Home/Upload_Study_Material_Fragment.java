package com.example.teacher_panel_application.Home;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.FragmentUploadAnnouncementBinding;
import com.example.teacher_panel_application.databinding.FragmentUploadStudyMaterialBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Upload_Study_Material_Fragment extends Fragment {


    public Upload_Study_Material_Fragment() {
        // Required empty public constructor
    }

    private FragmentUploadStudyMaterialBinding binding;
    ActivityResultLauncher<Intent> resultLauncher;
    private Uri pdfUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUploadStudyMaterialBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

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
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // When permission is not granted
                // Result permission
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{
                                Manifest.permission
                                        .READ_EXTERNAL_STORAGE},
                        1);
            } else {
                // When permission is granted
                // Create method
                selectPDF();
            }
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
    private void uploadFile(String year,String semester,String purpose) {
        if (pdfUri != null) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("year",year);
            hashMap.put("semester",semester);
            hashMap.put("purpose",purpose);
            Calendar calendar = Calendar.getInstance();
            long milli = calendar.getTimeInMillis();
            String milliSecondChild = String.valueOf(milli);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Announcement")
                    .child(MethodsUtils.getCurrentUID())
                    .child(milliSecondChild);

            StorageReference storageReference = FirebaseStorage.getInstance().getReference("Teacher_Uploaded_PDF");
            String fileName = System.currentTimeMillis() + ".pdf";
            StorageReference fileReference = storageReference.child(fileName);
            fileReference.putFile(pdfUri)
                    .addOnSuccessListener(taskSnapshot -> {

                        ///Toast.makeText(getActivity(), "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            hashMap.put("PDF_URL",downloadUrl);
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    MethodsUtils.showSuccessDialog(getActivity(),"Success","Data Posted Successfully", SweetAlertDialog.SUCCESS_TYPE);
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

                        binding.pdfProgressTxt.setText("Uploading... "+progress+"%");
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}