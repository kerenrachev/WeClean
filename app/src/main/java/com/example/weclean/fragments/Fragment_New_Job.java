package com.example.weclean.fragments;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import com.example.weclean.R;
import com.example.weclean.data.Job;
import com.example.weclean.data.User;
import com.example.weclean.utils.FieldFilledValidator;
import com.example.weclean.utils.MyFireBaseAuth;
import com.example.weclean.utils.MyFireBaseDB;
import com.example.weclean.utils.MyPlacesAutoComplete;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Fragment_New_Job extends Fragment {

    private Fragment_GoogleMaps fragment_googleMaps;
    private AppCompatEditText newjob_TXT_description;
    private MaterialButton newjob_BTN_submit;
    private double currLat;
    private double currLng;
    private String locationName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_job, container, false);

        findViews(view);
        setClickListeners();
        initiateFragments();
        googlePlacesAutoComplete();
        return view;
    }


    private void setClickListeners() {
        newjob_BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate if card details filled
                ArrayList<AppCompatEditText> mandatoryFields = new ArrayList<>();
                mandatoryFields.add(newjob_TXT_description);

                if(locationName == null || locationName.length()==0 ){

                    Toast.makeText(getActivity().getApplicationContext() , "Please enter location.", Toast.LENGTH_SHORT).show();

                }
                else if(FieldFilledValidator.getMe().checkServiceCardDetails(mandatoryFields)){

                    Job job = new Job();
                    job.setDescription(newjob_TXT_description.getText()+ "")
                            .setUID(MyFireBaseAuth.getMe().getFireBaseUser().getUid())
                            .setId(UUID.randomUUID().toString())
                            .setLocation_name(locationName)
                            .setLat(currLat)
                            .setLng(currLng)
                            .setImgUrl(User.getMe().getPicture());

                    MyFireBaseDB.getMe().saveNewJob(job);

                    Fragment_Jobs jobsFragment = new Fragment_Jobs();
                    getParentFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, jobsFragment).commit();
                }


            }
        });
    }

    private void findViews(View view) {
        newjob_TXT_description = view.findViewById(R.id.newjob_TXT_description);
        newjob_BTN_submit = view.findViewById(R.id.newjob_BTN_submit);
    }

    private void googlePlacesAutoComplete() {

        MyPlacesAutoComplete.getMe().initPlaces();

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                fragment_googleMaps.setMapLocation(place.getLatLng().latitude, place.getLatLng().longitude);
                currLat = place.getLatLng().latitude;
                currLng = place.getLatLng().longitude;
                locationName = place.getName();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("pttt", "An error occurred: " + status);
            }
        });
    }

    private void initiateFragments() {

        fragment_googleMaps = new Fragment_GoogleMaps();
        getParentFragmentManager().beginTransaction().add(R.id.location_FRAME_map, fragment_googleMaps).commit();
    }

}