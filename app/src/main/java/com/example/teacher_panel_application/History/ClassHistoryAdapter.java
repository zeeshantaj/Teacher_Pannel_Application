package com.example.teacher_panel_application.History;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.ClasshistoryRecyclerItemBinding;

import java.util.List;

public class ClassHistoryAdapter extends RecyclerView.Adapter<ClassHistoryAdapter.ViewHolder> {
    private List<UploadClassModel> modelList;

    public ClassHistoryAdapter(List<UploadClassModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ClassHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ClasshistoryRecyclerItemBinding binding = ClasshistoryRecyclerItemBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassHistoryAdapter.ViewHolder holder, int position) {
        UploadClassModel model = modelList.get(position);
        holder.binding.classHistoryItemDate.setText(model.getDateTime());
        holder.binding.classHistoryNametxt.setText(model.getName());
        holder.binding.classHistorySubjectTxt.setText(model.getSubject());
        holder.binding.classHistoryTopopicTxt.setText(model.getTopic());
        holder.binding.classHistoryDurationTxt.setText(model.getMinutes());
        holder.binding.classHistoryLocationTxt.setText(model.getLocation());
        holder.binding.classHistoryStartedTxt.setText(model.getDateTime());
        holder.binding.classHistoryDepartText.setText(model.getDepartment());

    }
    @Override
    public int getItemCount() {
        return modelList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        private ClasshistoryRecyclerItemBinding binding;
        public ViewHolder(@NonNull ClasshistoryRecyclerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                if (binding.expandableLayoutClassHistory.getVisibility() == View.VISIBLE) {
                    // If the inner layout is visible, hide it to collapse
                    binding.downArrowImg.setRotation(0);
                    binding.expandableLayoutClassHistory.setVisibility(View.GONE);
                } else {
                    // If the inner layout is not visible, show it to expand
                    binding.downArrowImg.setRotation(180);
                    binding.expandableLayoutClassHistory.setVisibility(View.VISIBLE);

                }
            });
        }
    }
}
