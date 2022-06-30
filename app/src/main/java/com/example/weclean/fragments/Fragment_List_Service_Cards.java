package com.example.weclean.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weclean.R;
import com.example.weclean.adapters.CardsAdapter;
import com.example.weclean.data.Service_Card;
import com.example.weclean.utils.MyFireBaseDB;

import java.util.ArrayList;

public class Fragment_List_Service_Cards extends Fragment {

    private RecyclerView cards_LST_items;
    public Fragment_List_Service_Cards() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment__list_service_cards, container, false);
        findViews(view);
        MyFireBaseDB.getMe().getAllServiceCardsFromDB();
        setCallBacks();


        return view;
    }
    CardsAdapter.CAllBack_OpenCardDetails cAllBack_openCardDetails = new CardsAdapter.CAllBack_OpenCardDetails() {
        @Override
        public void openCardDetails(Bundle bundle) {

            Fragment_Service_Card fragmentServiceCard = new Fragment_Service_Card();
            fragmentServiceCard.setArguments(bundle);
            getParentFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragmentServiceCard).commit();
        }
    };

    private void setCallBacks() {
        MyFireBaseDB.getMe().setCallBack_getAllServiceCards(callBack_getAllServiceCards);
        CardsAdapter.getMe().setcAllBack_openCardDetails(cAllBack_openCardDetails);
    }

    MyFireBaseDB.CallBack_getAllServiceCards callBack_getAllServiceCards = new MyFireBaseDB.CallBack_getAllServiceCards() {
        @Override
        public void serviceCardsReturned(ArrayList<Service_Card> allCards) {
            Log.d("pttt"," All cards returned! length: " + allCards.size());
            CardsAdapter.getMe().setServiceCards(allCards);
            cards_LST_items.setLayoutManager(new LinearLayoutManager(getContext()));
            cards_LST_items.setHasFixedSize(true);
            cards_LST_items.setAdapter(CardsAdapter.getMe());
        }
    };



    private void findViews(View view) {
        cards_LST_items = view.findViewById(R.id.cards_LST_items);
    }
}