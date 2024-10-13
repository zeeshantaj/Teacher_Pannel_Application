package com.example.teacher_panel_application.VideoStreaming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.teacher_panel_application.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddTitleDialog extends DialogFragment {

    public AddTitleDialog() {
    }
    OnTitleListener onTitleListener;
    public void setListener(OnTitleListener listener){
        onTitleListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.title_dialog,container,false);
        setCancelable(false);

        MaterialButton addBtn = view.findViewById(R.id.addTitleBtn);
        MaterialButton cancel = view.findViewById(R.id.cancelBtn);
        TextInputEditText addTitleEdt = view.findViewById(R.id.addTitle);

        addBtn.setOnClickListener(view1 -> {
            if (addTitleEdt.getText().toString().isEmpty()){
                Toast.makeText(getContext(), "Title can not be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            onTitleListener.onSuccess(addTitleEdt.getText().toString());
        });
        cancel.setOnClickListener(view1 -> {
            onTitleListener.onFailed();
        });

        return view;
    }
}
