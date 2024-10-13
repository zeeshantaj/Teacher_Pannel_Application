package com.example.teacher_panel_application.VideoStreaming;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
        View view = LayoutInflater.from(context).inflate(R.layout.stream_history_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiveVideoStreamingAdapter.ViewHolder holder, int position) {
        StreamModel model = modelList.get(position);
        holder.title.setText(model.getTitle());
        holder.time.setText(model.getTime());

        Glide.with(context)
                .load(model.getImageUrl())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context,JoinedStreamingActivity.class);
            intent.putExtra("liveId",model.getLiveId());
            intent.putExtra("userId",model.getUserId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,time;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.userName);
            time = itemView.findViewById(R.id.userTime);
            imageView = itemView.findViewById(R.id.userImg);
        }
    }
}
