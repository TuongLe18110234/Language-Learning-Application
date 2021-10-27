package com.example.languages_learning_app.Views.Trainee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.Lesson;
import com.example.languages_learning_app.DTO.Vocabulary;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TraineeVocabPagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;

    private List<Vocabulary> vocabularies = new ArrayList<>();
    private ArrayList<String> listVocabs;
    private Lesson lesson;

    private DatabaseReference mDataBase;

    public TraineeVocabPagerAdapter(@NonNull FragmentManager fm, int numberOfTabs, Lesson lesson, ArrayList<String> listVocabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        this.listVocabs = listVocabs;
        this.lesson = lesson;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Get data from firebase
        // Data is list vocabularies in current lesson
        mDataBase = FirebaseDatabase.getInstance().getReference("Vocabularies").child(Common.language.getId());
        Query query = mDataBase.orderByChild("lessonId").equalTo(lesson.getId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vocabularies.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Vocabulary vocabulary = dataSnapshot.getValue(Vocabulary.class);
                    if(vocabulary.isActive()) {
                        vocabularies.add(vocabulary);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Bundle bundle = new Bundle();
        bundle.putSerializable("ListVocabs", (Serializable) vocabularies);
        bundle.putStringArrayList("LearnedList", listVocabs);
        bundle.putSerializable("Lesson", lesson);
        switch (position) {
            case 0:
                TraineeVocabFlashcardFragment flashcardFragment = new TraineeVocabFlashcardFragment();
                flashcardFragment.setArguments(bundle);
                return flashcardFragment;
            case 1:
                TraineeVocabLearningFragment learningFragment = new TraineeVocabLearningFragment();
                learningFragment.setArguments(bundle);
                return learningFragment;
            case 2:
                TraineeVocabLearnedFragment learnedFragment = new TraineeVocabLearnedFragment();
                learnedFragment.setArguments(bundle);
                return learnedFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}

