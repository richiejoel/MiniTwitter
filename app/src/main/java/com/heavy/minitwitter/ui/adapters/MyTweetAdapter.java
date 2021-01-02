package com.heavy.minitwitter.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.heavy.minitwitter.R;
import com.heavy.minitwitter.common.Constants;
import com.heavy.minitwitter.common.SharedPreferencesManager;
import com.heavy.minitwitter.data.TweetViewModel;
import com.heavy.minitwitter.retrofit.response.Like;
import com.heavy.minitwitter.retrofit.response.Tweet;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyTweetAdapter extends RecyclerView.Adapter<MyTweetAdapter.MyTweetViewHolder> {

    private ArrayList<Tweet> listTweet;
    private Context ctx;
    String username;
    TweetViewModel tweetViewModel;

    public MyTweetAdapter(ArrayList<Tweet> listTweet, Context ctx){
        this.listTweet = listTweet;
        this.ctx = ctx;
        this.username = SharedPreferencesManager.getSomeStringValue(Constants.PREF_USERNAME);
        //new ViewModelProvider((ViewModelStoreOwner) getContext()).get(TweetViewModel.class);
        this.tweetViewModel = new ViewModelProvider((ViewModelStoreOwner) ctx).get(TweetViewModel.class);

        //this.tweetViewModel = ViewModelProviders.of((FragmentActivity) ctx).get(TweetViewModel.class);
    }

    @NonNull
    @Override
    public MyTweetAdapter.MyTweetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tweet, parent, false);
        return new MyTweetAdapter.MyTweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTweetAdapter.MyTweetViewHolder holder, int position) {

        if(!this.listTweet.isEmpty()){
            holder.tweet = listTweet.get(position);
            holder.txtMessage.setText(holder.tweet.getMensaje());
            holder.txtUsername.setText("@" + holder.tweet.getUser().getUsername());
            holder.txtViewLikes.setText(String.valueOf(holder.tweet.getLikes().size()));
            holder.imgShowMenu.setVisibility(View.GONE);
            if(holder.tweet.getUser().getUsername().equals(username)){
                holder.imgShowMenu.setVisibility(View.VISIBLE);
            }

            holder.imgLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tweetViewModel.mLikeDislikeTweet(holder.tweet.getId());
                }
            });

            holder.imgShowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tweetViewModel.openDialogTweetMenu(ctx,holder.tweet.getId());
                }
            });

            String photo = holder.tweet.getUser().getPhotoUrl();
            if(!photo.isEmpty()){
                Glide.with(ctx)
                        .load("https://www.minitwitter.com/apiv1/uploads/photos/" + photo)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(holder.imgTweetAvatar);
            }

            Glide.with(ctx)
                    .load(R.drawable.ic_like)
                    .into(holder.imgLike);
            holder.txtViewLikes.setTextColor(ctx.getResources().getColor(android.R.color.black));
            holder.txtViewLikes.setTypeface(null, Typeface.NORMAL);

            for(Like like: holder.tweet.getLikes()) {
                if(like.getUsername().equals(username)) {
                    Glide.with(ctx)
                            .load(R.drawable.ic_like_pink)
                            .into(holder.imgLike);
                    holder.txtViewLikes.setTextColor(ctx.getResources().getColor(R.color.colorPink));
                    holder.txtViewLikes.setTypeface(null, Typeface.BOLD);
                    break;
                }
            }
        }

    }

    public void setData(List<Tweet> tweetList){
        this.listTweet = (ArrayList<Tweet>) tweetList;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {

        if(!this.listTweet.isEmpty()){
            return this.listTweet.size();
        } else {
            return 0;
        }

    }

    public class MyTweetViewHolder extends RecyclerView.ViewHolder {

        TextView txtUsername, txtMessage, txtViewLikes;
        ImageView imgLike, imgShowMenu;
        CircleImageView imgTweetAvatar;
        public Tweet tweet;

        public MyTweetViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtViewLikes = itemView.findViewById(R.id.txtViewLikes);
            imgLike = itemView.findViewById(R.id.imgLike);
            imgTweetAvatar = itemView.findViewById(R.id.imgTweetAvatar);
            imgShowMenu = itemView.findViewById(R.id.imgShowMenu);
        }

    }
}
