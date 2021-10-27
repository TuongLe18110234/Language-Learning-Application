package com.example.languages_learning_app.Views.Trainee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languages_learning_app.Adapters.Trainee.ScorePracticeAdapter;
import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.Lesson;
import com.example.languages_learning_app.DTO.Score;
import com.example.languages_learning_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class TraineePracticeFragment extends Fragment {
    RecyclerView recyclerView;
    ScorePracticeAdapter adapter;
    DatabaseReference mDatabase;

    ArrayList<Score> scores;

    private ScorePracticeAdapter.RecyclerViewClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trainee_practice, container, false);

        setOnClickListener();
        setRecyclerView(root);

        return root;
    }

    private void setOnClickListener() {
        listener = new ScorePracticeAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                int PRACTICE_TYPE = Common.PRACTICE_TYPE_EASY;
                switch (v.getId()) {
                    case R.id.tvEasyPractice:
                        PRACTICE_TYPE = Common.PRACTICE_TYPE_EASY;
                        break;
                    case R.id.tvHardPractice:
                        PRACTICE_TYPE = Common.PRACTICE_TYPE_HARD;
                        break;
                }

                Intent intent = new Intent(getActivity(), TraineePracticeActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("score", (Serializable)scores.get(position));
                intent.putExtras(bundle);
                intent.putExtra("PRACTICE_TYPE", PRACTICE_TYPE);

                startActivity(intent);
            }
        };
    }

    private void setRecyclerView(View root){
        recyclerView = root.findViewById(R.id.rvScorePractice);

        scores = new ArrayList<>();
        getListLesson();

        adapter = new ScorePracticeAdapter(getContext(), scores, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);
    }

    private void getListScore(){
        // Get data from firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Scores");
        Query query =  mDatabase.child(Common.language.getId()).orderByChild("traineeId").equalTo(Common.user.getUserId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Score score = dataSnapshot.getValue(Score.class);

                    for (int i=0;i<scores.size();i++){
                        if(scores.get(i).getLessonId().equals(score.getLessonId())){
                            scores.set(i, score);
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

    private void getListLesson(){
        // Get data from firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Lessons");
        mDatabase.child(Common.language.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scores.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Lesson lesson = dataSnapshot.getValue(Lesson.class);

                    Score score = new Score(Common.user.getUserId(), lesson.getId());
                    score.setLessonName(lesson.getName());
                    score.setPracticeEasyPercentile(0);
                    score.setPracticeHardPercentile(0);
                    scores.add(score);
                }
                getListScore();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
