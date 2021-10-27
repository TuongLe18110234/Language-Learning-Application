package com.example.languages_learning_app.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.languages_learning_app.DTO.YouTubeVideo;
import com.example.languages_learning_app.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class GeneralPlayVideoActivity extends AppCompatActivity {

    private TextView tvVideoName, tvSinger, tvLyric;
    private YouTubePlayerView youTubePlayer;
    private YouTubeVideo video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_play_video);

        // Get data from intent (data is current lesson)
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            video = (YouTubeVideo) bundle.getSerializable("video");
        }

        // Mapping
        tvVideoName = findViewById(R.id.txtVideoName);
        tvSinger = findViewById(R.id.txtSinger);
        tvLyric = findViewById(R.id.txtLyric);

        tvVideoName.setText(video.getSongName());
        tvSinger.setText(video.getSinger());
        tvLyric.setText(video.getLyric());

        youTubePlayer= findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayer);

        youTubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);

                String videoId = video.getVideoId();
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
    }
}