package com.example.destinationrecognizer.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VisionModelList {
    @SerializedName("vision")
    private List<VisionModel1> visionModelList;
    public List<VisionModel1> getVisionModelList() {return visionModelList; }

    @Override
    public String toString(){
        return
                "Vision{" +
                        "vision = '" + visionModelList + '\'' +
                        "}";
    }
}
