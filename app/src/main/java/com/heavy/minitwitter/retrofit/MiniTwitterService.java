package com.heavy.minitwitter.retrofit;

import com.heavy.minitwitter.RequestLogin;
import com.heavy.minitwitter.RequestSignUp;
import com.heavy.minitwitter.ResponseLogin;
import com.heavy.minitwitter.ResponseSignUp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MiniTwitterService {

    @POST("/auth/login")
    Call<ResponseLogin> mDoLogin(@Body RequestLogin requestLogin);

    @POST("/auth/signup")
    Call<ResponseSignUp> mDoSignUp(@Body RequestSignUp requestSignUp);
}
