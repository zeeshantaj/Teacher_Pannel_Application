package com.example.teacher_panel_application.Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.Fragment.TermsAndConditionFragment;
import com.example.teacher_panel_application.Login.Login_Activity;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.FragmentSettingsBinding;
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


public class Settings_Fragment extends Fragment {
    private FragmentSettingsBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater,container,false);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        ClickShrinkUtils.applyClickShrink(binding.rateTxt);
        binding.rateTxt.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            MaterialRating materialRating = new MaterialRating();
            materialRating.show(fragmentManager,"Rating");
        });

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

        binding.profileCard.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            MethodsUtils.setFragment(fragmentManager,new ProfileFragment());
        });

        binding.termsAndCondCard.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            MethodsUtils.setFragment(fragmentManager,new TermsAndConditionFragment());
        });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("notificationState", Context.MODE_PRIVATE);
        boolean isChecked = sharedPreferences.getBoolean("isNotification", true);
        binding.notificationChecked.setChecked(isChecked);
        binding.notificationChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getActivity(), "Notification UnMute", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Notification Muted", Toast.LENGTH_SHORT).show();
                }

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("notificationState", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isNotification",isChecked);
                editor.apply();
            }
        });
        getVersion();
        return binding.getRoot();
    }

    private void getVersion(){

        PackageManager packageManager = getActivity().getPackageManager();
        String packageName = getActivity().getPackageName();
        String versionName = null;
        try {
            versionName = "Version " + packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        binding.versionTxt.setText(versionName);

    }
}
