package com.heavy.minitwitter.data;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.heavy.minitwitter.common.MyApp;
import com.heavy.minitwitter.retrofit.AuthMiniTwitterClient;
import com.heavy.minitwitter.retrofit.AuthMiniTwitterService;
import com.heavy.minitwitter.retrofit.request.RequestCreateTweet;
import com.heavy.minitwitter.retrofit.response.Tweet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    private AuthMiniTwitterClient authMiniTwitterClient;
    private AuthMiniTwitterService authMiniTwitterService;
    MutableLiveData<List<Tweet>> tweetList;


    public TweetRepository() {
        authMiniTwitterClient = AuthMiniTwitterClient.getInstance();
        authMiniTwitterService = authMiniTwitterClient.getAuthMiniTwitterService();
        tweetList = getTweetList();

    }

    public MutableLiveData<List<Tweet>> getTweetList(){
        //final MutableLiveData<List<Tweet>> data = new MutableLiveData<>();
        if(tweetList == null){
            tweetList = new MutableLiveData<>();
        }

        Call<List<Tweet>> call = authMiniTwitterService.mDoAllGetTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful()){
                    tweetList.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Error in Sever. Try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Trouble connection. Try again please.", Toast.LENGTH_SHORT).show();
            }
        });


        return tweetList;
    }

    public void mCreateTweet(String message){
        RequestCreateTweet requestCreateTweet = new RequestCreateTweet(message);
        Call<Tweet> call = authMiniTwitterService.mDoCreateTweet(requestCreateTweet);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()){
                    List<Tweet> tweetsCurrently = new ArrayList<>();
                    tweetsCurrently.add(response.body());
                    /*if(tweetList.getValue() !=  null){
                        for(int i=0; i < tweetList.getValue().size(); i++) {
                            tweetsCurrently.add(new Tweet(tweetList.getValue().get(i)));
                        }
                        tweetList.setValue(tweetsCurrently);
                    }*/

                     if(tweetList.getValue() != null){
                        tweetsCurrently.addAll(tweetList.getValue());
                        tweetList.setValue(tweetsCurrently);
                        Log.i("TPWK", "onResponse: "+tweetList.getValue().get(0).getMensaje());
                    }

                } else {
                    Toast.makeText(MyApp.getContext(), "Error in Sever. Try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Trouble connection. Try again please.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void mLikeDislikeTweet(int idTweet){
        Call<Tweet> call = authMiniTwitterService.mDoLikeDislikeTweet(idTweet);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()){
                    List<Tweet> tweetsCurrently = new ArrayList<>();
                    for(int i=0; i < tweetList.getValue().size(); i++ ){
                        if(tweetList.getValue().get(i).getId().equals(idTweet)){
                            tweetsCurrently.add(response.body());
                        } else {
                            tweetsCurrently.add(new Tweet(tweetList.getValue().get(i)));
                        }
                    }
                    tweetList.setValue(tweetsCurrently);

                } else {
                    Toast.makeText(MyApp.getContext(), "Error in Sever. Try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Trouble connection. Try again please.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
