package com.example.weclean.data;

public class Comment {

    private String uid;
    private String fullName;
    private String picture;
    private String comment;
    private int starts;


    public Comment(){};

    public String getUid() {
        return uid;
    }

    public Comment setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Comment setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public int getStarts() {
        return starts;
    }

    public Comment setStarts(int starts) {
        this.starts = starts;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public Comment setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public Comment setPicture(String picture) {
        this.picture = picture;
        return this;
    }
}
