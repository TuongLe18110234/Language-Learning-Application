package com.example.languages_learning_app.DTO.DictionaryApi;

public class Phonetic {
    public String text;
    public String audio;

    public Phonetic() {
    }

    public Phonetic(String text, String audio) {
        this.text = text;
        this.audio = audio;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
