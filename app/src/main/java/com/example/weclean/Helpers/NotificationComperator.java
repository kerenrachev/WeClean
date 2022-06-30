package com.example.weclean.Helpers;

import com.example.weclean.data.Notification;

import java.util.Comparator;

public class NotificationComperator implements Comparator {

    @Override
    public int compare(Object one, Object two) {
        Notification n1=(Notification)one;
        Notification n2=(Notification)two;
        if(n1.getTimeStamp()< n2.getTimeStamp()){
            return 1;
        }
        else if(n1.getTimeStamp()> n2.getTimeStamp()){
            return -1;
        }
        return 0;
    }
}
