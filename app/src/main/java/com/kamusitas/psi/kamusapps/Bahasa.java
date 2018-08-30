package com.kamusitas.psi.kamusapps;

public class Bahasa {
    private String source, target, url;

    Bahasa(String source, String target, String url){
        this.source = source;
        this.target = target;
        this.url = url;
    }

    public String getSource(){
        return this.source;
    }

    public void setSource(String source){
        this.source = source;
    }

    public String getFullName(){
        return String.format("%s - %s", this.source, this.target);
    }

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getTarget(){
        return this.target;
    }

    public void setTarget(String target){
        this.target = target;
    }


}
