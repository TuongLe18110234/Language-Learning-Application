package com.example.languages_learning_app.Views.Trainee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.languages_learning_app.Adapters.Trainee.ScoreRankAdapter;
import com.example.languages_learning_app.Adapters.Trainee.ScoreTestAdapter;
import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DAO.ScoreDAO;
import com.example.languages_learning_app.DTO.Lesson;
import com.example.languages_learning_app.DTO.Score;
import com.example.languages_learning_app.DTO.User;
import com.example.languages_learning_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TraineeRankActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ScoreRankAdapter adapter;
    DatabaseReference mDatabase;

    ArrayList<Score> scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_rank);

        setToolbar("Bảng xếp hạng học viên");
        setRecyclerView();

    }
    // Toolbar on the top of screen
    private void setToolbar(String name) {
        // Finish activity when clicking on back item
        ImageView backIcon = findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });

        // Set name for activity
        TextView txtToolbarName = findViewById(R.id.activity_name);
        txtToolbarName.setText(name);
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.rvRank);

        scores = new ArrayList<>();
        getListScore();

        adapter = new ScoreRankAdapter(this, scores);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);
    }

    private void getListScore(){
        // Get data from firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("FinalScores");
        mDatabase =  mDatabase.child(Common.language.getId());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scores.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Score score = dataSnapshot.getValue(Score.class);
                    scores.add(score);
                }
                Score score;
                for (int i = 0; i<scores.size()-1;i++)
                    for (int j = i; j<scores.size();j++){
                        if (scores.get(i).getTotalScore() < scores.get(j).getTotalScore())
                        {
                            score = scores.get(i);
                            scores.set(i, scores.get(j));
                            scores.set(j, score);
                        }
                    }

                getListUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getListUser(){
        // Get data from firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        Query query = mDatabase.orderByChild("role");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);

                    for (int i=0;i<scores.size();i++){
                        if (scores.get(i).getTraineeId().equals(user.getUserId())){
                            scores.get(i).setTraineeName(user.getFullName());
                            break;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}