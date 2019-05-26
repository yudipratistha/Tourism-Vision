package com.example.destinationrecognizer;


import com.example.destinationrecognizer.model.LabelModel;
import com.example.destinationrecognizer.model.LandmarkModel;
import com.example.destinationrecognizer.model.WebMatchingModel;
import com.example.destinationrecognizer.model.WebModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Destination implements Serializable {
    @SerializedName("landmark")
    private List<LandmarkModel> landmark;

    @SerializedName("label")
    private List<LabelModel> label;

    @SerializedName("webEntities")
    private List<WebModel> webEntities;

    @SerializedName("pageMatchingImages")
    private List<WebMatchingModel> pageMatchingImages;

    @SerializedName("vision_name")
    private String vision_name;

    @SerializedName("alamat")
    private String alamat;

    @SerializedName("type")
    private String type;

    @SerializedName("deskripsi")
    private String deskripsi;

    @SerializedName("price")
    private String price;

    @SerializedName("imageBase64")
    private transient String imageBase64;

    public void setVisionName(String vision_name){this.vision_name = vision_name;}
    public void setAlamat(String alamat){this.alamat = alamat;}
    public void setType(String type){this.type = type;}
    public void setDeskripsi(String deskripsi){this.deskripsi = deskripsi;}
    public void setPrice(String price){this.price = price;}
    public void setImageBase64(String imageBase64){this.imageBase64 = imageBase64;}

    public String getVisionName(){return this.vision_name;}
    public String getAlamat(){return this.alamat;}
    public String getType(){return this.type;}
    public String getDeskripsi(){return this.deskripsi;}
    public String getPrice(){return this.price;}
    public String getImageBase64(){return this.imageBase64;}


    public void setLandmark(List<LandmarkModel> landmarks){
        this.landmark = landmarks;
    }
    public List<LandmarkModel> getLandmark(){
        return this.landmark;
    }

    public void setLabel(List<LabelModel> label){
        this.label = label;
    }
    public List<LabelModel> getLabel(){
        return this.label;
    }

    public void setWebEntities(List<WebModel> webEntities){ this.webEntities = webEntities; }
    public List<WebModel> getWebEntities(){
        return this.webEntities;
    }

    public void setPageMatchingImages(List<WebMatchingModel> pageMatchingImages){this.pageMatchingImages = pageMatchingImages; }
    public List<WebMatchingModel> getPageMatchingImages(){
        return this.pageMatchingImages;
    }


}
