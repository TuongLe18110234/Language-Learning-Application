package com.example.languages_learning_app.APIs;

import com.example.languages_learning_app.DTO.DictionaryApi.Dictionary;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryApiService {

    // Link Api: https://api.dictionaryapi.dev/api/v2/entries/en_US/blue
    Gson gson = new GsonBuilder()
            .setDateFormat("YYYY-MM-dd HH:mm:ss")
            .create();

    DictionaryApiService apiService = new Retrofit.Builder()
            .baseUrl("https://api.dictionaryapi.dev/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(DictionaryApiService.class);

    @GET("api/v2/entries/en_US/{word}")
    Call<List<Dictionary>> getInfomationOfWord(@Path("word") String word);
}
