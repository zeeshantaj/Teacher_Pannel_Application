package com.example.teacher_panel_application.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.Models.QAModel;
import com.example.teacher_panel_application.R;

import java.util.List;

public class QAAdapter extends RecyclerView.Adapter<QAAdapter.ViewHolder> {
    private Context context;
    private List<QAModel> modelList;

    public QAAdapter(Context context, List<QAModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public QAAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.qa_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QAAdapter.ViewHolder holder, int position) {
        QAModel model = modelList.get(position);
        holder.question.setText(model.getQuestion());
        holder.answer.setText(model.getAnswer());



    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView question,answer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.asnwer);
        }
    }
}
