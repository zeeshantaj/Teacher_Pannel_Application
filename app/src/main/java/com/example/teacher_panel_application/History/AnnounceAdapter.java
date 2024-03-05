package com.example.teacher_panel_application.History;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.AnnounceDataLayoutBinding;
import com.example.teacher_panel_application.databinding.ClasshistoryRecyclerItemBinding;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class AnnounceAdapter extends RecyclerView.Adapter<AnnounceAdapter.ViewHolder> {

    List<AnnouncementModel> announcementModelList;

    public AnnounceAdapter(List<AnnouncementModel> announcementModelList) {
        this.announcementModelList = announcementModelList;
    }

    @NonNull
    @Override
    public AnnounceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AnnounceDataLayoutBinding binding = AnnounceDataLayoutBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnounceAdapter.ViewHolder holder, int position) {

        AnnouncementModel model = announcementModelList.get(position);

        holder.binding.itemDate.setText(model.getCurrent_date());
        holder.binding.itemTItle.setText(model.getTitle());
       // holder.title.setVisibility(View.VISIBLE);
        holder.binding.itemDes.setText(model.getDescription());
        //holder.description.setVisibility(View.VISIBLE);
        holder.binding.itemDueDate.setText(model.getDue_date());
        holder.binding.itemDueDate.setVisibility(View.VISIBLE);
        //holder.date.setVisibility(View.VISIBLE);

        holder.binding.itemImage.setVisibility(View.VISIBLE);
        Glide.with(holder.binding.itemImage.getContext())
                .load(model.getImageUrl())
                .into(holder.binding.itemImage);
        Log.e("MyApp","itemImageUrl"+model.getImageUrl());

    }

    @Override
    public int getItemCount() {
        return announcementModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AnnounceDataLayoutBinding binding;
        public ViewHolder(@NonNull AnnounceDataLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                if (binding.myExpandableLayout.getVisibility() == View.VISIBLE) {
                    // If the inner layout is visible, hide it to collapse
                    binding.myExpandableLayout.setVisibility(View.GONE);
                } else {
                    // If the inner layout is not visible, show it to expand
                    binding.myExpandableLayout.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
