package com.example.destinationrecognizer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LabelModel implements Serializable {

    @SerializedName("label_name")
    private String name;

    @SerializedName("score")
    private float score;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setScore(float score){
        this.score = score;
    }

    public float getScore(){
        return score;
    }

    public int getPercentage(){
        float percentage = Math.round(score * 100f);
        return (int)percentage;
    }
}
