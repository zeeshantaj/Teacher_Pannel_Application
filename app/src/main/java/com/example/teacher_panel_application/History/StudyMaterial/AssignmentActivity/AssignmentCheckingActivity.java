package com.example.teacher_panel_application.History.StudyMaterial.AssignmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.ActivityAssignmentCheckingActvityBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssignmentCheckingActivity extends AppCompatActivity {

    private ActivityAssignmentCheckingActvityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentCheckingActvityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Check Assignemnt");

        List<String> arr = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            if (i == 0) {
                arr.add("Select Marks");
            }
            arr.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                arr
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.outOfMarks.setAdapter(adapter);

        List<String> arr1 = new ArrayList<>();
        arr1.add("Select Marks");
        for (int i = 5; i <= 100; i += 5) {
            arr1.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                arr1
        );

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.fromMarks.setAdapter(adapter1);


        binding.SignUpName.requestFocus();
// Suppress the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.SignUpName.getWindowToken(), 0);
    }
}