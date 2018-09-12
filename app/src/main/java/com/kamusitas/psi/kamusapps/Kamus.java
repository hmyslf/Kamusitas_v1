package com.kamusitas.psi.kamusapps;

public class Kamus {
    private String word;
    private String definition;
    private int id;

    public Kamus(String word){
        this.word = word;
    }

    public Kamus(String word, String definition){
        this.word = word;
        this.definition = definition;
    }

    public String getWord(){
        return word;
    }
    public void setWord(String word){
        this.word = word;
    }
    public String getDefinition(){
        return definition;
    }
    public void setDefinition(String definition){
        this.definition = definition;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }

}
