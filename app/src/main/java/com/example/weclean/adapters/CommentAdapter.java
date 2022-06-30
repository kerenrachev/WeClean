package com.example.weclean.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclean.R;
import com.example.weclean.data.Comment;
import com.example.weclean.data.Service_Card;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hadi.emojiratingbar.EmojiRatingBar;
import com.hadi.emojiratingbar.RateStatus;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Comment> comments = new ArrayList<>();
    private Context context;
    private CommentAdapter(Context context) {
        this.context = context;
    }

    private static CommentAdapter me;

    public static CommentAdapter getMe() {
        return me;
    }

    public static CommentAdapter initHelper(Context context) {
        if (me == null) {
            me = new CommentAdapter(context);
        }
        return me;
    }
    public CommentAdapter(Activity activity, ArrayList<Comment> comments){
        this.activity = activity;
        this.comments = comments;
    }

    public void setComments(ArrayList<Comment> comments){
        this.comments = comments;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment, parent, false);
        CommentHolder commentHolder = new CommentHolder(view);
        return commentHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final CommentHolder holder = (CommentHolder) viewHolder;
        Comment comment = getItem(position);
        holder.comment_LBL_name.setText(comment.getFullName());
        holder.comment_TXT_content.setText(comment.getComment());

        // Set starts of comment
        for(int i = 0 ; i< comment.getStarts(); i++){
            holder.stars.get(i).setVisibility(View.VISIBLE);
        }

        Picasso.with(context).load(comment.getPicture()).placeholder(R.drawable.img_placeholder).into(holder.comment_IMG_user);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public Comment getItem(int position) {
        return comments.get(position);
    }

    public interface CAllBack_OpenCardDetails{
        void openCardDetails(Bundle bundle);
    }



    class CommentHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView comment_IMG_user;
        private MaterialTextView comment_LBL_name;
        private MaterialTextView comment_TXT_content;

        ArrayList<ImageView> stars = new ArrayList<>();
        public CommentHolder(View itemView) {
            super(itemView);
            comment_IMG_user = itemView.findViewById(R.id.comment_IMG_user);
            comment_LBL_name = itemView.findViewById(R.id.comment_LBL_name);
            comment_TXT_content = itemView.findViewById(R.id.comment_TXT_content);
            stars.add(itemView.findViewById(R.id.comment_IMG_star1));
            stars.add(itemView.findViewById(R.id.comment_IMG_star2));
            stars.add(itemView.findViewById(R.id.comment_IMG_star3));
            stars.add(itemView.findViewById(R.id.comment_IMG_star4));
            stars.add(itemView.findViewById(R.id.comment_IMG_star5));
        }

    }
}