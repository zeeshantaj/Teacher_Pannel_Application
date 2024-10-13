package com.example.teacher_panel_application.VideoStreaming;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.History.StudyMaterial.Adapter.SubmittedModel;
import com.example.teacher_panel_application.History.StudyMaterial.AssignmentActivity.AssignmentCheckingActivity;
import com.example.teacher_panel_application.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendeesAdapter extends RecyclerView.Adapter<AttendeesAdapter.ViewHolder> {
    private Context context;
    private List<AttendeesModel> modelList;

    public AttendeesAdapter(Context context, List<AttendeesModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public AttendeesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.submitted_pdf_users_layout,parent,false);
        return new AttendeesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeesAdapter.ViewHolder holder, int position) {
        AttendeesModel model = modelList.get(position);
        holder.name.setText(model.getName());
        Glide.with(context)
                .load(model.getImage())
                .into(holder.img);
    }
    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name,date;
        private CircleImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.submittedPdfUserName);
            img = itemView.findViewById(R.id.submittedPdfUserImg);
            date = itemView.findViewById(R.id.submittedPdfDate);
        }
    }
}
