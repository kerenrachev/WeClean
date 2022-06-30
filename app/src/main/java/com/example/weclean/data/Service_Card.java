package com.example.weclean.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletionException;

public class Service_Card {

    private String id;
    private String UID;
    private String details;
    private double stars;
    private String picture;
    private String firstName;
    private String lastName;
    private String phoneNum;
    private int price;
    //private ArrayList<Comment> comments;

    private HashMap<String, Comment> comments;

    public Service_Card(){}

    public String getUID() {
        return UID;
    }

    public Service_Card setUID(String UID) {
        this.UID = UID;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public Service_Card setDetails(String details) {
        this.details = details;
        return this;
    }

    public double getStars() {
        return stars;
    }

    public Service_Card setStars(double stars) {
        this.stars = stars;
        return this;
    }

    //public ArrayList<Comment> getComments() {
    //    return comments;
    //}
//
    //public Service_Card setComments(ArrayList<Comment> comments) {
    //    this.comments = comments;
    //    return this;
    //}


    public HashMap<String, Comment> getComments() {
        return comments;
    }

    public Service_Card setComments(HashMap<String, Comment> comments) {
        this.comments = comments;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public Service_Card setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Service_Card setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Service_Card setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public Service_Card setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        return this;
    }

    public int getPrice() {
        return price;
    }

    public Service_Card setPrice(int price) {
        this.price = price;
        return this;
    }

    public String getId() {
        return id;
    }

    public Service_Card setId(String id) {
        this.id = id;
        return this;
    }
}
