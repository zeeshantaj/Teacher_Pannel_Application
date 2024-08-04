package com.example.teacher_panel_application.History.Announcement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.example.teacher_panel_application.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

public class Announcement_full_view_Fragment extends DialogFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_announce_full_view, container, false);
        TextView title = view.findViewById(R.id.AFVTitle);
        TextView description = view.findViewById(R.id.AFVDescription);
        TextView postedDate = view.findViewById(R.id.AFVPostedDate);
        TextView dueDate = view.findViewById(R.id.AFVDueDate);
        PhotoView imageView = view.findViewById(R.id.AFVImageView);
        MaterialButton deleteBtn = view.findViewById(R.id.AFVDeletBtn);
        MaterialButton dismissBtn = view.findViewById(R.id.AFVDismissBtn);
        ConstraintLayout constraintLayout = view.findViewById(R.id.AFVTitleDesView);
        if (getArguments() != null) {
            String json = getArguments().getString("announcement");
            AnnouncementModel model = new Gson().fromJson(json, AnnouncementModel.class);
            if (model != null) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginType", Context.MODE_PRIVATE);
                boolean isTrue = sharedPreferences.getBoolean("typeBool",false);

                // Use the data as needed
                dueDate.setText(model.getDue_date());
                postedDate.setText(model.getCurrent_date());
                if (model.getTitle() == null || model.getTitle().isEmpty()) {
                    // set imageview
                    imageView.setVisibility(View.VISIBLE);
                    Glide.with(getActivity())
                            .load(model.getImageUrl())
                            .into(imageView);

                }

                if (model.getImageUrl() == null || model.getImageUrl().isEmpty()) {
                    // set title and des here
                    constraintLayout.setVisibility(View.VISIBLE);
                    title.setText(model.getTitle());
                    description.setText(model.getDescription());
                }
                if (!isTrue){
                    deleteBtn.setVisibility(View.VISIBLE);
                    dismissBtn.setVisibility(View.VISIBLE);
                    deleteBtn.setOnClickListener(v -> {
                        String id = model.getId();
                        String url = model.getImageUrl();
                        removeItem(id,url);
                    });
                    dismissBtn.setOnClickListener(v -> {
                        dismiss();
                    });
                }else {
                    deleteBtn.setVisibility(View.GONE);
                    dismissBtn.setVisibility(View.GONE);
                }

            }
        }
        return view;
    }

    public void removeItem(String id, String imageUrl) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Announcement").child(uid).child(id);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.delete_item_layout, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        MaterialButton noBtn = dialogView.findViewById(R.id.noBtn);
        MaterialButton yesBtn = dialogView.findViewById(R.id.deleteBtn);

        yesBtn.setOnClickListener(v -> {


            if (imageUrl != null && !imageUrl.isEmpty()) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(aVoid -> {
                    databaseReference.removeValue().addOnSuccessListener(unused -> {
                        Toast.makeText(getActivity(), "Announcement deleted successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        dismiss();
                    }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Error deleting Announcement: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                }).addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Error deleting image: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
            }else {
                databaseReference.removeValue().addOnSuccessListener(unused -> {
                    Toast.makeText(getActivity(), "Announcement deleted successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    dismiss();
                }).addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Error deleting Announcement: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

        noBtn.setOnClickListener(v -> {
            dialog.dismiss();
            dismiss();
        });
    }
}