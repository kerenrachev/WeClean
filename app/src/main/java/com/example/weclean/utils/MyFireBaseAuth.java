package com.example.weclean.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MyFireBaseAuth {

    private static final Integer RESULT_OK = -1;
    private Context context;
    private ActivityResultLauncher<Intent> signInLauncher;
    private CallBack_OpenHomePage callBackFireBaseAuth;
    private CallBack_LogOut callBack_logOut;
    private FirebaseUser firebaseUser;

    public interface CallBack_OpenHomePage {
        void openHomePage();
    }

    public interface CallBack_LogOut {
        void logout();
    }

    private MyFireBaseAuth(Context context) {
        this.context = context;
    }

    private static MyFireBaseAuth me;

    public static MyFireBaseAuth getMe() {
        return me;
    }

    public static MyFireBaseAuth initHelper(Context context) {
        if (me == null) {
            me = new MyFireBaseAuth(context);
        }
        return me;
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        // Remove the Firebase data change listener for this particular user.
        MyFireBaseDB.getMe().removeEventListener(firebaseUser.getUid());
        callBack_logOut.logout();
    }


    public MyFireBaseAuth setCallBack_logOut(CallBack_LogOut callBack_logOut) {
        this.callBack_logOut = callBack_logOut;
        return this;
    }

    public void assignActivity(AppCompatActivity activity) {

        signInLauncher = activity.registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                    @Override
                    public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                        onSignInResult(result);

                    }
                }
        );
    }

    public void signIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent signInIntent =
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        //.setLogo(R.drawable.headache)
                        .setTosAndPrivacyPolicyUrls("https://firebase.google.com/docs/auth/android/firebaseui?hl=en&authuser=0", "https://firebase.google.com/docs/auth/android/firebaseui?hl=en&authuser=0")
                        .build();

        signInLauncher.launch(signInIntent);
    }




    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            callBackFireBaseAuth.openHomePage();
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            //Log.d("pttt", result.getIdpResponse().getProviderType() + "\n" + result.getIdpResponse());
        }
    }
    public FirebaseUser getFireBaseUser(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); return firebaseUser;
    }


    public MyFireBaseAuth setCallback_userSigned(CallBack_OpenHomePage callBackFireBaseAuth) {
        this.callBackFireBaseAuth = callBackFireBaseAuth;
        return this;
    }
}
