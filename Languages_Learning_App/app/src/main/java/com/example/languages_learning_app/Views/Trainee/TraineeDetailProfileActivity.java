package com.example.languages_learning_app.Views.Trainee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.Score;
import com.example.languages_learning_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.languages_learning_app.R;

public class TraineeDetailProfileActivity extends AppCompatActivity {

    DatabaseReference mDatabase;

    CircleImageView civProfile;
    TextView tvFullName, tvEmail, tvPhoneNumber, tvWritingCount, tvWriting,
            tvSelectionCount, tvSelection, tvAudioCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_detail_profile);

        mapAndSetView();
        getFinalScore();
    }

    private void mapAndSetView(){
        civProfile = findViewById(R.id.imgProfile);
        int imageLanguage = Common.getFlagLanguage(Common.language.getName());
        civProfile.setImageResource(imageLanguage);

        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvWritingCount = findViewById(R.id.tvWritingCount);
        tvWriting = findViewById(R.id.tvWriting);
        tvSelectionCount = findViewById(R.id.tvSelectionCount);
        tvSelection = findViewById(R.id.tvSelection);
        tvAudioCount = findViewById(R.id.tvAudioCount);

        tvFullName.setText(Common.user.getFullName());
        tvEmail.setText(Common.user.getEmail());
        tvPhoneNumber.setText(Common.user.getPhone());

        tvWriting.setText("Việt - " + Common.language.getBriefName());
        tvWriting.setText(Common.language.getBriefName() + " - Việt");
    }

    private void getFinalScore(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("FinalScores").child(Common.language.getId()).child(Common.user.getUserId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    Score score = task.getResult().getValue(Score.class);

                    if(score != null) {
                        tvWritingCount.setText(String.valueOf(score.getWritingScore()));
                        tvSelectionCount.setText(String.valueOf(score.getSelectionScore()));
                        tvAudioCount.setText(String.valueOf(score.getAudioScore()));
                    }
                }
            }
        });
    }
}