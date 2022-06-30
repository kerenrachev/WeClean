package com.example.weclean.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.weclean.R;
import com.example.weclean.databinding.FragmentHomeBinding;

public class Fragment_Home extends Fragment {

    private FragmentHomeBinding binding;

    private TextView main_BTN_findJob;
    private TextView main_BTN_postJob;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        findViews(root);
        setClickListeners();
        return root;
    }

    private void setClickListeners() {
        main_BTN_postJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_New_Job fragmentNewJob = new Fragment_New_Job();
                getParentFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragmentNewJob).commit();
            }
        });

        main_BTN_findJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Jobs fragment_jobs = new Fragment_Jobs();
                getParentFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragment_jobs).commit();
            }
        });
    }

    private void findViews(View root) {
        Log.d("pttt","Im in home fragment!");
        main_BTN_postJob = root.findViewById(R.id.main_BTN_postJob);
        main_BTN_findJob = root.findViewById(R.id.main_BTN_findJob);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}