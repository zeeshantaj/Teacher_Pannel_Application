package com.example.teacher_panel_application.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Student.PollModel;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

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
        // Handle the selected option and increment the counter
        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedOptionKey = "";
                String countKey = "";

                if (view.getId() == R.id.tv_option1){
                    selectedOptionKey = "option0";
                    countKey = "option0_count";
                }else if (view.getId() == R.id.tv_option2){
                    selectedOptionKey = "option1";
                    countKey = "option1_count";
                }else if (view.getId() == R.id.tv_option3){
                    selectedOptionKey = "option2";
                    countKey = "option2_count";
                }else if (view.getId() == R.id.tv_option4){
                    selectedOptionKey = "option3";
                    countKey = "option3_count";
                }

                // Increment the selected option counter in Firebase
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TeachersCreatedPoll")
                        .child(MethodsUtils.getCurrentUID())
                        .child(model.getPollId()) // Assuming you have pollId stored in PollModel
                        .child(countKey);

                reference.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        Integer currentCount = currentData.getValue(Integer.class);
                        if (currentCount == null) {
                            currentCount = 0;
                        }
                        currentData.setValue(currentCount + 1);
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                        if (committed) {
                            MethodsUtils.showFlawDialog(holder.itemView.getContext(), R.drawable.success_png, "Success", "Your vote has been submitted", 1);
                            model.setSelectedOption(selectedOptionKey); // Update the model with the selected option
                            notifyDataSetChanged(); // Optionally update the UI
                        } else {
                            MethodsUtils.showFlawDialog(holder.itemView.getContext(), R.drawable.icon_error, "Error", "Failed to submit vote", 1);
                        }
                    }
                });
            }
        };
        holder.op1.setOnClickListener(optionClickListener);
        holder.op2.setOnClickListener(optionClickListener);
        holder.op3.setOnClickListener(optionClickListener);
        holder.op4.setOnClickListener(optionClickListener);
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
