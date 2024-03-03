package com.example.teacher_panel_application.History;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.R;

import java.util.List;

public class ClassHistoryAdapter extends RecyclerView.Adapter<ClassHistoryAdapter.ViewHolder> {
    private List<UploadClassModel> modelList;

    public ClassHistoryAdapter(List<UploadClassModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ClassHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classHistoryRecyclerItem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassHistoryAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
