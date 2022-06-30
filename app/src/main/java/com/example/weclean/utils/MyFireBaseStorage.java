package com.example.weclean.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.weclean.activities.Activity_SignUp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class MyFireBaseStorage {

    private Context context;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();
    private StorageReference currRef;
    private CallBack_ImageUploaded callBack_imageUploaded;


    public interface CallBack_ImageUploaded {
        void imageUploded();
        void imageUrlReturned(Uri uri);
    }

    private MyFireBaseStorage(Context context) {
        this.context = context;
    }

    private static MyFireBaseStorage me;

    public static MyFireBaseStorage getMe() {
        return me;
    }

    public static MyFireBaseStorage initHelper(Context context) {
        if (me == null) {
            me = new MyFireBaseStorage(context);
        }
        return me;
    }

    public MyFireBaseStorage setCallBack_imageUploaded(CallBack_ImageUploaded callBack_imageUploaded) {
        this.callBack_imageUploaded = callBack_imageUploaded;
        return this;
    }

    public void uploadImage(Uri filePath)
    {
        if (filePath != null) {


            // Defining the child of storageReference
            currRef = storageReference.child("images/"+ UUID.randomUUID().toString());

            currRef.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    // Image uploaded successfully
                                    Toast.makeText(context,"Image Uploaded!!",Toast.LENGTH_SHORT) .show();
                                    callBack_imageUploaded.imageUploded();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            // Error, Image not uploaded
                            Toast.makeText(context,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                }
                            });
        }
        else {
            callBack_imageUploaded.imageUploded();
        }
    }

    public void getUploadedFileUrl(){
        currRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                callBack_imageUploaded.imageUrlReturned(uri);
            }
        });
    }

}


