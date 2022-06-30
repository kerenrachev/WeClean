package com.example.weclean.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.SoundPool;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclean.R;
import com.example.weclean.data.Job;
import com.example.weclean.data.Notification;
import com.example.weclean.data.Notification_type;
import com.example.weclean.data.User;
import com.example.weclean.utils.MyAlertDialog;
import com.example.weclean.utils.MyFireBaseAuth;
import com.example.weclean.utils.MyFireBaseDB;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.ServerValue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class JobsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Job> jobs = new ArrayList<>();
    private Context context;
    private JobsAdapter(Context context) {
        this.context = context;
    }

    private static JobsAdapter me;

    public static JobsAdapter getMe() {
        return me;
    }

    public static JobsAdapter initHelper(Context context) {
        if (me == null) {
            me = new JobsAdapter(context);
        }
        return me;
    }

    public JobsAdapter setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public void setJobs(ArrayList<Job> allJobs){
        this.jobs = allJobs;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_jobs, parent, false);
        JobHolder jobHolder = new JobHolder(view);
        return jobHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final JobHolder holder = (JobHolder) viewHolder;
        Job job = getItem(position);

        holder.job_LBL_location.setText(job.getLocation_name());
        holder.job_LBL_description.setText(job.getDescription());
        //holder.job_LBL_price.setText("$22 " );
        Picasso.with(context).load(job.getImgUrl()).placeholder(R.drawable.img_placeholder).into(holder.jobs_IMG_user);
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public Job getItem(int position) {
        return jobs.get(position);
    }



    class JobHolder extends RecyclerView.ViewHolder {

        //private AppCompatImageView job_IMG_image;
        private MaterialTextView job_LBL_location;
        private MaterialTextView job_LBL_description;
        private MaterialTextView job_LBL_price;
        private MaterialButton job_BTN_offer;
        private AppCompatImageView jobs_IMG_user;

        public JobHolder(View itemView) {
            super(itemView);
            jobs_IMG_user = itemView.findViewById(R.id.jobs_IMG_user);
            job_LBL_location = itemView.findViewById(R.id.job_LBL_location);
            job_LBL_description = itemView.findViewById(R.id.job_LBL_description);
            // = itemView.findViewById(R.id.job_LBL_price);
            job_BTN_offer = itemView.findViewById(R.id.job_BTN_offer);



            job_BTN_offer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyAlertDialog.CallBack_agree callBack_agree = new MyAlertDialog.CallBack_agree() {
                        @Override
                        public void agreeToOfferService() {
                            Notification newNotification = new Notification();
                            newNotification.setUid(MyFireBaseAuth.getMe().getFireBaseUser().getUid())
                                    .setPicture(User.getMe().getPicture())
                                    .setNotification_type(Notification_type.OFFER)
                                    .setSeen(false)
                                    .setNotificationId(UUID.randomUUID().toString())
                                    .setText(User.getMe().getFirstName() +" "+ User.getMe().getLastName() + " made an offer for the job you have posted!")
                                    .setSendToUID(jobs.get(getAdapterPosition()).getUID())
                                    .setTimeStamp(Calendar.getInstance().getTimeInMillis());
                            MyFireBaseDB.getMe().createNotification(newNotification);
                        }
                    };
                    MyAlertDialog.getMe().setCallBack_agree(callBack_agree);
                    MyAlertDialog.getMe().startAlertDialog(activity);

                }
            });
        }

    }
}