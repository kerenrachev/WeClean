package com.example.weclean.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclean.R;
import com.example.weclean.data.Service_Card;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Service_Card> service_cards = new ArrayList<>();
    private Context context;
    private CAllBack_OpenCardDetails cAllBack_openCardDetails;
    private CardsAdapter(Context context) {
        this.context = context;
    }

    private static CardsAdapter me;

    public static CardsAdapter getMe() {
        return me;
    }

    public static CardsAdapter initHelper(Context context) {
        if (me == null) {
            me = new CardsAdapter(context);
        }
        return me;
    }
    public CardsAdapter(Activity activity, ArrayList<Service_Card> service_cards){
        this.activity = activity;
        this.service_cards = service_cards;
    }

    public CardsAdapter setcAllBack_openCardDetails(CAllBack_OpenCardDetails cAllBack_openCardDetails) {
        this.cAllBack_openCardDetails = cAllBack_openCardDetails;
        return this;
    }

    public void setServiceCards(ArrayList<Service_Card> allCards){
        this.service_cards = allCards;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_servicecard, parent, false);
        CardHolder cardHolder = new CardHolder(view);
        return cardHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final CardHolder holder = (CardHolder) viewHolder;
        Service_Card card = getItem(position);

        holder.cards_LBL_fullname.setText(card.getFirstName() +" "+ card.getLastName());
        holder.cards_LBL_phoneNum.setText(card.getPhoneNum());
        holder.cards_LBL_price.setText(card.getPrice() +" $$");
        holder.card_TXT_numOfRatings.setText("( " +card.getComments().size() +" rated )");
        for(int i = 0 ; i< card.getStars(); i++){
            holder.stars.get(i).setVisibility(View.VISIBLE);
        }
        Picasso.with(context).load(card.getPicture()).placeholder(R.drawable.img_placeholder).into(holder.cards_IMG_user);
        holder.setService_card(card);
    }

    @Override
    public int getItemCount() {
        return service_cards.size();
    }

    public Service_Card getItem(int position) {
        return service_cards.get(position);
    }

    public interface CAllBack_OpenCardDetails{
        void openCardDetails(Bundle bundle);
    }


    class CardHolder extends RecyclerView.ViewHolder {

        //private AppCompatImageView job_IMG_image;
        private MaterialTextView cards_LBL_fullname;
        private MaterialTextView cards_LBL_phoneNum;
        private MaterialTextView cards_LBL_price;
        private MaterialButton cards_BTN_moreDetails;
        private AppCompatImageView cards_IMG_user;
        private ArrayList<ImageView> stars = new ArrayList<>();
        private TextView card_TXT_numOfRatings;
        private String id;
        private Service_Card service_card;

        public CardHolder(View itemView) {
            super(itemView);
            cards_IMG_user = itemView.findViewById(R.id.cards_IMG_user);
            cards_LBL_fullname = itemView.findViewById(R.id.cards_LBL_fullname);
            cards_LBL_phoneNum = itemView.findViewById(R.id.cards_LBL_phoneNum);
            cards_LBL_price = itemView.findViewById(R.id.cards_LBL_price);
            cards_BTN_moreDetails = itemView.findViewById(R.id.cards_BTN_moreDetails);
            stars.add(itemView.findViewById(R.id.card_IMG_star1));
            stars.add(itemView.findViewById(R.id.card_IMG_star2));
            stars.add(itemView.findViewById(R.id.card_IMG_star3));
            stars.add(itemView.findViewById(R.id.card_IMG_star4));
            stars.add(itemView.findViewById(R.id.card_IMG_star5));
            card_TXT_numOfRatings = itemView.findViewById(R.id.card_TXT_numOfRatings);
            cards_BTN_moreDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("who", "someoneElse");
                    String JSONServiceCard= new Gson().toJson(service_card,new TypeToken<Service_Card>(){}.getType());
                    bundle.putString("Card", JSONServiceCard);
                    cAllBack_openCardDetails.openCardDetails(bundle);

                    //getParentFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, serviceCardFragment).commit();
                }
            });
        }

        private void setService_card(Service_Card service_card){
            this.service_card = service_card;
        }

    }
}