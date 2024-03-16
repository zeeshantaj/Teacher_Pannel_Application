package com.example.teacher_panel_application.History;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.Login.Login_Activity;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.ClasshistoryRecyclerItemBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ClassHistoryAdapter extends RecyclerView.Adapter<ClassHistoryAdapter.ViewHolder> {
    private List<UploadClassModel> modelList;
    private Context context;

    public ClassHistoryAdapter(List<UploadClassModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
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
        holder.binding.expandableLayoutClassHistory.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                removeItem(position,modelList.get(position).getDateTime());
                return true;
            }
        });

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
    public void removeItem(int position,String dateTime){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("PostedData")
                .child(uid)
                .child(dateTime);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = getLayoutInflater().inflate(R.layout.share_and_edit_dialugue, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView buttonCancel = dialogView.findViewById(R.id.cancelBtn);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Item")
                .setMessage("Are you Sure You Want To Delete This?")
                .setPositiveButton("Yes", (dialog, which) -> reference.removeValue().addOnSuccessListener(unused -> {

                    modelList.remove(position);
                    notifyItemRemoved(position);
                    dialog.dismiss();
                }).addOnFailureListener(e -> Toast.makeText(context, "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show()))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();


    }
}
