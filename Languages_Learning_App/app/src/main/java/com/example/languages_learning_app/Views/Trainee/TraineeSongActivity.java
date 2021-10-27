package com.example.languages_learning_app.Views.Trainee;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languages_learning_app.Adapters.Manager.YoutubeVideoAdapter;
import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.YouTubeVideo;
import com.example.languages_learning_app.R;
import com.example.languages_learning_app.Views.GeneralPlayVideoActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.Serializable;
import java.util.ArrayList;

public class TraineeSongActivity extends AppCompatActivity {

    // Database
    DatabaseReference mDatabase;
    ArrayList<YouTubeVideo> listVideo;

    // Recycler View
    RecyclerView recyclerView;
    YoutubeVideoAdapter adapter;
    private YoutubeVideoAdapter.RecyclerViewClickListener listener;

    // Full screen dialog
    private Dialog dialog;
    private TextView tvVideoName, tvSinger, tvLyric;
    private YouTubePlayerView youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_song);

        setToolbar();
        setOnClickListener();
        setRecyclerView();
    }

    private void setOnClickListener() {
        listener = new YoutubeVideoAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                YouTubeVideo video = listVideo.get(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("video", (Serializable) video);

                Intent intent = new Intent(TraineeSongActivity.this, GeneralPlayVideoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, int position) {

            }
        };
    }

    /**
     * Set layout manager, adapter for recycler view
     */
    private void setRecyclerView(){
        recyclerView = findViewById(R.id.rvListSongs);
        listVideo = new ArrayList<>();

        adapter = new YoutubeVideoAdapter(this, listVideo, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);

        // Get data from firebase
        // Data is list lessons
        mDatabase = FirebaseDatabase.getInstance().getReference("Videos");
        mDatabase.child(Common.language.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listVideo.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    YouTubeVideo video = dataSnapshot.getValue(YouTubeVideo.class);
                    listVideo.add(video);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Toolbar on the top of screen
    private void setToolbar() {
        // Finish activity when clicking on back item
        ImageView backIcon = findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });

        // Set name for activity
        TextView txtToolbarName = findViewById(R.id.activity_name);
        txtToolbarName.setText("Học qua bài hát");
    }
}