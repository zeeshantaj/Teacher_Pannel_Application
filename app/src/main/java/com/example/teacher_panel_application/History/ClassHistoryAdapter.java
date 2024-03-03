package com.example.teacher_panel_application.History;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classhistory_recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassHistoryAdapter.ViewHolder holder, int position) {
        UploadClassModel model = modelList.get(position);
        holder.itemDate.setText(model.getCurrentTime());
        holder.name.setText(model.getName());
        holder.sub.setText(model.getSubject());
        holder.topic.setText(model.getTopic());
        holder.dur.setText(model.getMinutes());
        holder.loc.setText(model.getLocation());
        holder.startedAt.setText(model.getCurrentTime());
        holder.dep.setText(model.getDepartment());

    }
    @Override
    public int getItemCount() {
        return modelList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name,sub,topic,dur,loc,startedAt,dep,itemDate;
        private ConstraintLayout expandableView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.classHistory_nametxt);
            sub = itemView.findViewById(R.id.classHistory_subjectTxt);
            topic = itemView.findViewById(R.id.classHistory_topopicTxt);
            dur = itemView.findViewById(R.id.classHistory_durationTxt);
            loc = itemView.findViewById(R.id.classHistory_locationTxt);
            startedAt = itemView.findViewById(R.id.classHistory_startedTxt);
            dep = itemView.findViewById(R.id.classHistory_departText);
            itemDate = itemView.findViewById(R.id.classHistory_itemDate);
            expandableView = itemView.findViewById(R.id.expandableLayoutClassHistory);

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
