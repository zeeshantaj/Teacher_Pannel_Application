package com.example.teacher_panel_application.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Student.PollModel;

import java.util.List;

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.ViewHolder> {
    private List<PollModel> pollModelsList;
    private Context context;

    public PollAdapter(List<PollModel> pollModelsList, Context context) {
        this.pollModelsList = pollModelsList;
        this.context = context;
    }

    @NonNull
    @Override
    public PollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_recycler_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PollAdapter.ViewHolder holder, int position) {
        PollModel model = pollModelsList.get(position);
        holder.question.setText(model.getQuestion());
        holder.op1.setText(model.getOption1());
        holder.op2.setText(model.getOption2());
        holder.op3.setText(model.getOption3());
        holder.op4.setText(model.getOption4());

        if (model.getOption3()  != null &&  !model.getOption3().isEmpty() ) {
            holder.op3.setVisibility(View.VISIBLE);
            holder.seek_bar3.setVisibility(View.VISIBLE);
        }else {
            holder.op3.setVisibility(View.GONE);
            holder.seek_bar3.setVisibility(View.GONE);
        }
        if (model.getOption4() != null  &&  !model.getOption4().isEmpty()){
            holder.op4.setVisibility(View.VISIBLE);
            holder.seek_bar4.setVisibility(View.VISIBLE);
        }else {
            holder.op4.setVisibility(View.GONE);
            holder.seek_bar4.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return pollModelsList.size();
    }

    public class
    ViewHolder extends RecyclerView.ViewHolder {
        private TextView question,op1,op2,op3,op4,pr1,pr2,pr3,pr4;
        private SeekBar seek_bar4,seek_bar1,seek_bar2,seek_bar3;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.tv_question);
            op1 = itemView.findViewById(R.id.tv_option1);
            op2 = itemView.findViewById(R.id.tv_option2);
            op3 = itemView.findViewById(R.id.tv_option3);
            op4 = itemView.findViewById(R.id.tv_option4);
            pr1 = itemView.findViewById(R.id.tv_percent1);
            pr2 = itemView.findViewById(R.id.tv_percent2);
            pr3 = itemView.findViewById(R.id.tv_percent3);
            pr4 = itemView.findViewById(R.id.tv_percent4);
            seek_bar4 = itemView.findViewById(R.id.seek_bar4);
            seek_bar1 = itemView.findViewById(R.id.seek_bar1);
            seek_bar2 = itemView.findViewById(R.id.seek_bar2);
            seek_bar3 = itemView.findViewById(R.id.seek_bar3);

        }
    }
}