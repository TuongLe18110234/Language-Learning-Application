package com.example.languages_learning_app.DAO;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.Language;
import com.example.languages_learning_app.DTO.Music;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MusicDAO {
    String path_song, path_cover;
    DatabaseReference mDatabase;

    private static MusicDAO instance;

    public static MusicDAO getInstance() {
        if (instance==null)
            instance = new MusicDAO();
        return instance;
    }

    public static void setInstance(MusicDAO instance) {
        MusicDAO.instance = instance;
    }

    public MusicDAO(){
        path_song = "Language_Lesson/" + Common.language.getId() +"/music/song";
        path_cover = "Language_Lesson/" + Common.language.getId() +"/music/cover";
    }


    public void setMusicValue(Music music){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(path_cover).child(String.valueOf(music.getMusicId())).setValue(music);
    }
    public void setMusicValueSong(Music music){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(path_song).child(String.valueOf(music.getMusicId())).setValue(music);
    }

}
