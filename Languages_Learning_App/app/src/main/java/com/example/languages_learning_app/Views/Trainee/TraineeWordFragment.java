package com.example.languages_learning_app.Views.Trainee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languages_learning_app.Adapters.LessonVocabAdapter;
import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.Lesson;
import com.example.languages_learning_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TraineeWordFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Lesson> listLesson;
    LessonVocabAdapter adapter;
    DatabaseReference mDatabase;

    private LessonVocabAdapter.RecyclerViewClickListener listener;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_trainee_flascard_lesson, container, false);

        setToolbarWithoutBack(root);
        setOnClickListener();
        setRecyclerView(root);

        return root;
    }

    private void setOnClickListener() {
        listener = new LessonVocabAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Lesson lesson = listLesson.get(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("lesson", lesson);

                Intent intent = new Intent(getActivity(), TraineeVocabularyActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }

    /**
     * Set layout manager, adapter for recycler view
     * @param root: this fragment
     */
    private void setRecyclerView(View root){
        recyclerView = root.findViewById(R.id.rvLesson);
        listLesson = new ArrayList<>();
        // Mode 0 is manager mode
        adapter = new LessonVocabAdapter(getContext(), listLesson, 1, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);

        // Get data from firebase
        // Data is list lessons
        mDatabase = FirebaseDatabase.getInstance().getReference("Lessons");
        mDatabase.child(Common.language.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLesson.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Lesson lesson = dataSnapshot.getValue(Lesson.class);
                    listLesson.add(lesson);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Toolbar on the top of screen
    private void setToolbarWithoutBack(View root) {
        // Set name for activity
        TextView txtToolbarName = root.findViewById(R.id.activity_name);
        txtToolbarName.setText("Chọn bài học");
    }
}