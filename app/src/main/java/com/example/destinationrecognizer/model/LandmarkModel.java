package com.example.destinationrecognizer.model;

import com.google.gson.annotations.SerializedName;

public class LandmarkModel {

    @SerializedName("landmark_name")
    private String name;

    private float score;
    private double lat;
    private double lng;
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private float x3;
    private float y3;
    private float x4;
    private float y4;

    public void setX1(float x){ this.x1 = x; }
    public float getX1(){
        return x1;
    }
    public void setY1(float y){ this.y1 = y; }
    public float getY1(){ return y1; }

    public void setX2(float x){ this.x2 = x; }
    public float getX2(){
        return x2;
    }
    public void setY2(float y){ this.y2 = y; }
    public float getY2(){ return y2; }

    public void setX3(float x){ this.x3 = x; }
    public float getX3(){
        return x3;
    }
    public void setY3(float y){ this.y3 = y; }
    public float getY3(){ return y3; }

    public void setX4(float x){ this.x4 = x; }
    public float getX4(){
        return x4;
    }
    public void setY4(float y){ this.y4 = y; }
    public float getY4(){ return y4; }

    public void setScore(float score){
        this.score = score;
    }

    public float getScore(){
        return score;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setLat(double lat){
        this.lat = lat;
    }

    public double getLat(){
        return lat;
    }

    public void setLng(double lng){
        this.lng = lng;
    }

    public double getLng(){ return lng; }

    public int getPercentage(){
        float percentage = Math.round(score * 100f);
        return (int)percentage;
    }
}
