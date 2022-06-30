package com.example.weclean.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weclean.R;
import com.example.weclean.data.Comment;
import com.example.weclean.data.Service_Card;
import com.example.weclean.data.User;
import com.example.weclean.utils.FieldFilledValidator;
import com.example.weclean.utils.MyFireBaseDB;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Fragment_New_Service_Card extends Fragment {

    private TextView editcard_TXT_instruction;
    private ShapeableImageView editcard_IMGBTN_uploadPic;
    private AppCompatEditText editcard_TXT_firstname;
    private AppCompatEditText editcard_TXT_lastname;
    private AppCompatEditText editcard_TXT_phonenum;
    private AppCompatEditText editcard_TXT_price;
    private AppCompatEditText editcard_TXT_description;
    private MaterialButton editcard_BTN_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_card, container, false);
        findViews(view);
        setCallBacks();

        Picasso.with(MyFireBaseDB.getMe().getContext()).load(User.getMe().getPicture()).placeholder(R.drawable.img_placeholder).into(editcard_IMGBTN_uploadPic);
        editcard_TXT_firstname.setText(User.getMe().getFirstName());
        editcard_TXT_lastname.setText(User.getMe().getLastName());
        editcard_TXT_phonenum.setText(User.getMe().getPhone());
        editcard_TXT_instruction.setText("Lets create your service card! :)");
        editcard_BTN_btn.setText("Publish Card");

        editcard_BTN_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Validate if card details filled
                ArrayList<AppCompatEditText> mandatoryFields = new ArrayList<>();
                mandatoryFields.add(editcard_TXT_firstname);
                mandatoryFields.add(editcard_TXT_lastname);
                mandatoryFields.add(editcard_TXT_phonenum);
                mandatoryFields.add(editcard_TXT_price);
                if(FieldFilledValidator.getMe().checkServiceCardDetails(mandatoryFields)){

                    Service_Card service_card = new Service_Card();
                    service_card.setUID(User.getMe().getUID())
                            .setId(UUID.randomUUID().toString())
                            .setFirstName(editcard_TXT_firstname.getText().toString())
                            .setLastName(editcard_TXT_lastname.getText().toString())
                            .setPhoneNum(editcard_TXT_phonenum.getText().toString())
                            .setPicture(User.getMe().getPicture())
                            .setPrice(Integer.parseInt(editcard_TXT_price.getText().toString()))
                            .setStars(0)
                            .setComments(new HashMap<String, Comment>())
                            .setDetails(editcard_TXT_description.getText().toString());
                    User.getMe().setService_card(service_card);
                    MyFireBaseDB.getMe().createServiceCard(service_card);
                }
            }
        });
        return view;
    }

    private void findViews(View view) {
        editcard_TXT_instruction = view.findViewById(R.id.editcard_TXT_instruction);
        editcard_IMGBTN_uploadPic = view.findViewById(R.id.editcard_IMGBTN_uploadPic);
        editcard_TXT_firstname = view.findViewById(R.id.editcard_TXT_firstname);
        editcard_TXT_lastname = view.findViewById(R.id.editcard_TXT_lastname);
        editcard_TXT_phonenum = view.findViewById(R.id.editcard_TXT_phonenum);
        editcard_TXT_price = view.findViewById(R.id.editcard_TXT_price);
        editcard_TXT_description = view.findViewById(R.id.editcard_TXT_description);
        editcard_BTN_btn = view.findViewById(R.id.editcard_BTN_btn);
    }

    private void setCallBacks() {
        MyFireBaseDB.getMe().setCallBack_serviceCardCreated(callBack_serviceCardCreated);
    }

    MyFireBaseDB.CallBack_serviceCardCreated callBack_serviceCardCreated = new MyFireBaseDB.CallBack_serviceCardCreated() {
        @Override
        public void cardCreated() {
            Fragment_Jobs jobsFragment= new Fragment_Jobs();
            getParentFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, jobsFragment).commit();
        }
    };

}