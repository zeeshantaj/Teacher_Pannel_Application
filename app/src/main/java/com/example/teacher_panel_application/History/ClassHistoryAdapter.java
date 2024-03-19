package com.example.teacher_panel_application.History;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.teacher_panel_application.Models.UploadClassModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.databinding.ClasshistoryRecyclerItemBinding;
import com.google.android.material.button.MaterialButton;
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
        int pos = holder.getAdapterPosition();
        UploadClassModel model = modelList.get(pos);
        holder.binding.classHistoryItemDate.setText(model.getDateTime());
        holder.binding.classHistoryNametxt.setText(model.getName());
        holder.binding.classHistorySubjectTxt.setText(model.getSubject());
        holder.binding.classHistoryTopopicTxt.setText(model.getTopic());
        holder.binding.classHistoryDurationTxt.setText(model.getMinutes());
        holder.binding.classHistoryLocationTxt.setText(model.getLocation());
        holder.binding.classHistoryStartedTxt.setText(model.getDateTime());
        holder.binding.classHistoryDepartText.setText(model.getDepartment());

        holder.binding.expandableLayoutClassHistory.setOnLongClickListener(v -> {

            removeItem(pos,modelList.get(pos).getDateTime());
            return true;
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
    public void removeItem(int position, String dateTime) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("PostedData")
                .child(uid)
                .child(dateTime);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.delete_item_layout, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        MaterialButton noBtn = dialogView.findViewById(R.id.noBtn);
        MaterialButton yesBtn = dialogView.findViewById(R.id.deleteBtn);

        yesBtn.setOnClickListener(v -> reference.removeValue().addOnSuccessListener(unused -> {
            if (position >= 0 && position < modelList.size()) {
                modelList.remove(position);
                notifyItemRemoved(position);
            }
            dialog.dismiss();
            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> Toast.makeText(context, "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show()));
        noBtn.setOnClickListener(v -> dialog.dismiss());
    }
}
