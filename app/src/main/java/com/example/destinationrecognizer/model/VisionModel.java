package com.example.destinationrecognizer.model;

import com.google.gson.annotations.SerializedName;

<<<<<<< HEAD
import java.util.List;

public class VisionModel {
    @SerializedName("vision")
    private VisionModel1 visionModel1;
=======
public class VisionModel {
    @SerializedName("vision")
    private VisionModel1 visionModel1;

>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344
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
