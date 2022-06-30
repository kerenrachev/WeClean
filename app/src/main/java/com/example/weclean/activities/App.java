package com.example.weclean.activities;

import android.app.Application;

import com.example.weclean.adapters.CardsAdapter;
import com.example.weclean.adapters.CommentAdapter;
import com.example.weclean.adapters.JobsAdapter;
import com.example.weclean.adapters.NotificationsAdapter;
import com.example.weclean.data.Comment;
import com.example.weclean.data.User;
import com.example.weclean.utils.FieldFilledValidator;
import com.example.weclean.utils.MyAlertDialog;
import com.example.weclean.utils.MyFireBaseAuth;
import com.example.weclean.utils.MyFireBaseDB;
import com.example.weclean.utils.MyFireBaseStorage;
import com.example.weclean.utils.MyGalleryPicker;
import com.example.weclean.utils.MyPlacesAutoComplete;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MyFireBaseAuth.initHelper(this);
        MyFireBaseDB.initHelper(this);
        MyFireBaseStorage.initHelper(this);
        User.initHelper();
        MyGalleryPicker.initHelper(this);
        MyPlacesAutoComplete.initHelper(this);
        JobsAdapter.initHelper(this);
        CardsAdapter.initHelper(this);
        CommentAdapter.initHelper(this);
        MyAlertDialog.initHelper(this);
        NotificationsAdapter.initHelper(this);
        FieldFilledValidator.initHelper(this);
    }

}
