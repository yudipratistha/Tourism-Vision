package com.example.destinationrecognizer.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VisionModel {
    @SerializedName("vision")
    private VisionModel1 visionModel1;
    public VisionModel1 getVisionModel1(){
        return this.visionModel1;
    }

    @Override
    public String toString(){
        return
                "Vision{" +
                        "vision = '" + visionModel1 + '\'' +
                        "}";
    }
}
