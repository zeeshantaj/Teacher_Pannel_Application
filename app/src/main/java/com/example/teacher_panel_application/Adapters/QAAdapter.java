package com.example.teacher_panel_application.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.Home.PostAnswerActivity;
import com.example.teacher_panel_application.Models.QAModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.text.UStringsKt;

public class QAAdapter extends RecyclerView.Adapter<QAAdapter.ViewHolder> {
    private Context context;
    private List<QAModel> modelList;
    private int currentIndex = 0;
    private Handler handler = new Handler();
    private String text = "Waiting for Answer...";
    private String text1 = "Write Answer Here!";
    private OnItemClick listener;

    public interface OnItemClick{
        void onCLick(String key);
    }

    public QAAdapter(Context context, List<QAModel> modelList, OnItemClick listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;

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




        SharedPreferences sharedPreferences = context.getSharedPreferences("loginType", Context.MODE_PRIVATE);
        boolean isTrue = sharedPreferences.getBoolean("typeBool",false);
        if (!isTrue){
            // for teachers
            if (!model.getAnswer().isEmpty()) {
                holder.answer.setText(model.getAnswer());

                String uid = MethodsUtils.getCurrentUID();
                if (uid.equals(model.getTeacherUid())){
                    holder.ansOptionImg.setVisibility(View.VISIBLE);

                    holder.ansOptionImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View dialogView = LayoutInflater.from(context).inflate(R.layout.delete_item_layout, null);
                            builder.setView(dialogView);

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            MaterialButton noBtn = dialogView.findViewById(R.id.noBtn);
                            MaterialButton yesBtn = dialogView.findViewById(R.id.deleteBtn);
                            yesBtn.setOnClickListener(v1 -> {
                                DatabaseReference reference = FirebaseDatabase.getInstance()
                                        .getReference("Students_Posted_Questions")
                                        .child(model.getStudentUid())
                                        .child(model.getKey());
                                HashMap<String ,Object > values = new HashMap<>();
                                values.put("teacherUid", "");
                                values.put("answer", "");
                                values.put("teacherName", "");
                                values.put("answerDate", "");
                                reference.updateChildren(values).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        MethodsUtils.showSuccessDialog(context, "Success", "Answer Deleted Successfully", SweetAlertDialog.SUCCESS_TYPE);
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        MethodsUtils.showSuccessDialog(context, "Error ", "Failed to delete Answer", SweetAlertDialog.SUCCESS_TYPE);

                                    }
                                });

                            });
                            noBtn.setOnClickListener(v1 -> {
                                dialog.dismiss();
                            });


                        }
                    });

                }else {
                    holder.ansOptionImg.setVisibility(View.GONE);
                }



            }else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (currentIndex <= text1.length()) {
                            String displayedText = text1.substring(0, currentIndex);
                            holder.answer.setText(displayedText);
                            currentIndex++;
                            handler.postDelayed(this, 100);
                        }
                    }
                },100);
                holder.answerCard.setOnClickListener(v -> {
                    Intent intent = new Intent(context,PostAnswerActivity.class);
                    intent.putExtra("uid",model.getStudentUid());
                    intent.putExtra("key",model.getKey());
                    intent.putExtra("question",model.getQuestion());
                    context.startActivity(intent);
                });

            }

        }else {
            // for students

            if (!model.getAnswer().isEmpty()){
                holder.answer.setText(model.getAnswer());
            }else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (currentIndex <= text.length()) {
                            String displayedText = text.substring(0, currentIndex);
                            holder.answer.setText(displayedText);
                            currentIndex++;
                            handler.postDelayed(this, 100);
                        }
                    }
                },100);
            }
            String uid = MethodsUtils.getCurrentUID();
            if (uid.equals(model.getStudentUid())){
                holder.optionImg.setVisibility(View.VISIBLE);
                holder.optionImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onCLick(model.getKey());
                    }
                });
            }
            else {
                holder.optionImg.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView question,answer;
        private CardView answerCard;
        private ImageView optionImg,ansOptionImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.asnwer);
            answerCard = itemView.findViewById(R.id.cardView1);
            optionImg = itemView.findViewById(R.id.imageView8);
            ansOptionImg = itemView.findViewById(R.id.imageView9);
        }
    }

}
