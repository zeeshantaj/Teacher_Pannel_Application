package com.example.teacher_panel_application.VideoStreaming;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.R;

import java.util.List;

public class ReceiveVideoStreamingAdapter extends RecyclerView.Adapter<ReceiveVideoStreamingAdapter.ViewHolder> {
    private Context context;
    private List<StreamModel> modelList;

    public ReceiveVideoStreamingAdapter(Context context, List<StreamModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ReceiveVideoStreamingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiveVideoStreamingAdapter.ViewHolder holder, int position) {
        StreamModel model = modelList.get(position);

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
