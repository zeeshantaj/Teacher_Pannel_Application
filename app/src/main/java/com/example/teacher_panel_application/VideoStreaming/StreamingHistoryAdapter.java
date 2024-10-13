package com.example.teacher_panel_application.VideoStreaming;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
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

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class StreamingHistoryAdapter extends RecyclerView.Adapter<StreamingHistoryAdapter.ViewHolder> {
    private Context context;
    private List<StreamModel> modelList;

    public StreamingHistoryAdapter(Context context, List<StreamModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public StreamingHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stream_history_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StreamingHistoryAdapter.ViewHolder holder, int position) {
        StreamModel model = modelList.get(position);
        holder.title.setText(model.getTitle() +"\n"+model.getStreamerName());
        holder.time.setText(model.getTime());

        Glide.with(context)
                .load(model.getImageUrl())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, StreamDetailsActivity.class);
            intent.putExtra("liveId",model.getTime());
            intent.putExtra("title",model.getTitle());
            intent.putExtra("name",model.getStreamerName());
            intent.putExtra("image",model.getImageUrl());
            intent.putStringArrayListExtra("joinedUsersList", model.getJoinedUsersList());
            context.startActivity(intent);
        });
        holder.liveNow.setText("Live Ended");
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,time,liveNow;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.userName);
            time = itemView.findViewById(R.id.userTime);
            liveNow = itemView.findViewById(R.id.liveNow);
            imageView = itemView.findViewById(R.id.userImg);
            BlurView blurView = itemView.findViewById(R.id.blurView);
            float radius = 5f;

            View decorView = ((Activity) context).getWindow().getDecorView();
            ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
            Drawable windowBackground = decorView.getBackground();

            blurView.setupWith(rootView, new RenderScriptBlur(context)) // or RenderEffectBlur
                    .setFrameClearDrawable(windowBackground) // Optional
                    .setBlurRadius(radius);

            // Create a ShapeDrawable with rounded corners
            float cornerRadius = 30f;  // Adjust corner radius
            ShapeDrawable shapeDrawable = new ShapeDrawable();
            shapeDrawable.setShape(new RoundRectShape(
                    new float[] {cornerRadius, cornerRadius, cornerRadius, cornerRadius,
                            cornerRadius, cornerRadius, cornerRadius, cornerRadius},
                    null, null));
            shapeDrawable.getPaint().setColor(Color.TRANSPARENT); // Transparent background
            blurView.setBackground(shapeDrawable);
        }
    }
}
