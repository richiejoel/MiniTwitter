package com.heavy.minitwitter.retrofit;

import com.heavy.minitwitter.retrofit.request.RequestLogin;
import com.heavy.minitwitter.retrofit.request.RequestSignUp;
import com.heavy.minitwitter.retrofit.response.ResponseLogin;
import com.heavy.minitwitter.retrofit.response.ResponseSignUp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MiniTwitterService {

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    Call<ResponseLogin> mDoLogin(@Body RequestLogin requestLogin);

    @Headers("Content-Type: application/json")
    @FormUrlEncoded
    @POST("auth/signup")
    Call<ResponseSignUp> mDoSignUp(@Body RequestSignUp requestSignUp);
}
