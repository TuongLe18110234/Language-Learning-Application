package com.example.languages_learning_app.DAO;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.Language;
import com.example.languages_learning_app.DTO.Lesson;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LessonDAO<changeIsActivedLanguage> {
    String path;
    DatabaseReference mDatabase;

    private static LessonDAO instance;

    public static LessonDAO getInstance() {
        if (instance==null)
            instance = new LessonDAO();
        return instance;
    }

    public static void setInstance(LessonDAO instance) {
        LessonDAO.instance = instance;
    }

    public LessonDAO(){
        path = "Lessons";
    }

    public void setLessonValue(Lesson lesson){
        mDatabase = FirebaseDatabase.getInstance().getReference(path);

        if(lesson.getId() == null) {
            String key = mDatabase.push().getKey();
            lesson.setId(key);
        }

        mDatabase.child(Common.language.getId()).child(lesson.getId()).setValue(lesson);
    }

    public boolean deleteLesson(String id) {
        try {
            mDatabase = FirebaseDatabase.getInstance().getReference(path);
            mDatabase.child(Common.language.getId()).child(id).removeValue();
            return true;
        } catch (Error error){
            return false;
        }
    }

    public void changeStatusLesson(Lesson lesson){
        mDatabase = FirebaseDatabase.getInstance().getReference(path);
        mDatabase.child(Common.language.getId()).child(lesson.getId()).child("status").setValue(!lesson.isStatus());
    }

    public void updateNumOfPractice(String lessonId, int numEasy, int numHard){
        mDatabase = FirebaseDatabase.getInstance().getReference(path);

        mDatabase.child(Common.language.getId()).child(lessonId).child("easyPracticeCount").setValue(numEasy);
        mDatabase.child(Common.language.getId()).child(lessonId).child("hardPracticeCount").setValue(numHard);
    }
}
