package com.example.teacher_panel_application.History;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.teacher_panel_application.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Announcement_full_view_Fragment extends Fragment {


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