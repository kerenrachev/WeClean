package com.example.weclean.utils;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MyGalleryPicker {

    private Context context;
    // request code
    private final int PICK_IMAGE_REQUEST = 1;
    private MyGalleryPicker(Context context) {
        this.context = context;
    }

    private static MyGalleryPicker me;

    public static MyGalleryPicker getMe() {
        return me;
    }

    public static MyGalleryPicker initHelper(Context context) {
        if (me == null) {
            me = new MyGalleryPicker(context);
        }
        return me;
    }

    // Select Image method
    public void SelectImage(AppCompatActivity activity)
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        activity.startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    public int getPICK_IMAGE_REQUEST() {
        return PICK_IMAGE_REQUEST;
    }
}
