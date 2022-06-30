package com.example.weclean.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.weclean.R;
import com.example.weclean.data.User;
import com.example.weclean.utils.FieldFilledValidator;
import com.example.weclean.utils.MyFireBaseDB;
import com.example.weclean.utils.MyFireBaseStorage;
import com.example.weclean.utils.MyGalleryPicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

public class Activity_SignUp extends AppCompatActivity {

    private ShapeableImageView register_IMGBTN_uploadPic;
    private AppCompatEditText register_TXT_firstname;
    private AppCompatEditText register_TXT_lastname;
    private AppCompatEditText register_TXT_phonenum;
    private MaterialButton register_BTN_register;
    private ProgressBar sign_PROGBAR_progress;
    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initStorage();
        setCallBacks();
        findViews();
        setClickListeners();

    }

    private void setCallBacks() {
        MyFireBaseStorage.getMe().setCallBack_imageUploaded(callBack_imageUploaded);
    }

    private void initStorage() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    private void findViews() {
        register_IMGBTN_uploadPic = findViewById(R.id.register_IMGBTN_uploadPic);
        register_TXT_firstname = findViewById(R.id.register_TXT_firstname);
        register_TXT_lastname = findViewById(R.id.register_TXT_lastname);
        register_TXT_phonenum = findViewById(R.id.register_TXT_phonenum);
        register_BTN_register = findViewById(R.id.register_BTN_register);
        sign_PROGBAR_progress = findViewById(R.id.sign_PROGBAR_progress);
    }


    private void setClickListeners() {
        register_IMGBTN_uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyGalleryPicker.getMe().SelectImage(Activity_SignUp.this);
            }
        });
        register_BTN_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(updateUserDetails())
                        openRegisterActivity();

            }
        });
    }

    // Override onActivityResult method - called when returning from image picker
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == MyGalleryPicker.getMe().getPICK_IMAGE_REQUEST()
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                register_IMGBTN_uploadPic.setImageBitmap(bitmap);
                MyFireBaseStorage.getMe().uploadImage(filePath);
                register_BTN_register.setEnabled(false);
                sign_PROGBAR_progress.setVisibility(View.VISIBLE);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }


    MyFireBaseStorage.CallBack_ImageUploaded callBack_imageUploaded = new MyFireBaseStorage.CallBack_ImageUploaded() {
        @Override
        public void imageUploded() {
            MyFireBaseStorage.getMe().getUploadedFileUrl();
        }

        @Override
        public void imageUrlReturned(Uri uri) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            User.getMe().setPicture(uri.toString());
            register_BTN_register.setEnabled(true);
            sign_PROGBAR_progress.setVisibility(View.GONE);
        }
    };

    private boolean updateUserDetails() {

        // Validate if card details filled
        ArrayList<AppCompatEditText> mandatoryFields = new ArrayList<>();
        mandatoryFields.add(register_TXT_firstname);
        mandatoryFields.add(register_TXT_lastname);
        mandatoryFields.add(register_TXT_phonenum);

        if(User.getMe().getPicture() == null){
            Toast.makeText(this, "Please choose a profile picture", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(FieldFilledValidator.getMe().checkServiceCardDetails(mandatoryFields)) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            User.getMe()
                    .setFirstName(register_TXT_firstname.getText()+"")
                    .setLastName(register_TXT_lastname.getText()+"")
                    .setPhone(register_TXT_phonenum.getText()+"")
                    .setUID(user.getUid());
            MyFireBaseDB.getMe().saveUserInDB();
            return true;

        }
        return false;
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, Activity_Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


}