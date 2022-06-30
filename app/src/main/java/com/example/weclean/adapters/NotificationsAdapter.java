package com.example.weclean.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclean.R;
import com.example.weclean.fragments.Fragment_Service_Card;
import com.example.weclean.data.Notification;
import com.example.weclean.data.Notification_type;
import com.example.weclean.data.User;
import com.example.weclean.utils.MyFireBaseDB;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private Activity activity;
    private ArrayList<Notification> notifications = new ArrayList<>();
    private Context context;
    private CallBack_closeNotificationsWindow callBack_closeNotificationsWindow;


    private NotificationsAdapter(Context context) {
        this.context = context;
    }

    private static NotificationsAdapter me;

    public static NotificationsAdapter getMe() {
        return me;
    }

    public static NotificationsAdapter initHelper(Context context) {
        if (me == null) {
            me = new NotificationsAdapter(context);
        }
        return me;
    }

    public NotificationsAdapter setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public interface CallBack_closeNotificationsWindow{
        void closeNotifications();
    }

    public NotificationsAdapter setCallBack_closeNotificationsWindow(CallBack_closeNotificationsWindow callBack_closeNotificationsWindow) {
        this.callBack_closeNotificationsWindow = callBack_closeNotificationsWindow;
        return this;
    }

    public void setNotifications(ArrayList<Notification> allNotifications){

        // Making a copy of the notifications array to get the correct seen/unseen value before user opened notifications.
        // (This value is updated instantly after the user clicks on the notifications icon and a CallBack is called which updates User's instance notifications array)
        this.notifications = new ArrayList<>();
        for(Notification notification : allNotifications){
            notifications.add(new Notification(notification));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notification, parent, false);
        NotificationHolder notificationHolder = new NotificationHolder(view);
        // Move new notifications to old (Seen already)
        MyFireBaseDB.getMe().updateNotificationsSeen(User.getMe().getUID(), User.getMe().getNotifications());
        return notificationHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final NotificationHolder holder = (NotificationHolder) viewHolder;
        Notification notification = getItem(position);
        holder.notification_LBL_description.setText(notification.getText());
        Picasso.with(context).load(notification.getPicture()).placeholder(R.drawable.img_placeholder).into(holder.notifications_IMG_user);

        switch (notification.getNotification_type()){
            case OFFER:
                holder.notifications_badge_icon.setImageResource(R.drawable.ic_offer);
                break;
            case COMMENT:
                holder.notifications_badge_icon.setImageResource(R.drawable.ic_comment);
                break;
            default:
                holder.notifications_badge_icon.setImageResource(0);
                break;
        }
        if (!notification.isSeen()){
            holder.notitication_IMG_newNotification.setVisibility(View.VISIBLE);
        }
        else{
            holder.notitication_IMG_newNotification.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public Notification getItem(int position) {
        return notifications.get(position);
    }

    class NotificationHolder extends RecyclerView.ViewHolder {

        private MaterialTextView notification_LBL_description;
        private ShapeableImageView notifications_IMG_user;
        private ImageView notifications_badge_icon;
        private MaterialCardView card;
        private ImageView notitication_IMG_newNotification;
        public NotificationHolder(View itemView) {
            super(itemView);
            notifications_IMG_user = itemView.findViewById(R.id.notifications_IMG_user);
            notification_LBL_description = itemView.findViewById(R.id.notification_LBL_description);
            notifications_badge_icon = itemView.findViewById(R.id.notifications_badge_icon);
            notitication_IMG_newNotification = itemView.findViewById(R.id.notitication_IMG_newNotification);
            card = itemView.findViewById(R.id.card);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(notifications.get(getAdapterPosition()).getNotification_type().equals(Notification_type.COMMENT)){
                        Fragment_Service_Card fragmentServiceCard = new Fragment_Service_Card();
                        Bundle bundle = new Bundle();
                        bundle.putString("who", "mine");
                        fragmentServiceCard.setArguments(bundle);
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        callBack_closeNotificationsWindow.closeNotifications();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragmentServiceCard).commit();
                    }

                }
            });


        }

    }
}
