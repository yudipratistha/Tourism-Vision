package com.example.destinationrecognizer.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VisionModel1 {
    @SerializedName("landmark")
    private List<LandmarkModel> landmark;

    @SerializedName("label")
    private List<LabelModel> label;

    @SerializedName("web_entities")
    private List<WebModel> webEntities;

    @SerializedName("page_matching_images")
    private List<WebMatchingModel> pageMatchingImages;

    private String vision_name;
    private String alamat;
<<<<<<< HEAD
    private String type;
    private String deskripsi;
    private String price;
    private String imageBase64;

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

=======
    private String luas_area;
    private String deskripsi;
    @SerializedName("fasilitas_aktivitas")
    private String fasilitas;

    public void setVisionName(String vision_name){this.vision_name = vision_name;}
    public void setAlamat(String alamat){this.alamat = alamat;}
    public void setLuasArea(String luas_area){this.luas_area = luas_area;}
    public void setDeskripsi(String deskripsi){this.deskripsi = deskripsi;}
    public void setFasilitas(String fasilitas){this.fasilitas = fasilitas;}

    public String getVisionName(){return this.vision_name;}
    public String getAlamat(){return this.alamat;}
    public String getLuasArea(){return this.luas_area;}
    public String getDeskripsi(){return this.deskripsi;}
    public String getFasilitas(){return this.fasilitas;}
>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344

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
