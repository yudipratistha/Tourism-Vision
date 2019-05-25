package com.example.destinationrecognizer;

public class Destination {
    private String name;
    private String region;
    private int thumbnail;

    public Destination() {
    }

    public Destination(String name, String region, int thumbnail) {
        this.name = name;
        this.region = region;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() { return region; }

    public void setRegion(String region) { this.region = region; }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
