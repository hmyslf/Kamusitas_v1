package com.example.arham.kamusapps;

public class Bahasa {
    private String source, target, url;

    public Bahasa() {

    }
    public Bahasa(String source, String target, String url){
        this.source = source;
        this.target = target;
        this.url = url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setSource(String source){
        this.source = source;
    }
    public String getSource(){
        return this.source;
    }
    public String getFullName(){
        return String.format("%s - %s", this.source, this.target);
    }
    public String getUrl(){
        return this.url;
    }
    public void setTarget(String target){
        this.target = target;
    }
    public String getTarget(){
        return this.target;
    }


}
