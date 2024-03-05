package com.example.teacher_panel_application.Home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.teacher_panel_application.CreateGroup.CreateGroup_Activity;
import com.example.teacher_panel_application.Login.Login_Activity;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;
import com.realpacific.clickshrinkeffect.ClickShrinkUtils;
import com.vimalcvs.materialrating.MaterialRating;


public class Profile_Fragment extends Fragment {
    private FragmentProfileBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);

        ClickShrinkUtils.applyClickShrink(binding.rateTxt);
        binding.rateTxt.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            MaterialRating materialRating = new MaterialRating();
            materialRating.show(fragmentManager,"Rating");
        });
        ClickShrinkUtils.applyClickShrink(binding.logoutCard);
        binding.logoutCard.setOnClickListener(v -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
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


        return binding.getRoot();
    }
}
