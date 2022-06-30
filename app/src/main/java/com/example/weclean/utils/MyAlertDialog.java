package com.example.weclean.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.weclean.R;

public class MyAlertDialog {
    private Context context;
    private CallBack_agree callBack_agree;
    public MyAlertDialog(Context context){
        this.context = context;

    }

    public interface CallBack_agree{
        void agreeToOfferService();
    }

    private static MyAlertDialog me;

    public static MyAlertDialog getMe() {
        return me;
    }

    public static MyAlertDialog initHelper(Context context) {
        if (me == null) {
            me = new MyAlertDialog(context);
        }
        return me;
    }

    public MyAlertDialog setCallBack_agree(CallBack_agree callBack_agree) {
        this.callBack_agree = callBack_agree;
        return this;
    }

    public void startAlertDialog(Activity activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Make an offer");
        alertDialog.setMessage("Your service card will be shared with the job poster, do you wish to continue?");
        alertDialog.setIcon(R.drawable.ic_clean);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        // Setting Positive "OK" Button
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        callBack_agree.agreeToOfferService();
                    }
                });
        // Setting Negative "CANCEL" Button
        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
}
