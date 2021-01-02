package com.heavy.minitwitter.data;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.heavy.minitwitter.retrofit.response.Tweet;
import com.heavy.minitwitter.ui.ui.BottomModalTweetFragment;

import java.util.List;

public class TweetViewModel extends AndroidViewModel {

    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> tweets;
    private LiveData<List<Tweet>> favTweets;


    public TweetViewModel(@NonNull Application application) {
        super(application);
        tweetRepository = new TweetRepository();
        this.tweets = tweetRepository.getTweetList();
    }

    public LiveData<List<Tweet>> getTweets(){
        return this.tweets;
    }

    public LiveData<List<Tweet>> getNewTweets(){
        this.tweets = tweetRepository.getTweetList();
        return this.tweets;
    }

    public void mCreateTweet(String message){
        tweetRepository.mCreateTweet(message);
    }

    public void mLikeDislikeTweet(int idTweet){
        tweetRepository.mLikeDislikeTweet(idTweet);
    }

    public LiveData<List<Tweet>> getFavTweets() {
        favTweets = tweetRepository.getFavsTweets();
        return favTweets;
    }

    public LiveData<List<Tweet>> getNewFavTweets() {
        getNewTweets();
        return getFavTweets();
    }

    public void mDeleteTweet(int idTweet){
        tweetRepository.mDeleteTweet(idTweet);
    }

    public void openDialogTweetMenu(Context ctx, int idTweet) {
        BottomModalTweetFragment dialogTweet = BottomModalTweetFragment.newInstance(idTweet);
        dialogTweet.show(((AppCompatActivity)ctx).getSupportFragmentManager(), "BottomModalTweetFragment");
    }

}
