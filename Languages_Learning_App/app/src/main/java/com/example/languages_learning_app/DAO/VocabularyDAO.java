package com.example.languages_learning_app.DAO;

import android.util.Log;

import com.example.languages_learning_app.APIs.DictionaryApiService;
import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.DictionaryApi.Dictionary;
import com.example.languages_learning_app.DTO.DictionaryApi.Phonetic;
import com.example.languages_learning_app.DTO.Vocabulary;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VocabularyDAO<changeIsActivedLanguage> {
    String path;
    DatabaseReference mDatabase;

    private static VocabularyDAO instance;

    public static VocabularyDAO getInstance() {
        if (instance==null)
            instance = new VocabularyDAO();
        return instance;
    }

    public static void setInstance(VocabularyDAO instance) {
        VocabularyDAO.instance = instance;
    }

    public VocabularyDAO(){
        path = "Vocabularies";
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

    public void changeStatus(Vocabulary vocabulary){
        mDatabase = FirebaseDatabase.getInstance().getReference(path);
        mDatabase.child(Common.language.getId()).child(vocabulary.getId()).child("active").setValue(!vocabulary.isActive());
    }

    public void saveWordWithPhonetic(Vocabulary vocabulary) {
        DictionaryApiService.apiService.getInfomationOfWord(vocabulary.getWord()).enqueue(new Callback<List<Dictionary>>() {
            @Override
            public void onResponse(Call<List<Dictionary>> call, Response<List<Dictionary>> response) {
                List<Dictionary> dictionaries = response.body();
                if(dictionaries != null) {
                    List<Phonetic> phonetics = dictionaries.get(0).phonetics;
                    String phonetic = "";
                    for(Phonetic p : phonetics) {
                        phonetic += p.text + " ";
                    }
                    phonetic = phonetic.substring(0, phonetic.length() - 1);
                    vocabulary.setPronunciation(phonetic);

                } else {
                    vocabulary.setPronunciation("");
                }

                mDatabase = FirebaseDatabase.getInstance().getReference(path);
                mDatabase.child(Common.language.getId()).child(vocabulary.getId()).setValue(vocabulary);
            }

            @Override
            public void onFailure(Call<List<Dictionary>> call, Throwable t) {
                Log.e("Api",t.getMessage());

            }
        });
    }
}
