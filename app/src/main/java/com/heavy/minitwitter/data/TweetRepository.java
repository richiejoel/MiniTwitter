package com.heavy.minitwitter.data;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.heavy.minitwitter.common.Constants;
import com.heavy.minitwitter.common.MyApp;
import com.heavy.minitwitter.common.SharedPreferencesManager;
import com.heavy.minitwitter.retrofit.AuthMiniTwitterClient;
import com.heavy.minitwitter.retrofit.AuthMiniTwitterService;
import com.heavy.minitwitter.retrofit.request.RequestCreateTweet;
import com.heavy.minitwitter.retrofit.response.Like;
import com.heavy.minitwitter.retrofit.response.ResponseDeleteTweet;
import com.heavy.minitwitter.retrofit.response.Tweet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    private AuthMiniTwitterClient authMiniTwitterClient;
    private AuthMiniTwitterService authMiniTwitterService;
    MutableLiveData<List<Tweet>> tweetList;
    MutableLiveData<List<Tweet>> tweetFavsList;
    private String username;


    public TweetRepository() {
        authMiniTwitterClient = AuthMiniTwitterClient.getInstance();
        authMiniTwitterService = authMiniTwitterClient.getAuthMiniTwitterService();
        tweetList = getTweetList();
        this.username = SharedPreferencesManager.getSomeStringValue(Constants.PREF_USERNAME);

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

    public MutableLiveData<List<Tweet>> getFavsTweets() {
        if(tweetFavsList == null) {
            tweetFavsList = new MutableLiveData<>();
        }

        List<Tweet> newFavList = new ArrayList<>();
        Iterator itTweets = tweetList.getValue().iterator();

        while(itTweets.hasNext()) {
            Tweet current = (Tweet) itTweets.next();
            Iterator itLikes = current.getLikes().iterator();
            boolean enc = false;
            while (itLikes.hasNext() && !enc) {
                Like like = (Like)itLikes.next();
                if(like.getUsername().equals(username)) {
                    enc = true;
                    newFavList.add(current);
                }
            }
        }

        tweetFavsList.setValue(newFavList);

        return tweetFavsList;
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
                    getFavsTweets();

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

    public void mDeleteTweet(final int idTweet) {
        Call<ResponseDeleteTweet> call = authMiniTwitterService.mDoDeleteTweet(idTweet);
        call.enqueue(new Callback<ResponseDeleteTweet>() {
            @Override
            public void onResponse(Call<ResponseDeleteTweet> call, Response<ResponseDeleteTweet> response) {
                if(response.isSuccessful()){
                    Log.i("DELETE", "Delete succesful");
                    List<Tweet> tweetsCurrently = new ArrayList<>();
                    for(int i=0; i <  tweetList.getValue().size(); i++){
                        if(tweetList.getValue().get(i).getId() != idTweet){
                            tweetsCurrently.add(new Tweet(tweetList.getValue().get(i)));
                        }
                    }
                    tweetList.setValue(tweetsCurrently);
                    getFavsTweets();
                } else {
                    Toast.makeText(MyApp.getContext(), "Error in Sever. Try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDeleteTweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Trouble connection. Try again please.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
