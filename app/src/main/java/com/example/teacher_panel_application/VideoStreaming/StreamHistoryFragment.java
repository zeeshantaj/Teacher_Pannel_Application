package com.example.teacher_panel_application.VideoStreaming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StreamHistoryFragment extends Fragment {

    public StreamHistoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stream_history_fragment,container,false);

        RecyclerView recyclerView = view.findViewById(R.id.streamHistoryRecycler);

        List<StreamModel> modelList = new ArrayList<>();


        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("videoStreaming")
                .child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        StreamModel model = dataSnapshot.getValue(StreamModel.class);
                        if (model != null) {
                            modelList.add(model);

                            DataSnapshot joinedUsersSnapshot = dataSnapshot.child("joinedUsers");

                            if (joinedUsersSnapshot.exists()) {
                                ArrayList<String> joinedUsersList = new ArrayList<>();

                                if (joinedUsersSnapshot.getValue() instanceof List) {
                                    // If joinedUsers is a list
                                    List<String> users = (List<String>) joinedUsersSnapshot.getValue();
                                    joinedUsersList.addAll(users);
                                } else if (joinedUsersSnapshot.getValue() instanceof Map) {
                                    // If joinedUsers is a map
                                    Map<String, Object> usersMap = (Map<String, Object>) joinedUsersSnapshot.getValue();
                                    for (String key : usersMap.keySet()) {
                                        joinedUsersList.add(key);  // Or add the value if you need the values
                                    }
                                }

                                model.setJoinedUsersList(joinedUsersList);  // Assuming setJoinedUsersList() exists in StreamModel
                            }
                        }
                    }
                    // Set up the adapter and RecyclerView after data retrieval
                    StreamingHistoryAdapter adapter = new StreamingHistoryAdapter(getContext(), modelList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }
}
