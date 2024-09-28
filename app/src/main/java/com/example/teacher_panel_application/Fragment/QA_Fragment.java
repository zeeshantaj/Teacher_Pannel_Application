package com.example.teacher_panel_application.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.teacher_panel_application.Adapters.QAAdapter;
import com.example.teacher_panel_application.Models.PDFModel;
import com.example.teacher_panel_application.Models.QAModel;
import com.example.teacher_panel_application.R;
import com.example.teacher_panel_application.Utils.MethodsUtils;
import com.example.teacher_panel_application.databinding.FragmentQABinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class QA_Fragment extends Fragment {

    public QA_Fragment() {
    }
    private FragmentQABinding binding;
    private QAAdapter adapter;
    private List<QAModel> modelList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQABinding.inflate(inflater,container,false);


        return binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();

        modelList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Students_Posted_Questions");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            QAModel model = dataSnapshot1.getValue(QAModel.class);
                            binding.noquestionPostedTxt.setVisibility(View.GONE);

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginType", Context.MODE_PRIVATE);
                            boolean isTrue = sharedPreferences.getBoolean("typeBool",false);
                            if (isTrue){
                                // for students
                                if (MethodsUtils.getString(getContext(),"studentSemester").equals(model.getSemester())
                                        && MethodsUtils.getString(getContext(),"studentMajor").equals(model.getMajor())
                                        && MethodsUtils.getString(getContext(),"studentYear").equals(model.getYear())){
                                    modelList.add(model);
                                }
                            }else {
                                // for teachers
                                modelList.add(model);
                            }


                        }
                    }
                    Collections.reverse(modelList);


                    adapter = new QAAdapter(getContext(), modelList, new QAAdapter.OnItemClick() {
                        @Override
                        public void onCLick(String key) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.delete_item_layout, null);
                    builder.setView(dialogView);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    MaterialButton noBtn = dialogView.findViewById(R.id.noBtn);
                    MaterialButton yesBtn = dialogView.findViewById(R.id.deleteBtn);
                    yesBtn.setOnClickListener(v1 -> {
                        deleteNode(MethodsUtils.getCurrentUID(),key);
                        dialog.dismiss();
                    });
                    noBtn.setOnClickListener(v1 -> {
                        dialog.dismiss();
                    });
                        }
                    });
                    binding.QARecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.QARecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else {
                    binding.noquestionPostedTxt.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void deleteNode(String uid,String key){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Students_Posted_Questions").child(uid).child(key);
        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                for (int i = 0; i < modelList.size(); i++) {
                    if (modelList.get(i).getKey().equals(key)) {
                        modelList.remove(i); // Remove the item from the list
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
                MethodsUtils.showSuccessDialog(getContext(),"Success","Deleted Successfully", SweetAlertDialog.SUCCESS_TYPE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                MethodsUtils.showSuccessDialog(getContext(),"Error","Failed to delete", SweetAlertDialog.ERROR_TYPE);
            }
        });
    }
}
