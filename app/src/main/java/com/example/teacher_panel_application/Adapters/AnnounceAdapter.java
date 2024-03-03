package com.example.teacher_panel_application.Adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.Models.AnnouncementModel;
import com.example.teacher_panel_application.R;
import com.github.chrisbanes.photoview.PhotoView;

import org.w3c.dom.Text;

import java.util.List;

public class AnnounceAdapter extends RecyclerView.Adapter<AnnounceAdapter.ViewHolder> {

    List<AnnouncementModel> announcementModelList;

    public AnnounceAdapter(List<AnnouncementModel> announcementModelList) {
        this.announcementModelList = announcementModelList;
    }

    @NonNull
    @Override
    public AnnounceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announce_data_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnounceAdapter.ViewHolder holder, int position) {

        AnnouncementModel model = announcementModelList.get(position);

        holder.date.setText(model.getCurrent_date());
        holder.title.setText(model.getTitle());
       // holder.title.setVisibility(View.VISIBLE);
        holder.description.setText(model.getDescription());
        //holder.description.setVisibility(View.VISIBLE);
        holder.dueDate.setText(model.getDue_date());
        holder.dueDate.setVisibility(View.VISIBLE);
        //holder.date.setVisibility(View.VISIBLE);

        holder.image.setVisibility(View.VISIBLE);
        Glide.with(holder.itemView.getContext())
                .load(model.getImageUrl())
                .into(holder.image);
        Log.e("MyApp","itemImageUrl"+model.getImageUrl());
            //        if (!model.getTitle().isEmpty()){
//            holder.title.setText(model.getTitle());
//            holder.title.setVisibility(View.VISIBLE);
//            holder.description.setText(model.getDescription());
//            holder.description.setVisibility(View.VISIBLE);
//            holder.dueDate.setText(model.getDue_date());
//            holder.date.setVisibility(View.VISIBLE);
//        }
//        if (model.getImageUrl() != null){

//        }

//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (holder.expandableView.getVisibility() == View.VISIBLE) {
//                    // If the inner layout is visible, hide it to collapse
//                    holder.expandableView.setVisibility(View.GONE);
//                } else {
//                    // If the inner layout is not visible, show it to expand
//                    holder.expandableView.setVisibility(View.VISIBLE);
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return announcementModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView title,description,date,dueDate;
        private PhotoView image;
        private CardView cardView;
        private ConstraintLayout expandableView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.itemTItle);
            date = itemView.findViewById(R.id.itemDate);
            dueDate = itemView.findViewById(R.id.itemDueDate);
            description = itemView.findViewById(R.id.itemDes);
            image = itemView.findViewById(R.id.itemImage);
            cardView = itemView.findViewById(R.id.card);
            expandableView = itemView.findViewById(R.id.myExpandableLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandableView.getVisibility() == View.VISIBLE) {
                        // If the inner layout is visible, hide it to collapse
                        expandableView.setVisibility(View.GONE);
                    } else {
                        // If the inner layout is not visible, show it to expand
                        expandableView.setVisibility(View.VISIBLE);
//                        title.setVisibility(View.VISIBLE);
//                        //dueDate.setVisibility(View.VISIBLE);
//                        description.setVisibility(View.VISIBLE);
//                        image.setVisibility(View.VISIBLE);

                    }
                }
            });

        }
    }
}
