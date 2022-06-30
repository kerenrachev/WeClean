package com.example.weclean.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclean.R;
import com.example.weclean.adapters.CommentAdapter;
import com.example.weclean.data.Comment;
import com.example.weclean.data.Notification;
import com.example.weclean.data.Notification_type;
import com.example.weclean.data.Service_Card;
import com.example.weclean.data.User;
import com.example.weclean.utils.MyFireBaseAuth;
import com.example.weclean.utils.MyFireBaseDB;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hadi.emojiratingbar.EmojiRatingBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

public class Fragment_Service_Card extends Fragment {

    private TextView card_TXT_instruction;
    private MaterialButton card_BTN_btn;
    private LinearLayout card_LAY_newCard;
    private ScrollView card_LAY_card;
    private AppCompatImageView card_IMG_user;
    private MaterialTextView card_LBL_fullname;
    private MaterialTextView card_LBL_phone;
    private MaterialTextView card_LBL_price;
    private MaterialTextView card_LBL_description;
    private TextView card_TXT_nocomments;
    private EditText card_EDITTXT_putComment;
    private EmojiRatingBar emoji_rating_bar;
    private MaterialButton card_BTN_submitComment;
    private LinearLayout card_LAY_putCommentSection;
    private RecyclerView comments_LST_items;
    private ArrayList<ImageView> stars = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_card, container, false);
        findViews(view);
        if(getArguments()!= null){
            String who = getArguments().getString("who");
            // User wants to see his own service card
            if(who.equalsIgnoreCase("mine")){
                setCallBacks();
                MyFireBaseDB.getMe().checkIfUserHasServiceCard();

            }
            // User wants to view someone else's service card - Adding a comment will be allowed.
            else{
                String json = getArguments().getString("Card");
                Service_Card service_card = new Gson().fromJson(json, new TypeToken<Service_Card>(){}.getType());
                initCard(service_card);
            }
        }


        return view;
    }

    private void initCard(Service_Card service_card) {
        card_LAY_putCommentSection.setVisibility(View.VISIBLE);
        card_LAY_card.setVisibility(View.VISIBLE);
        card_BTN_submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rating = emoji_rating_bar.getCurrentRateStatus().ordinal();
                String commentContent = card_EDITTXT_putComment.getText().toString();
                Comment comment = new Comment();
                comment.setComment(commentContent)
                        .setStarts(rating)
                        .setUid(MyFireBaseAuth.getMe().getFireBaseUser().getUid())
                        .setFullName(User.getMe().getFirstName() +" " +User.getMe().getLastName())
                        .setPicture(User.getMe().getPicture());

                double newRating = 0;
                int numOfComments = service_card.getComments().size();
                if(numOfComments == 0){
                    newRating = rating;
                }
                else{
                    newRating = (service_card.getStars() * numOfComments + rating) / (numOfComments +1);
                }

                Notification newNotification = new Notification();
                newNotification.setUid(MyFireBaseAuth.getMe().getFireBaseUser().getUid())
                        .setPicture(User.getMe().getPicture())
                        .setNotification_type(Notification_type.COMMENT)
                        .setSeen(false)
                        .setNotificationId(UUID.randomUUID().toString())
                        .setText(User.getMe().getFirstName() +" "+ User.getMe().getLastName() + " has rated your service.")
                        .setSendToUID(service_card.getUID())
                        .setTimeStamp(Calendar.getInstance().getTimeInMillis());
                String randomId = UUID.randomUUID().toString();
                MyFireBaseDB.getMe().addCommentToCard(service_card.getId(),comment, newRating , newNotification, randomId);
                //Update the comments and refresh fragment
                service_card.getComments().put(randomId,comment);
                Bundle bundle = new Bundle();
                bundle.putString("who", "someoneElse");
                String JSONServiceCard= new Gson().toJson(service_card,new TypeToken<Service_Card>(){}.getType());
                bundle.putString("Card", JSONServiceCard);
                Fragment_Service_Card fragmentServiceCard = new Fragment_Service_Card();
                fragmentServiceCard.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragmentServiceCard).commit();
            }
        });
        updateCard(service_card);

    }

    private void findViews(View view) {
        card_TXT_instruction = view.findViewById(R.id.card_TXT_instruction);
        card_BTN_btn = view.findViewById(R.id.card_BTN_btn);
        card_LAY_card = view.findViewById(R.id.card_LAY_card);
        card_LAY_newCard = view.findViewById(R.id.card_LAY_newCard);
        card_IMG_user  = view.findViewById(R.id.serviceCard_IMG_user);
        card_LBL_fullname = view.findViewById(R.id.card_LBL_fullname);
        card_LBL_phone = view.findViewById(R.id.card_LBL_phone);
        card_LBL_price = view.findViewById(R.id.card_LBL_price);
        card_LBL_description  = view.findViewById(R.id.card_LBL_description);
        card_TXT_nocomments = view.findViewById(R.id.card_TXT_nocomments);
        card_EDITTXT_putComment = view.findViewById(R.id.card_EDITTXT_putComment);
        emoji_rating_bar = view.findViewById(R.id.emoji_rating_bar);
        card_BTN_submitComment = view.findViewById(R.id.card_BTN_submitComment);
        card_LAY_putCommentSection = view.findViewById(R.id.card_LAY_putCommentSection);
        comments_LST_items = view.findViewById(R.id.comments_LST_items);

        stars.add(view.findViewById(R.id.serviceCard_IMG_star1));
        stars.add(view.findViewById(R.id.serviceCard_IMG_star2));
        stars.add(view.findViewById(R.id.serviceCard_IMG_star3));
        stars.add(view.findViewById(R.id.serviceCard_IMG_star4));
        stars.add(view.findViewById(R.id.serviceCard_IMG_star5));

    }

    private void setCallBacks() {
        MyFireBaseDB.getMe().setCallBack_userServiceGiver(callBack_userServiceGiver);
        MyFireBaseDB.getMe().setCallBack_serviceCardCreated(callBack_serviceCardCreated);
    }

    MyFireBaseDB.CallBack_serviceCardCreated callBack_serviceCardCreated = new MyFireBaseDB.CallBack_serviceCardCreated() {
        @Override
        public void cardCreated() {
            Fragment_Jobs jobsFragment= new Fragment_Jobs();
            getParentFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, jobsFragment).commit();
        }
    };


    MyFireBaseDB.CallBack_UserServiceGiver callBack_userServiceGiver = new MyFireBaseDB.CallBack_UserServiceGiver() {
        @Override
        public void userHasServiceCard(boolean hasServiceCard) {

            if(hasServiceCard){
                // Show the service card and give the upportunity to edit
                card_LAY_card.setVisibility(View.VISIBLE);
                Service_Card card = User.getMe().getService_card();
                updateCard(card);
            }
            else{

                card_LAY_newCard.setVisibility(View.VISIBLE);
                card_TXT_instruction.setVisibility(View.VISIBLE);
                card_BTN_btn.setVisibility(View.VISIBLE);
                card_BTN_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment_New_Service_Card fragmentNewServiceCard = new Fragment_New_Service_Card();
                        getParentFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragmentNewServiceCard).commit();
                    }
                });
            }
        }
    };

    private void updateCard(Service_Card card) {
        Picasso.with(MyFireBaseDB.getMe().getContext()).load(card.getPicture()).into(card_IMG_user);
        card_LBL_fullname.setText(card.getFirstName() +" " + card.getLastName());
        card_LBL_phone.setText(card.getPhoneNum());
        card_LBL_price.setText(card.getPrice()+"$ per hour.");
        card_LBL_description.setText(card.getDetails());

        for(int i = 0 ; i< card.getStars(); i++){
            stars.get(i).setVisibility(View.VISIBLE);
        }

        if(card.getComments().size() ==0 ){
            card_TXT_nocomments.setVisibility(View.VISIBLE);
            comments_LST_items.setVisibility(View.GONE);
            card_TXT_nocomments.setText("No comments\\ ratings yet.");
        }
        else{
            ArrayList<Comment> comments = new ArrayList<>();
            Collection<Comment> values = card.getComments().values();
            ArrayList<Comment> commentsArr= new ArrayList<>(values);
            Log.d("pttt", "Setting the adapter :)");
            CommentAdapter.getMe().setComments(commentsArr);

            comments_LST_items.setLayoutManager(new LinearLayoutManager(getContext()));
            comments_LST_items.setHasFixedSize(true);
            comments_LST_items.setAdapter(CommentAdapter.getMe());
        }
    }
}