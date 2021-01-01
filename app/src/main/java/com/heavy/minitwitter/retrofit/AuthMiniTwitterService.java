package com.heavy.minitwitter.retrofit;

import com.heavy.minitwitter.retrofit.request.RequestCreateTweet;
import com.heavy.minitwitter.retrofit.request.RequestSignUp;
import com.heavy.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthMiniTwitterService {

    @Headers("Content-Type: application/json")
    @GET("tweets/all")
    Call<List<Tweet>> mDoAllGetTweets();

    @Headers("Content-Type: application/json")
    @POST("tweets/create")
    Call<Tweet> mDoCreateTweet(@Body RequestCreateTweet requestCreateTweet);

}
