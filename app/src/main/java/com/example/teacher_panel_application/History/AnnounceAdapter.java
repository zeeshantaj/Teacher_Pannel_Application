package com.example.teacher_panel_application.History;


import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.AnnounceDataLayoutBinding;
import com.example.teacher_panel_application.databinding.AnnounceImgLaoutBinding;
import com.example.teacher_panel_application.databinding.ClasshistoryRecyclerItemBinding;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AnnounceAdapter extends RecyclerView.Adapter<AnnounceAdapter.ViewHolder> {

//    List<AnnouncementModel> announcementModelList;
//
//    public AnnounceAdapter(List<AnnouncementModel> announcementModelList) {
//        this.announcementModelList = announcementModelList;
//    }
//
//    @NonNull
//    @Override
//    public AnnounceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        AnnounceDataLayoutBinding binding = AnnounceDataLayoutBinding.inflate(inflater,parent,false);
//        return new ViewHolder(binding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull AnnounceAdapter.ViewHolder holder, int position) {
//
//        AnnouncementModel model = announcementModelList.get(position);
//
//        holder.binding.itemDate.setText(model.getCurrent_date());
//        holder.binding.itemTItle.setText(model.getTitle());
//       // holder.title.setVisibility(View.VISIBLE);
//        holder.binding.itemDes.setText(model.getDescription());
//        //holder.description.setVisibility(View.VISIBLE);
//        holder.binding.itemDueDate.setText(model.getDue_date());
//        holder.binding.itemDueDate.setVisibility(View.VISIBLE);
//        //holder.date.setVisibility(View.VISIBLE);
//
//        holder.binding.itemImage.setVisibility(View.VISIBLE);
//        Glide.with(holder.binding.itemImage.getContext())
//                .load(model.getImageUrl())
//                .into(holder.binding.itemImage);
//
//        Log.e("MyApp","itemImageUrl"+model.getImageUrl());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return announcementModelList.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private AnnounceDataLayoutBinding binding;
//        public ViewHolder(@NonNull AnnounceDataLayoutBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//    }

    private List<AnnouncementModel> announcementModelList;
    private Context context;

    public AnnounceAdapter(List<AnnouncementModel> announcementModelList, Context context) {
        this.announcementModelList = announcementModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0) {
            // Inflate layout for title/description
            view = inflater.inflate(R.layout.announce_data_layout, parent, false);
        } else {
            // Inflate layout for image
            view = inflater.inflate(R.layout.announce_img_laout, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnnouncementModel model = announcementModelList.get(position);

        if (getItemViewType(position) == 0) {
            // Set data for title/description layout
            AnnounceDataLayoutBinding binding = AnnounceDataLayoutBinding.bind(holder.itemView);
            binding.itemDate.setText(model.getCurrent_date());
            binding.itemTitle.setText(model.getTitle());
            binding.itemDes.setText(model.getDescription());
            binding.itemDueDate.setText(model.getDue_date());
        } else {
            // Set data for image layout
            AnnounceImgLaoutBinding binding = AnnounceImgLaoutBinding.bind(holder.itemView);
            //binding.itemDate.setText(model.getCurrent_date());


            Glide.with(holder.itemView.getContext())
                    .load(model.getImageUrl())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(15)))
                    .into(binding.itemImage);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "data "+holder.getAdapterPosition()+model.getCurrent_date(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return announcementModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        AnnouncementModel model = announcementModelList.get(position);
        return model.getImageUrl() != null ? 1 : 0; // Return 1 for image, 0 for title/description
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public void removeItem(int position, String currentDate) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        DatabaseReference reference = FirebaseDatabase
                .getInstance()
                .getReference("Announcement")
                .child(uid);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.delete_item_layout, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        MaterialButton noBtn = dialogView.findViewById(R.id.noBtn);
        MaterialButton yesBtn = dialogView.findViewById(R.id.deleteBtn);

        yesBtn.setOnClickListener(v -> reference.removeValue().addOnSuccessListener(unused -> {
            if (position >= 0 && position < announcementModelList.size()) {
                announcementModelList.remove(position);
                notifyItemRemoved(position);
            }
            dialog.dismiss();
            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> Toast.makeText(context, "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show()));
        noBtn.setOnClickListener(v -> dialog.dismiss());
    }
}
