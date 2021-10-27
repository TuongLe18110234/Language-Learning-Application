package com.example.languages_learning_app.DAO;

import android.util.Log;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.YouTubeVideo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class YoutubeVideoDAO {
    String path;
    DatabaseReference mDatabase;

    private static YoutubeVideoDAO instance;

    public static YoutubeVideoDAO getInstance() {
        if (instance==null)
            instance = new YoutubeVideoDAO();
        return instance;
    }

    public static void setInstance(YoutubeVideoDAO instance) {
        YoutubeVideoDAO.instance = instance;
    }

    public YoutubeVideoDAO(){
        path = "Videos";
    }

    public boolean setValue(YouTubeVideo video) {
        try {
            mDatabase = FirebaseDatabase.getInstance().getReference(path);
            mDatabase.child(Common.language.getId()).child(video.getId()).setValue(video);
            return true;
        } catch (Exception e) {
            Log.e("Set Value YoutubeVideo Exception", e.getMessage());
            return false;
        }
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
