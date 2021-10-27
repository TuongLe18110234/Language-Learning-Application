package com.example.languages_learning_app.DTO.DictionaryApi;

import java.util.List;

public class Dictionary {
    public String word;
    public List<Phonetic> phonetics;
    public List<Meaning> meanings;

    public Dictionary() {
    }

    public Dictionary(String word, List<Phonetic> phonetics, List<Meaning> meanings) {
        this.word = word;
        this.phonetics = phonetics;
        this.meanings = meanings;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<Phonetic> getPhonetics() {
        return phonetics;
    }

    public void setPhonetics(List<Phonetic> phonetics) {
        this.phonetics = phonetics;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<Meaning> meanings) {
        this.meanings = meanings;
    }
}

