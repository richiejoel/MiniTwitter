package com.heavy.minitwitter.retrofit;

import com.heavy.minitwitter.retrofit.request.RequestCreateTweet;
import com.heavy.minitwitter.retrofit.request.RequestUserProfile;
import com.heavy.minitwitter.retrofit.response.ResponseDeleteTweet;
import com.heavy.minitwitter.retrofit.response.ResponseUserProfile;
import com.heavy.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AuthMiniTwitterService {

    //Tweets
    @Headers("Content-Type: application/json")
    @GET("tweets/all")
    Call<List<Tweet>> mDoAllGetTweets();

    @Headers("Content-Type: application/json")
    @POST("tweets/create")
    Call<Tweet> mDoCreateTweet(@Body RequestCreateTweet requestCreateTweet);

    @Headers("Content-Type: application/json")
    @POST("tweets/like/{idTweet}")
    Call<Tweet> mDoLikeDislikeTweet(@Path("idTweet") int idTweet);

    @Headers("Content-Type: application/json")
    @DELETE("tweets/{idTweet}")
    Call<ResponseDeleteTweet> mDoDeleteTweet(@Path("idTweet") int idTweet);


    //Users
    @Headers("Content-Type: application/json")
    @GET("users/profile")
    Call<ResponseUserProfile> mDoGetProfile();

    @Headers("Content-Type: application/json")
    @PUT("users/profile")
    Call<ResponseUserProfile> mDoUpdateProfile(@Body RequestUserProfile requestUserProfile);



}
