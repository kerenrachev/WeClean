package com.example.weclean.data;

import com.google.firebase.database.ServerValue;

import java.sql.Timestamp;

public class Notification {

    private String notificationId;
    private String uid;
    private String text;
    private Long timeStamp;
    private Notification_type notification_type;
    private String picture;
    private boolean seen;
    private String sendToUID;


    public Notification(){}

    public Notification(Notification notification) {
        this.notificationId = notification.getNotificationId();
        this.uid = notification.getUid();
        this.text = notification.getText();
        this.timeStamp = notification.getTimeStamp();
        this.notification_type = notification.getNotification_type();
        this.picture = notification.getPicture();
        this.seen = notification.isSeen();
        this.sendToUID = notification.getSendToUID();
    }

    public String getUid() {
        return uid;
    }

    public Notification setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getText() {
        return text;
    }

    public Notification setText(String text) {
        this.text = text;
        return this;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public Notification setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public Notification_type getNotification_type() {
        return notification_type;
    }

    public Notification setNotification_type(Notification_type notification_type) {
        this.notification_type = notification_type;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public Notification setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public boolean isSeen() {
        return seen;
    }

    public Notification setSeen(boolean seen) {
        this.seen = seen;
        return this;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public Notification setNotificationId(String notificationId) {
        this.notificationId = notificationId;
        return this;
    }

    public String getSendToUID() {
        return sendToUID;
    }

    public Notification setSendToUID(String sendToUID) {
        this.sendToUID = sendToUID;
        return this;
    }
}
