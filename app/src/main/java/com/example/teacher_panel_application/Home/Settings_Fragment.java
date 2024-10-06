package com.example.teacher_panel_application.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.teacher_panel_application.Fragment.TermsAndConditionFragment;
import com.example.teacher_panel_application.Login.Login_Activity;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.Utils.ProgressHelper;
import com.example.teacher_panel_application.VideoStreaming.ReceivedVideoStreamingActivity;
import com.example.teacher_panel_application.VideoStreaming.ZegoCloudVideoStreamingActivity;
import com.example.teacher_panel_application.databinding.FragmentSettingsBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.realpacific.clickshrinkeffect.ClickShrinkUtils;
import com.vimalcvs.materialrating.MaterialRating;


public class Settings_Fragment extends Fragment {
    private FragmentSettingsBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        ClickShrinkUtils.applyClickShrink(binding.rateTxt);

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("loginType", Context.MODE_PRIVATE);
        boolean isTrue = sharedPreferences2.getBoolean("typeBool",false);
        if (isTrue){
            // student
            binding.startVideoStreamingCard.setVisibility(View.GONE);
            binding.receivedVideo.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), ReceivedVideoStreamingActivity.class));
            });
        }else {
            // teacher
            binding.receivedVideo.setVisibility(View.GONE);
            binding.startVideoStreamingCard.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), ZegoCloudVideoStreamingActivity.class));
            });
        }

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
                            SharedPreferences sharedPreference = getActivity().getSharedPreferences("loginType", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreference.edit();
                            editor.remove("typeBool"); // To remove the value
                            editor.apply();
                            ProgressHelper.showDialog(getContext(),"Logout","Please Wait...");
                            Intent intent = new Intent(getActivity(), Login_Activity.class);
                            startActivity(intent);
                            getActivity().finish();
                            auth.signOut();
                            //ProgressHelper.dismissDialog();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //ProgressHelper.dismissDialog();
                        }
                    })
                    .show();
        });

        binding.profileCard.setOnClickListener(v -> {
            openFragment();
        });

        binding.termsAndCondCard.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            MethodsUtils.setFragment(fragmentManager,new TermsAndConditionFragment());
        });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("notificationState", Context.MODE_PRIVATE);
        boolean isChecked = sharedPreferences.getBoolean("isNotification", true);
        binding.notificationChecked.setChecked(isChecked);
        binding.notificationChecked.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            if (isChecked1){
                setSnackBar(buttonView,"You'll receive notification when class ended!");
            }
            else {
                setSnackBar(buttonView,"You'll no longer receive notification when class ended!");
            }
            SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("notificationState", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putBoolean("isNotification", isChecked1);
            editor.apply();
        });

        ClickShrinkUtils.applyClickShrink(binding.updateBtn);
        binding.updateBtn.setOnClickListener(v -> {

        });

        getVersion();
    }
    public void openFragment() {
        ProfileFragment editDataFragment = new ProfileFragment();
        editDataFragment.show(getActivity().getSupportFragmentManager(),editDataFragment.getTag());
    }
    private void setSnackBar(View buttonView,String message){
        Snackbar snackbar = Snackbar.make(buttonView, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Dismiss", view -> snackbar.dismiss());
        snackbar.show();
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
