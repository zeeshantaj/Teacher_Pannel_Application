package com.example.teacher_panel_application.VideoStreaming;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.teacher_panel_application.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StreamDetailsActivity extends AppCompatActivity {
    List<AttendeesModel> modelList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_details);

        TextView Title = findViewById(R.id.Title);
        TextView time = findViewById(R.id.userTime);
        ImageView imageView = findViewById(R.id.userImg);


        Intent intent = getIntent();
        String timeStr = intent.getStringExtra("liveId");
        String titleStr = intent.getStringExtra("title");
        String nameStr = intent.getStringExtra("name");
        String image = intent.getStringExtra("image");

        time.setText(timeStr);
        Title.setText(titleStr);
        Glide.with(this)
                .load(image)
                .into(imageView);

        ArrayList<String> joinedUsersList = intent.getStringArrayListExtra("joinedUsersList");
        if (joinedUsersList != null) {
            for (String user : joinedUsersList) {
                Log.d("User", "Joined User: " + user);
                getUserInfo(user);


            }
        }
    }
    private void getUserInfo(String uid){

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("StudentsInfo")
                .child(uid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
//                    modelList.clear(); // Clear the list to avoid duplicate entries on data change
                    String name = snapshot.child("name").getValue(String.class);
                    String image = snapshot.child("image").getValue(String.class);

                    AttendeesModel model = new AttendeesModel();

                    model.setName(name);
                    model.setImage(image);
                    modelList.add(model);
                    // Set up the adapter and RecyclerView after populating the list
                    AttendeesAdapter adapter = new AttendeesAdapter(StreamDetailsActivity.this, modelList);
                    RecyclerView recyclerView = findViewById(R.id.attendeesRv);
                    recyclerView.setLayoutManager(new LinearLayoutManager(StreamDetailsActivity.this));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
                Toast.makeText(StreamDetailsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}