package com.example.weclean.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.example.weclean.R;
import com.example.weclean.utils.MyFireBaseAuth;
import com.example.weclean.utils.MyFireBaseDB;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_Splash extends AppCompatActivity {

    private MaterialTextView sign_LBL_info;
    private MaterialButton splash_BTN_sign;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private static final String TAG = Activity_Splash.class.getSimpleName();

    final int ANIM_DURATION = 1400;

    private ImageView splash_IMG_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        initAuth();
        loadFireBaseUser();
        findViews();
        showViewSlideDown(splash_IMG_logo);
        setSignBTN();
        setCallBacks();
        setClickListeners();


    }

    MyFireBaseAuth.CallBack_OpenHomePage callBackFireBaseAuth = new MyFireBaseAuth.CallBack_OpenHomePage() {
        @Override
        public void openHomePage() {
            MyFireBaseDB.getMe().checkIfUserInDB();
        }
    };

    MyFireBaseDB.CallBack_CheckIfUserInDB callBack_checkIfUserInDB = new MyFireBaseDB.CallBack_CheckIfUserInDB() {
        @Override
        public void checkIfUserInDB(boolean inDB) {
            Log.d("pttt", inDB+ "");
            if(inDB){
                openHomeActivity();
            }
            else{
                openRegisterActivity();
            }
        }
    };

    private void setClickListeners() {
        splash_BTN_sign.setOnClickListener(view -> MyFireBaseAuth.getMe().signIn());
    }

    private void setSignBTN() {
        if(user != null){
            splash_BTN_sign.setVisibility(View.INVISIBLE);
        }
    }

    private void setCallBacks() {
        MyFireBaseAuth.getMe().assignActivity(this);
        MyFireBaseAuth.getMe().setCallback_userSigned(callBackFireBaseAuth);
        MyFireBaseDB.getMe().setCallBack_checkIfUserInDB(callBack_checkIfUserInDB);
    }

    private void initAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void findViews() {
        splash_BTN_sign = findViewById(R.id.splash_BTN_sign);
        splash_IMG_logo = findViewById(R.id.splash_IMG_logo);
        splash_IMG_logo.setVisibility(View.INVISIBLE);
    }

    private void loadFireBaseUser() {
        user = mAuth.getCurrentUser();
    }

    public void showViewSlideDown(final View v) {
        v.setVisibility(View.VISIBLE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        v.setY(-height / 2);
        v.setScaleY(0.0f);
        v.setScaleX(0.0f);
        v.animate()
                .scaleY(1.0f)
                .scaleX(1.0f)
                .translationY(0)
                .setDuration(ANIM_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animationDone();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    private void animationDone() {
        if(user!= null){
            MyFireBaseDB.getMe().checkIfUserInDB();

        }
        else{
            splash_BTN_sign.setVisibility(View.VISIBLE);
        }

    }

    private void openHomeActivity() {
        Intent intent = new Intent(this, Activity_Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, Activity_SignUp.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }





}