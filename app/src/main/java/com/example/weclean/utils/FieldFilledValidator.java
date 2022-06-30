package com.example.weclean.utils;

import android.content.Context;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.ArrayList;

public class FieldFilledValidator {

    private Context context;

    private FieldFilledValidator(Context context) {
        this.context = context;
    }

    private static FieldFilledValidator me;

    public static FieldFilledValidator getMe() {
        return me;
    }

    public static FieldFilledValidator initHelper(Context context) {
        if (me == null) {
            me = new FieldFilledValidator(context);
        }
        return me;
    }

    public boolean checkServiceCardDetails(ArrayList<AppCompatEditText> textFields) {

        for(EditText field :textFields){
            if(field.getText().toString().length() == 0){
                field.setError("This is a mandatory field");
                return false;
            }
        }
        return true;
    }
}
