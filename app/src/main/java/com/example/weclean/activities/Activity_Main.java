package com.example.weclean.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclean.R;
import com.example.weclean.databinding.ActivityMainNavBinding;
import com.example.weclean.fragments.Fragment_List_Service_Cards;
import com.example.weclean.fragments.Fragment_Jobs;
import com.example.weclean.fragments.Fragment_New_Job;
import com.example.weclean.fragments.Fragment_Home;
import com.example.weclean.fragments.Fragment_Service_Card;
import com.example.weclean.adapters.JobsAdapter;
import com.example.weclean.adapters.NotificationsAdapter;
import com.example.weclean.data.Notification;
import com.example.weclean.data.User;
import com.example.weclean.utils.MyFireBaseAuth;
import com.example.weclean.utils.MyFireBaseDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Activity_Main extends AppCompatActivity {

    private ActivityMainNavBinding binding;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private MenuItem action_notification;
    private Toolbar toolbar;
    private RecyclerView notifications_LST_items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("We clean");
        Fragment_Home fragmentHome = new Fragment_Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragmentHome).commit();
        // Update the User Instance ( export data from DB)
        findViews();
        MyFireBaseDB.getMe().getUserFromDB();
        setCallBacks();
        setSideBarView();
        setBottomNavView();
        JobsAdapter.getMe().setActivity(this);

    }

    private void findViews() {

        //sfsdfsdfsdf
        Log.d("pttt","blsfb");
    }

    private void setBottomNavView() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.jobs:
                        Fragment_Jobs fragment_jobs = new Fragment_Jobs();
                        getSupportFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragment_jobs).commit();
                        return true;

                    case R.id.home:
                        Fragment_Home fragmentHome = new Fragment_Home();
                        getSupportFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragmentHome).commit();
                        return true;
                    case R.id.service_givers:
                        Fragment_List_Service_Cards fragmentListServiceCards = new Fragment_List_Service_Cards();
                        getSupportFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragmentListServiceCards).commit();
                        return true;
                }
                return false;
            }
        });
    }

    private void setSideBarView() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Fragment_Home fragmentHome = new Fragment_Home();
                        getSupportFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragmentHome).commit();
                        DrawerLayout mDrawerLayout;
                        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                        mDrawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_gallery:
                        Fragment_New_Job fragmentNewJob = new Fragment_New_Job();
                        getSupportFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragmentNewJob).commit();

                        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                        mDrawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_slideshow:
                        Fragment_Service_Card fragmentServiceCard = new Fragment_Service_Card();
                        Bundle bundle = new Bundle();
                        bundle.putString("who", "mine");
                        fragmentServiceCard.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragmentServiceCard).commit();
                        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                        mDrawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_logout:
                        MyFireBaseAuth.getMe().logout();
                        break;
                }
                return true;
            }
        });
    }

    private void setCallBacks() {
        MyFireBaseDB.getMe().setCallBack_userObjectUpdated(callBack_userObjectUpdated);
        MyFireBaseAuth.getMe().setCallBack_logOut(callBack_logOut);
        MyFireBaseDB.getMe().setCallBack_notificationsListener(callBack_notificationsListener);
    }

    private void setUserInfo() {
        // Test to change the picture and name
        View header = navigationView.getHeaderView(0);
        TextView textView1 = (TextView) header.findViewById(R.id.textView1);
        textView1.setText(User.getMe().getFirstName() + " " + User.getMe().getLastName());
        TextView textView2 = (TextView) header.findViewById(R.id.textView2);
        textView2.setText(User.getMe().getPhone());
        ShapeableImageView imageView = (ShapeableImageView) header.findViewById(R.id.imageView);
        Picasso.with(this).load(User.getMe().getPicture()).placeholder(R.drawable.img_placeholder).into(imageView);

    }
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView test;
    private TextView badge_textView;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__main__nav, menu);

        // Handle clicks in the notifications icon
        MenuItem notifications = menu.findItem(R.id.action_notification);
        notifications.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotifications();
            }
        });

        // Handle new notifications
        badge_textView = notifications.getActionView().findViewById(R.id.badge_textView);

        // Start listenening to notifications
        MyFireBaseDB.getMe().listenToNotifications(MyFireBaseAuth.getMe().getFireBaseUser().getUid());


        return true;
    }

    private void showNotifications(){

        dialogBuilder = new AlertDialog.Builder(this);

        final View notificationsView = getLayoutInflater().inflate(R.layout.notifications_popup,null);
        test = notificationsView.findViewById(R.id.test);

        dialogBuilder.setView(notificationsView);

        dialog = dialogBuilder.create();
        if(User.getMe().getNotifications().size() ==0 ){
            test.setText("You still don't have notifications.");
        }

        else{
            notifications_LST_items = notificationsView.findViewById(R.id.notifications_LST_items);;

            // Update notifications list in notifications adapter
            NotificationsAdapter.getMe().setNotifications(User.getMe().getNotifications());
            NotificationsAdapter.getMe().setCallBack_closeNotificationsWindow(callBack_closeNotificationsWindow);
            // Set notifications adapter
            notifications_LST_items.setLayoutManager(new LinearLayoutManager(this));
            notifications_LST_items.setHasFixedSize(true);
            notifications_LST_items.setAdapter(NotificationsAdapter.getMe());


        }

        dialog.show();
    }

    NotificationsAdapter.CallBack_closeNotificationsWindow callBack_closeNotificationsWindow = new NotificationsAdapter.CallBack_closeNotificationsWindow() {
        @Override
        public void closeNotifications() {
            dialog.dismiss();
        }
    };


    private MyFireBaseDB.CallBack_notificationsListener callBack_notificationsListener = new MyFireBaseDB.CallBack_notificationsListener() {
        @Override
        public void notificationsReturned(ArrayList<Notification> allNotifications) {
            // Save notifications for the next time the user wants to view them.
            User.getMe().setNotifications(allNotifications);

            // Update notifications icon to hold the current number of new notifications.
            badge_textView.setText("");
            badge_textView.setBackgroundResource(R.color.white);

            if(allNotifications.size() != 0){
                int numOfNewNotifications = 0;
                for(Notification notification: allNotifications){
                    if(!notification.isSeen()) numOfNewNotifications++;
                }
                if(numOfNewNotifications != 0 ){
                    badge_textView.setText("" + numOfNewNotifications);
                    badge_textView.setBackgroundResource(com.google.android.libraries.places.R.color.quantum_vanillared700);
                }
                Log.d("pttt", "Num of new: " + numOfNewNotifications);
            }
            else{
                Log.d("pttt", "No notifications");
            }
        }
    };

    MyFireBaseDB.CallBack_UserObjectUpdated callBack_userObjectUpdated = new MyFireBaseDB.CallBack_UserObjectUpdated() {
        @Override
        public void userObjectUpdated() {
            setUserInfo();
        }
    };

    MyFireBaseAuth.CallBack_LogOut callBack_logOut = new MyFireBaseAuth.CallBack_LogOut() {
        @Override
        public void logout() {
            openSplashActivity();
        }
    };

    private void openSplashActivity() {
        Intent intent = new Intent(this, Activity_Splash.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}