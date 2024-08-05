package com.example.teacher_panel_application.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.R;

import java.util.List;

public class VideoLinkAdapter extends RecyclerView.Adapter<VideoLinkAdapter.ViewHolder> {
    private List<String> modelList;
    private Context context;

    public VideoLinkAdapter(List<String> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoLinkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_link_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoLinkAdapter.ViewHolder holder, int position) {
        String s = modelList.get(position);
        holder.linkTxt.setText(s);
    }
    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView linkTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linkTxt = itemView.findViewById(R.id.videoLink);
        }
    }
}
