package com.example.languages_learning_app.DAO;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.Practice;
import com.example.languages_learning_app.DTO.Score;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScoreDAO<changeIsActivedLanguage> {
    String path;
    DatabaseReference mDatabase;

    private static ScoreDAO instance;

    public static ScoreDAO getInstance() {
        if (instance==null)
            instance = new ScoreDAO();
        return instance;
    }

    public static void setInstance(ScoreDAO instance) {
        ScoreDAO.instance = instance;
    }

    public ScoreDAO(){
        path = "Scores";
    }

    public void setValue(Score score){
        mDatabase = FirebaseDatabase.getInstance().getReference(path);

        if(score.getId() == null) {
            String key = mDatabase.push().getKey();
            score.setId(key);
        }
        mDatabase.child(Common.language.getId()).child(score.getId()).setValue(score);
    }

    public void setFinalScore(Score score){
        mDatabase = FirebaseDatabase.getInstance().getReference("FinalScores");

        mDatabase.child(Common.language.getId()).child(score.getId()).setValue(score);
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
}
