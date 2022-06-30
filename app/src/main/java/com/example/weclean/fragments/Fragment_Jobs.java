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
import com.example.weclean.adapters.JobsAdapter;
import com.example.weclean.data.Job;
import com.example.weclean.utils.MyFireBaseDB;

import java.util.ArrayList;

public class Fragment_Jobs extends Fragment {

    private ArrayList<Job> jobs;
    private RecyclerView jobs_LST_items;
    public Fragment_Jobs() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__list_jobs, container, false);
        findViews(view);
        setCallBacks();
        // Check if user has a service card
        MyFireBaseDB.getMe().checkIfUserHasServiceCard();

        return view;
    }

    private void findViews(View view) {
        jobs_LST_items = view.findViewById(R.id.jobs_LST_items);
    }

    private void setCallBacks() {
        MyFireBaseDB.getMe().setCallBack_getAllJobs(callBack_getAllJobs);
        MyFireBaseDB.getMe().setCallBack_userServiceGiver(callBack_userServiceGiver);
    }

    MyFireBaseDB.CallBack_getAllJobs callBack_getAllJobs = new MyFireBaseDB.CallBack_getAllJobs() {
        @Override
        public void allJobsReturned(ArrayList<Job> jobsReturned) {

            JobsAdapter.getMe().setJobs(jobsReturned);
            jobs_LST_items.setLayoutManager(new LinearLayoutManager(getContext()));
            jobs_LST_items.setHasFixedSize(true);
            jobs_LST_items.setAdapter(JobsAdapter.getMe());

        }
    };
    MyFireBaseDB.CallBack_UserServiceGiver callBack_userServiceGiver = new MyFireBaseDB.CallBack_UserServiceGiver() {
        @Override
        public void userHasServiceCard(boolean hasServiceCard) {
            if(hasServiceCard){
                MyFireBaseDB.getMe().getAllJobsFromDB();
            }
            else{
                // Put a button that suggests the user to open a service card to view potential job :)
                Log.d("pttt", "User dont have service card, suggest him to open one");
                Fragment_Service_Card fragmentServiceCard = new Fragment_Service_Card();
                Bundle bundle = new Bundle();
                bundle.putString("who", "mine");
                fragmentServiceCard.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragmentServiceCard).commit();

            }
        }
    };
}