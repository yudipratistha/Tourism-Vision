package com.example.destinationrecognizer.model;

import com.google.gson.annotations.SerializedName;

<<<<<<< HEAD
import java.io.Serializable;

public class WebMatchingModel implements Serializable {
=======
public class WebMatchingModel {
>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344
    private String type;

    @SerializedName("page_name")
    private String name;

<<<<<<< HEAD
    @SerializedName("url")
    private String url;

    @SerializedName("score")
=======
    private String url;
>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344
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
