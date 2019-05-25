package com.example.destinationrecognizer.model;

public class PlaceModel {
    private String placeName;
    private String vicinity;
    private float azimuth;
    private float distance;

    public void setPlaceName(String placeName){
        this.placeName = placeName;
    }

    public String getPlaceName(){
        return placeName;
    }

    public void setVicinity(String vicinity){
        this.vicinity = vicinity;
    }

    public String getVicinity(){
        return vicinity;
    }

    public void setAzimuth(float azimuth){
        this.azimuth = azimuth;
    }

    public float getAzimuth(){
        return azimuth;
    }

    public void setDistance (float lat_a, float lng_a, float lat_b, float lng_b )
    {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;
        int meterConversion = 1609;
        this.distance = new Float(distance * meterConversion).floatValue();

    }

    public float getDistance(){ return  this.distance; }
    public String getDistanceString(){
        if(this.distance >= 1000){
            return "Distance : " + String.valueOf((this.distance/1000f + "KM"));
        }
        return "Distance : " + String.valueOf(Math.round(this.distance))+"M";
    }

    @Override
    public String toString(){
        return
                        "Place = '" + placeName + '\n' +
                        "Location = '" + vicinity + '\n' +
                                "Azimuth = '" + azimuth
                ;
    }
}
