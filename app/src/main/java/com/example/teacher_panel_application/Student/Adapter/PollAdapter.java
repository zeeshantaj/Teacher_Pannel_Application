package com.example.teacher_panel_application.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.ViewHolder> {
    private List<PollModel> pollModelsList;
    private Context context;
    private String selectedOptionKey;

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
        // Set the text for each option and manage visibility
        holder.radioOption1.setText(model.getOption1());
        holder.radioOption2.setText(model.getOption2());

        if (model.getOption3() != null && !model.getOption3().isEmpty()) {
            holder.radioOption3.setText(model.getOption3());
            holder.radioOption3.setVisibility(View.VISIBLE);
        } else {
            holder.radioOption3.setVisibility(View.GONE);
        }

        if (model.getOption4() != null && !model.getOption4().isEmpty()) {
            holder.radioOption4.setText(model.getOption4());
            holder.radioOption4.setVisibility(View.VISIBLE);
        } else {
            holder.radioOption4.setVisibility(View.GONE);
        }

        checkIfVoteExist(model.getUid(), model.getKey(),holder);
        holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedOption = "";
            if (checkedId == R.id.option1){
                selectedOption = "option1_count";
            }
            else if (checkedId == R.id.option2) {
                selectedOption = "option2_count";
            }
            else if (checkedId == R.id.option3) {
                selectedOption = "option3_count";
            }
            else if (checkedId == R.id.option4) {
                selectedOption = "option4_count";
            }
            Toast.makeText(context, ""+model.getKey()+model.getUid(), Toast.LENGTH_SHORT).show();

            updatePollOption(model.getUid(), model.getKey(), selectedOption,holder);
        });

//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TeachersCreatedPoll")
//                .child(model.getUid())
//                .child(model.getKey());
//
//        reference.child(MethodsUtils.getCurrentUID()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    // The user has already voted
//                    Toast.makeText(context, "You have already voted!", Toast.LENGTH_SHORT).show();
//                } else {
//                    // The user hasn't voted, proceed with voting
//                    //updatePollOption(reference);
//                }
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return pollModelsList.size();
    }
    private void updatePollOption(String uid,String key, String selectedOptionCount,ViewHolder holder) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TeachersCreatedPoll")
                .child(uid)
                .child(key);

        reference.child(selectedOptionCount).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = 0;
                if (snapshot.exists()) {
                    count = snapshot.getValue(Long.class);
                }
                Toast.makeText(context, "Vote Posted", Toast.LENGTH_SHORT).show();
                reference.child(MethodsUtils.getCurrentUID()).setValue(MethodsUtils.getCurrentUID());
                reference.child(selectedOptionCount).setValue(count + 1);
                disableVotingOptions(holder);

                //showResults(holder,reference);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to update poll", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkIfVoteExist(String uid,String key,ViewHolder holder) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TeachersCreatedPoll")
                .child(uid)
                .child(key);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child(MethodsUtils.getCurrentUID()).exists()){
                        disableVotingOptions(holder);
                        String op1 = snapshot.child("option0").getValue(String.class);
                        String op2 = snapshot.child("option1").getValue(String.class);
                        String op3 = snapshot.child("option2").getValue(String.class);
                        String op4 = snapshot.child("option3").getValue(String.class);

                        Long count = snapshot.child("option1_count").getValue(Long.class);
                        Long count1 = snapshot.child("option2_count").getValue(Long.class);
                        Long count2 = snapshot.child("option3_count").getValue(Long.class);
                        Long count3 = snapshot.child("option4_count").getValue(Long.class);


                        holder.radioOption1.setText(op1+" Votes "+count);
                        holder.radioOption2.setText(op2+" Votes "+count1);


                        if (holder.radioOption3.getVisibility() == View.VISIBLE) {
                            holder.radioOption3.setText(op3+" Votes "+count2);

                        }
                        if (holder.radioOption4.getVisibility() == View.VISIBLE) {
                            holder.radioOption4.setText(op4+" Votes "+count3);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void disableVotingOptions(PollAdapter.ViewHolder holder) {
        // Disable voting options and show results
        holder.radioOption1.setEnabled(false);
        holder.radioOption2.setEnabled(false);
        holder.radioOption3.setEnabled(false);
        holder.radioOption4.setEnabled(false);

        // Show the result counts
        //showResults(holder, reference);
    }
    private void showResults(PollAdapter.ViewHolder holder, DatabaseReference reference) {
        // Fetch the current vote counts and display them for option 1
        reference.child("option1_count").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count1 = snapshot.exists() ? snapshot.getValue(Long.class) : 0;
                holder.radioOption1.setText(holder.radioOption1.getText() + " (" + count1 + ")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // Fetch the current vote counts and display them for option 2
        reference.child("option2_count").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count2 = snapshot.exists() ? snapshot.getValue(Long.class) : 0;
                holder.radioOption2.setText(holder.radioOption2.getText() + " (" + count2 + ")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // Check if option 3 exists, if it does, fetch and display its count
        if (holder.radioOption3.getVisibility() == View.VISIBLE) {
            reference.child("option3_count").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long count3 = snapshot.exists() ? snapshot.getValue(Long.class) : 0;
                    holder.radioOption3.setText(holder.radioOption3.getText() + " (" + count3 + ")");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }

        // Check if option 4 exists, if it does, fetch and display its count
        if (holder.radioOption4.getVisibility() == View.VISIBLE) {
            reference.child("option4_count").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long count4 = snapshot.exists() ? snapshot.getValue(Long.class) : 0;
                    holder.radioOption4.setText(holder.radioOption4.getText() + " (" + count4 + ")");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }
    public class
    ViewHolder extends RecyclerView.ViewHolder {
        private TextView question;
        private RadioGroup radioGroup;
        private RadioButton radioOption1,radioOption2,radioOption3,radioOption4;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            radioOption1 = itemView.findViewById(R.id.option1);
            radioOption2 = itemView.findViewById(R.id.option2);
            radioOption3 = itemView.findViewById(R.id.option3);
            radioOption4 = itemView.findViewById(R.id.option4);

//            op2 = itemView.findViewById(R.id.tv_option2);
//            op3 = itemView.findViewById(R.id.tv_option3);
//            op4 = itemView.findViewById(R.id.tv_option4);
//            pr1 = itemView.findViewById(R.id.tv_percent1);
//            pr2 = itemView.findViewById(R.id.tv_percent2);
//            pr3 = itemView.findViewById(R.id.tv_percent3);
//            pr4 = itemView.findViewById(R.id.tv_percent4);
        }
    }
}
