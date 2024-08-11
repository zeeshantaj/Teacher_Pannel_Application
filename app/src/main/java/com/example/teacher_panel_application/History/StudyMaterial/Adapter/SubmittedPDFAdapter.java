package com.example.teacher_panel_application.History.StudyMaterial.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubmittedPDFAdapter extends RecyclerView.Adapter<SubmittedPDFAdapter.ViewHolder> {
    private Context context;
    private List<SubmittedModel> modelList;

    public SubmittedPDFAdapter(Context context, List<SubmittedModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public SubmittedPDFAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.submitted_pdf_users_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubmittedPDFAdapter.ViewHolder holder, int position) {
        SubmittedModel model = modelList.get(position);
        holder.name.setText(model.getUserName());
        holder.date.setText("Submitted Date: "+model.getDateTime());
        Glide.with(context)
                .load(model.getImgUrl())
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
