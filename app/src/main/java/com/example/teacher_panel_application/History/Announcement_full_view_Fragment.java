package com.example.teacher_panel_application.History;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.example.teacher_panel_application.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class Announcement_full_view_Fragment extends DialogFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.FullScreenDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_announce_full_view, container, false);
        TextView title = view.findViewById(R.id.AFVTitle);
        TextView description = view.findViewById(R.id.AFVDescription);
        TextView postedDate = view.findViewById(R.id.AFVPostedDate);
        TextView dueDate = view.findViewById(R.id.AFVDueDate);
        ImageView imageView = view.findViewById(R.id.AFVImageView);
        MaterialButton deleteBtn = view.findViewById(R.id.AFVDeletBtn);
        MaterialButton dismissBtn = view.findViewById(R.id.AFVDismissBtn);
        ConstraintLayout constraintLayout = view.findViewById(R.id.AFVTitleDesView);
        if (getArguments() != null) {
            String json = getArguments().getString("announcement");
            AnnouncementModel model = new Gson().fromJson(json, AnnouncementModel.class);
            if (model != null) {
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
            }
        }


        return view;
    }


}
//    public void removeItem(int position, String currentDate) {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        String uid = auth.getUid();
//        DatabaseReference reference = FirebaseDatabase
//                .getInstance()
//                .getReference("Announcement")
//                .child(uid);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View dialogView = LayoutInflater.from(context).inflate(R.layout.delete_item_layout, null);
//        builder.setView(dialogView);
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//        MaterialButton noBtn = dialogView.findViewById(R.id.noBtn);
//        MaterialButton yesBtn = dialogView.findViewById(R.id.deleteBtn);
//
//        yesBtn.setOnClickListener(v -> reference.removeValue().addOnSuccessListener(unused -> {
//            if (position >= 0 && position < announcementModelList.size()) {
//                announcementModelList.remove(position);
//                notifyItemRemoved(position);
//            }
//            dialog.dismiss();
//            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
//        }).addOnFailureListener(e -> Toast.makeText(context, "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show()));
//        noBtn.setOnClickListener(v -> dialog.dismiss());
//    }