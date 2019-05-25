package com.example.destinationrecognizer.model;

import com.google.gson.annotations.SerializedName;

public class WebMatchingModel {
    private String type;

    @SerializedName("page_name")
    private String name;

    private String url;
    private float score;

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public void setName(String name){this.name = name; }

    public String getName(){
        return name;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    public void setScore(float score){
        this.score = score;
    }

    public float getScore(){
        return Math.round(score*100f)/100f;
    }
}
