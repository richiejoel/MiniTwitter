package com.heavy.minitwitter.retrofit;

import com.heavy.minitwitter.common.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MiniTwitterClient {

    private static MiniTwitterClient obInstance = null;
    private MiniTwitterService miniTwitterService;
    private Retrofit retrofit;

    public MiniTwitterClient(){
        this.retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_MINITWITTER_URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.miniTwitterService = this.retrofit.create(MiniTwitterService.class);
    }

    public static MiniTwitterClient getInstance(){
        if(obInstance == null){
            obInstance = new MiniTwitterClient();
        }
        return obInstance;
    }

    public MiniTwitterService getMiniTwitterService(){
        return this.miniTwitterService;
    }
}
