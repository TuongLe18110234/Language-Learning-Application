package com.example.languages_learning_app.DAO;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.Lesson;
import com.example.languages_learning_app.DTO.Practice;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PracticeDAO<changeIsActivedLanguage> {
    String path;
    DatabaseReference mDatabase;

    private static PracticeDAO instance;

    public static PracticeDAO getInstance() {
        if (instance==null)
            instance = new PracticeDAO();
        return instance;
    }

    public static void setInstance(PracticeDAO instance) {
        PracticeDAO.instance = instance;
    }

    public PracticeDAO(){
        path = "Practices";
    }

    public void setValue(Practice practice){
        mDatabase = FirebaseDatabase.getInstance().getReference(path);

        if(practice.getId() == null) {
            String key = mDatabase.push().getKey();
            practice.setId(key);
        }
        mDatabase.child(Common.language.getId()).child(practice.getId()).setValue(practice);
    }

    public boolean delete(String id) {
        try {
            mDatabase = FirebaseDatabase.getInstance().getReference(path);
            mDatabase.child(Common.language.getId()).child(id).removeValue();
            return true;
        } catch (Error error){
            return false;
        }
    }

    public void changeStatus(Practice practice){
        mDatabase = FirebaseDatabase.getInstance().getReference(path);
        mDatabase.child(Common.language.getId()).child(practice.getId()).child("status").setValue(!practice.getStatus());
    }
}
