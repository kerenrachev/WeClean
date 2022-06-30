package com.example.weclean.data;

public class Job {

    private String id;
    private String imgUrl;
    private String UID;
    private String description;
    private String location_name;
    private double lat;
    private double lng;

    public Job(){}

    public String getUID() {
        return UID;
    }

    public Job setUID(String UID) {
        this.UID = UID;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Job setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getLocation_name() {
        return location_name;
    }

    public Job setLocation_name(String location_name) {
        this.location_name = location_name;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Job setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public Job setLng(double lng) {
        this.lng = lng;
        return this;
    }

    public String getId() {
        return id;
    }

    public Job setId(String id) {
        this.id = id;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Job setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }
}
