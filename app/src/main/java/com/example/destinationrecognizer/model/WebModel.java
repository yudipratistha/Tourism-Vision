package com.example.destinationrecognizer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WebModel implements Serializable {
    private String type;

    @SerializedName("web_entities_name")
    private String name;

    @SerializedName("url")
    private String url;

    @SerializedName("score")
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
