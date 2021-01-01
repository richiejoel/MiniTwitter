package com.heavy.minitwitter.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.heavy.minitwitter.retrofit.response.Tweet;

import java.util.List;

public class TweetViewModel extends AndroidViewModel {

    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> tweets;

    public TweetViewModel(@NonNull Application application) {
        super(application);
        tweetRepository = new TweetRepository();
        this.tweets = tweetRepository.getTweetList();
    }

    public LiveData<List<Tweet>> getTweets(){
        return this.tweets;
    }

    public void mCreateTweet(String message){
        tweetRepository.mCreateTweet(message);
    }

}
