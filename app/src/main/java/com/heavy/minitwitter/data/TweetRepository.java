package com.heavy.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.heavy.minitwitter.common.MyApp;
import com.heavy.minitwitter.retrofit.AuthMiniTwitterClient;
import com.heavy.minitwitter.retrofit.AuthMiniTwitterService;
import com.heavy.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    private AuthMiniTwitterClient authMiniTwitterClient;
    private AuthMiniTwitterService authMiniTwitterService;
    LiveData<List<Tweet>> tweetList;

    public TweetRepository() {
        authMiniTwitterClient = AuthMiniTwitterClient.getInstance();
        authMiniTwitterService = authMiniTwitterClient.getAuthMiniTwitterService();
        this.tweetList = getTweetList();
    }

    public LiveData<List<Tweet>> getTweetList(){
        final MutableLiveData<List<Tweet>> data = new MutableLiveData<>();

        Call<List<Tweet>> call = authMiniTwitterService.mDoAllGetTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful()){
                    data.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Error in Sever. Try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Trouble connection. Try again please.", Toast.LENGTH_SHORT).show();
            }
        });


        return data;
    }
}
