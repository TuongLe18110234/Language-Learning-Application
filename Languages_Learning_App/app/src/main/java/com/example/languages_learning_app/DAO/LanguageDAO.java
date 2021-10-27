package com.example.languages_learning_app.DAO;

import androidx.annotation.NonNull;

import com.example.languages_learning_app.DTO.Language;
import com.example.languages_learning_app.DTO.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LanguageDAO<changeIsActivedLanguage> {
    String path;
    DatabaseReference mDatabase;

    private static LanguageDAO instance;

    public static LanguageDAO getInstance() {
        if (instance==null)
            instance = new LanguageDAO();
        return instance;
    }

    public static void setInstance(LanguageDAO instance) {
        LanguageDAO.instance = instance;
    }

    public LanguageDAO(){
        path = "Languages";
    }

    public void setLanguageValue(Language language){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String key = mDatabase.push().getKey();
        language.setId(key);

        mDatabase.child(path).child(String.valueOf(language.getId())).setValue(language);
    }

    public boolean deleteLanguage(String id) {
        try {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(path).child(id).removeValue();
            return true;
        } catch (Error error){
            return false;
        }
    }

    public void changeStatusLanguage(Language language){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(path).child(String.valueOf(language.getId())).child("status").setValue(!language.isStatus());
    }
}
