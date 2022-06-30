package com.example.weclean.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.weclean.Helpers.NotificationComperator;
import com.example.weclean.adapters.NotificationsAdapter;
import com.example.weclean.data.Comment;
import com.example.weclean.data.Job;
import com.example.weclean.data.Notification;
import com.example.weclean.data.Notification_type;
import com.example.weclean.data.Service_Card;
import com.example.weclean.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MyFireBaseDB {

    private Context context;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private CallBack_CheckIfUserInDB callBack_checkIfUserInDB;
    private CallBack_UserObjectUpdated callBack_userObjectUpdated;
    private CallBack_getAllJobs callBack_getAllJobs;
    private CallBack_UserServiceGiver callBack_userServiceGiver;
    private CallBack_serviceCardCreated callBack_serviceCardCreated;
    private CallBack_getAllServiceCards callBack_getAllServiceCards;
    private CallBack_notificationsListener callBack_notificationsListener;
    private HashMap<String, String> userIMG;
    private NotificationComperator notificationComperator;




    public interface CallBack_notificationsListener{
        void notificationsReturned(ArrayList<Notification> notifications);
    }

    public interface CallBack_CheckIfUserInDB {
        void checkIfUserInDB(boolean inDB);
    }

    public interface CallBack_UserObjectUpdated{
        void userObjectUpdated();
    }
    public interface  CallBack_getAllJobs{
        void allJobsReturned(ArrayList<Job> jobs);
    }

    public interface CallBack_UserServiceGiver{
        void userHasServiceCard(boolean hasServiceCard);
    }

    public interface CallBack_serviceCardCreated{
        void cardCreated();
    }

    public interface  CallBack_getAllServiceCards{
        void serviceCardsReturned(ArrayList<Service_Card> allCards);
    }

    private MyFireBaseDB(Context context) {
        this.context = context;
        notificationComperator = new NotificationComperator();
    }

    private static MyFireBaseDB me;

    public static MyFireBaseDB getMe() {
        return me;
    }

    public static MyFireBaseDB initHelper(Context context) {
        if (me == null) {
            me = new MyFireBaseDB(context);
        }
        return me;
    }

    public Context getContext() {
        return context;
    }

    public MyFireBaseDB setCallBack_notificationsListener(CallBack_notificationsListener callBack_notificationsListener) {
        this.callBack_notificationsListener = callBack_notificationsListener;
        return this;
    }

    public MyFireBaseDB setCallBack_checkIfUserInDB(CallBack_CheckIfUserInDB callBack_checkIfUserInDB) {
        this.callBack_checkIfUserInDB = callBack_checkIfUserInDB;
        return this;
    }

    public MyFireBaseDB setCallBack_userObjectUpdated(CallBack_UserObjectUpdated callBack_userObjectUpdated) {
        this.callBack_userObjectUpdated = callBack_userObjectUpdated;
        return this;
    }

    public MyFireBaseDB setCallBack_getAllJobs(CallBack_getAllJobs callBack_getAllJobs) {
        this.callBack_getAllJobs = callBack_getAllJobs;
        return this;
    }

    public MyFireBaseDB setCallBack_userServiceGiver(CallBack_UserServiceGiver callBack_userServiceGiver) {
        this.callBack_userServiceGiver = callBack_userServiceGiver;
        return this;
    }

    public MyFireBaseDB setCallBack_serviceCardCreated(CallBack_serviceCardCreated callBack_serviceCardCreated) {
        this.callBack_serviceCardCreated = callBack_serviceCardCreated;
        return this;
    }

    public MyFireBaseDB setCallBack_getAllServiceCards(CallBack_getAllServiceCards callBack_getAllServiceCards) {
        this.callBack_getAllServiceCards = callBack_getAllServiceCards;
        return this;
    }

    public void saveUserInDB(){
        database.getReference()
                .child("Users")
                .child(User.getMe().getUID())
                .setValue(User.getMe());
    }

    public void checkIfUserInDB(){
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    callBack_checkIfUserInDB.checkIfUserInDB(true);
                }
                else callBack_checkIfUserInDB.checkIfUserInDB(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getUserFromDB(){

        database.getReference()
                .child("Users")
                .child(MyFireBaseAuth.getMe().getFireBaseUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {



                User.getMe().setUID(MyFireBaseAuth.getMe().getFireBaseUser().getUid())
                        .setPhone(snapshot.child("phone").getValue().toString())
                        .setFirstName(snapshot.child("firstName").getValue().toString())
                        .setLastName(snapshot.child("lastName").getValue().toString())
                        .setPicture(snapshot.child("picture").getValue().toString());


                if(snapshot.child("serviceCard").getValue() != null){
                    setUserServiceCard(snapshot.child("serviceCard").getValue().toString());

                }
                else callBack_userObjectUpdated.userObjectUpdated();
                // Need to update here all other objects like jobs, nofications etc...
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUserServiceCard(String id) {

        database.getReference()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        DataSnapshot serviceCardSnapshot = snapshot.child("ServiceCards").child(id);

                        Service_Card service_card = new Service_Card();
                        service_card.setId(serviceCardSnapshot.child("id").getValue().toString())
                                .setFirstName(serviceCardSnapshot.child("firstName").getValue().toString())
                                .setLastName(serviceCardSnapshot.child("lastName").getValue().toString())
                                .setPhoneNum(serviceCardSnapshot.child("phoneNum").getValue().toString())
                                .setPrice(Integer.parseInt(serviceCardSnapshot.child("price").getValue().toString()))
                                .setStars(Integer.parseInt(serviceCardSnapshot.child("stars").getValue().toString()))
                                .setDetails(serviceCardSnapshot.child("details").getValue().toString())
                                .setUID(serviceCardSnapshot.child("uid").getValue().toString())
                                .setPicture(serviceCardSnapshot.child("picture").getValue().toString());
                        if(serviceCardSnapshot.child("comments").getValue() == null ){
                            service_card.setComments(new HashMap<String, Comment>());
                        }
                        else{
                            HashMap<String,Comment> cardComments = new HashMap<>();
                            for(DataSnapshot comment: serviceCardSnapshot.child("comments").getChildren()){
                                String userUid = comment.child("uid").getValue().toString();
                                String fullName =
                                        snapshot.child("Users")
                                                .child(userUid)
                                                .child("firstName")
                                                .getValue()
                                                .toString()
                                                +
                                                snapshot.child("Users")
                                                        .child(userUid).child("lastName")
                                                        .getValue()
                                                        .toString();
                                String picture = snapshot.child("Users")
                                        .child(userUid)
                                        .child("picture")
                                        .getValue()
                                        .toString();
                                Comment newComment = new Comment();
                                newComment.setComment(comment.child("comment").getValue().toString())
                                        .setStarts(Integer.parseInt(comment.child("starts").getValue().toString()))
                                        .setPicture(picture)
                                        .setFullName(fullName)
                                        .setUid(comment.child("uid").getValue().toString());
                                cardComments.put( comment.getKey() , newComment);

                            }
                            service_card.setComments(cardComments);
                        }
                        User.getMe().setService_card(service_card);
                        callBack_userObjectUpdated.userObjectUpdated();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getUserPicForJob(String uid){
        database.getReference()
                .child("Users")
                .child(uid)
                .child("picture")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void saveNewJob(Job job) {

        database.getReference()
                .child("Users")
                .child(job.getUID())
                .child("jobs")
                .child(job.getId())
                .setValue(job.getId());

        database.getReference()
                .child("Jobs")
                .child(job.getId())
                .setValue(job);
    }


    public void getAllJobsFromDB(){
            database.getReference()
                .child("Jobs")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        ArrayList<Job> allJobs = new ArrayList<>();
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            if(!ds.child("uid").getValue().toString().equals(MyFireBaseAuth.getMe().getFireBaseUser().getUid())){
                                Job job = new Job();
                                job.setId(ds.getKey())
                                        .setUID(ds.child("uid").getValue().toString())
                                        .setLat((double) ds.child("lat").getValue())
                                        .setLng((double) ds.child("lng").getValue())
                                        .setDescription(ds.child("description").getValue().toString())
                                        .setLocation_name(ds.child("location_name").getValue().toString())
                                        .setImgUrl(ds.child("imgUrl").getValue().toString());
                                allJobs.add(job);
                            }
                        }
                        callBack_getAllJobs.allJobsReturned(allJobs);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getAllServiceCardsFromDB(){
        database.getReference()

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        ArrayList<Service_Card> allCards = new ArrayList<>();
                        for(DataSnapshot ds : snapshot.child("ServiceCards").getChildren()) {
                            if(!ds.child("uid").getValue().toString().equals(MyFireBaseAuth.getMe().getFireBaseUser().getUid())){
                                Service_Card card = new Service_Card();
                                //Log.d("pttt", )
                                card.setUID(ds.child("uid").getValue().toString())
                                        .setPicture(ds.child("picture").getValue().toString())
                                        .setDetails(ds.child("details").getValue().toString())
                                        .setStars(Double.parseDouble(ds.child("stars").getValue().toString()))
                                        .setId(ds.child("id").getValue().toString())
                                        .setPrice(Integer.parseInt(ds.child("price").getValue().toString()))
                                        .setPhoneNum(ds.child("phoneNum").getValue().toString())
                                        .setLastName(ds.child("lastName").getValue().toString())
                                        .setFirstName(ds.child("firstName").getValue().toString());

                                if(ds.child("comments").getValue() == null){
                                    card.setComments(new HashMap<String, Comment>());
                                }

                                else{
                                    HashMap<String,Comment> cardComments = new HashMap<>();
                                    for(DataSnapshot comment: ds.child("comments").getChildren()){
                                        String userUid = comment.child("uid").getValue().toString();
                                        String fullName =
                                                snapshot.child("Users")
                                                .child(userUid)
                                                .child("firstName")
                                                .getValue()
                                                .toString()
                                                +
                                                snapshot.child("Users")
                                                .child(userUid).child("lastName")
                                                .getValue()
                                                .toString();
                                        String picture = snapshot.child("Users")
                                                .child(userUid)
                                                .child("picture")
                                                .getValue()
                                                .toString();
                                        Comment newComment = new Comment();
                                        newComment.setComment(comment.child("comment").getValue().toString())
                                                .setStarts(Integer.parseInt(comment.child("starts").getValue().toString()))
                                                .setPicture(picture)
                                                .setFullName(fullName)
                                                .setUid(comment.child("uid").getValue().toString());
                                        cardComments.put( comment.getKey() , newComment);

                                    }
                                    card.setComments(cardComments);
                                }
                                allCards.add(card);
                            }
                        }
                        callBack_getAllServiceCards.serviceCardsReturned(allCards);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void checkIfUserHasServiceCard() {
        database.getReference()
                .child("Users")
                .child(MyFireBaseAuth.getMe().getFireBaseUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //Log.d("pttt", snapshot.child("serviceCard").getValue().toString());
                       if(snapshot.child("serviceCard").getValue() != null){
                            callBack_userServiceGiver.userHasServiceCard(true);
                       }
                       else{
                           callBack_userServiceGiver.userHasServiceCard(false);
                       }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    public void createServiceCard(Service_Card service_card) {
        database.getReference()
                .child("Users")
                .child(MyFireBaseAuth.getMe().getFireBaseUser().getUid())
                .child("serviceCard")
                .setValue(service_card.getId());

        database.getReference()
                .child("ServiceCards")
                .child(service_card.getId())
                .setValue(service_card);
        callBack_serviceCardCreated.cardCreated();
    }


    public void addCommentToCard(String id, Comment comment, double newRating, Notification newNotification, String randomId) {
        DatabaseReference ref =  database.getReference()
                .child("ServiceCards")
                .child(id);
        ref
                .child("comments")
                .child(randomId)
                .setValue(comment);
        ref
                .child("stars")
                .setValue(newRating);

        createNotification(newNotification);
    }


    public void listenToNotifications(String uid){
        database.getReference()
                .child("Users")
                .child(uid)
                .child("Notifications")
                .addValueEventListener(notificationsListener);

    }

    public void removeEventListener(String uid){
        DatabaseReference readRef = database.getReference()
                .child("Users")
                .child(uid)
                .child("Notifications");

        if (readRef != null && notificationsListener != null) {
            readRef.removeEventListener(notificationsListener);
        }
    }


    ValueEventListener notificationsListener =  new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            Log.d("pttt", "Listening");
            ArrayList<Notification> notifications = new ArrayList<>();
            if(snapshot!= null){
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Notification notification = new Notification();
                    notification.setNotificationId(ds.getKey());
                    notification.setUid(ds.child("uid").getValue().toString());
                    notification.setTimeStamp((Long)ds.child("timeStamp").getValue());
                    notification.setText(ds.child("text").getValue().toString());
                    notification.setNotification_type(Notification_type.valueOf(ds.child("notification_type").getValue().toString()));
                    notification.setPicture(ds.child("picture").getValue().toString());
                    notification.setSeen((Boolean)ds.child("seen").getValue());
                    notifications.add(notification);
                }
            }

            notifications.sort(notificationComperator);

            //oldNotifications.sort(notificationComperator);
            callBack_notificationsListener.notificationsReturned(notifications);

        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void createNotification(Notification newNotification) {

        Log.d("pttt","ME: " + newNotification.getUid() +" Poster: " + newNotification.getSendToUID());
        DatabaseReference ref =  database.getReference()
                .child("Users")
                .child(newNotification.getSendToUID())
                .child("Notifications")
                .child(newNotification.getNotificationId())
                ;
        ref.setValue(newNotification);
    }

    public void updateNotificationsSeen(String uid, ArrayList<Notification> allNotifications) {

        DatabaseReference ref =  database.getReference()
                .child("Users")
                .child(uid)
                .child("Notifications");
        HashMap<String, Notification> mapNotifications = new HashMap<>();

        for(Notification not : allNotifications){
            not.setSeen(true);
            mapNotifications.put(not.getNotificationId(), not);
        }

        ref.setValue(mapNotifications);


    }

}
