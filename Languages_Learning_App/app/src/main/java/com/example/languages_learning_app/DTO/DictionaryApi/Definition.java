package com.example.languages_learning_app.DTO.DictionaryApi;

import java.util.List;

public class Definition {
    public String definition;
    public String example;
    public List<String> synonyms;

    public Definition() {
    }

    public Definition(String definition, String example, List<String> synonyms) {
        this.definition = definition;
        this.example = example;
        this.synonyms = synonyms;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }
}
