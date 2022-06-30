package com.example.weclean.data;

import java.util.ArrayList;

public class User {

    private String UID;
    private String picture;
    private String firstName;
    private String lastName;
    private String phone;
    private Service_Card service_card = null;
    private ArrayList<Job> jobs;
    private ArrayList<Notification> notifications;

    private User(){}

    private static User me;

    public static User getMe() {
        return me;
    }

    public static User initHelper() {
        if (me == null) {
            me = new User();
        }
        return me;
    }
    public String getUID() {
        return UID;
    }

    public User setUID(String UID) {
        this.UID = UID;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public User setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Service_Card getService_card() {
        return service_card;
    }

    public User setService_card(Service_Card service_card) {
        this.service_card = service_card;
        return this;
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public User setJobs(ArrayList<Job> jobs) {
        this.jobs = jobs;
        return this;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public User setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

}
