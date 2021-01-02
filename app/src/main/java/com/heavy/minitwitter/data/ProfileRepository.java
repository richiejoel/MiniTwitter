package com.heavy.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.heavy.minitwitter.common.MyApp;
import com.heavy.minitwitter.retrofit.AuthMiniTwitterClient;
import com.heavy.minitwitter.retrofit.AuthMiniTwitterService;
import com.heavy.minitwitter.retrofit.response.ResponseUserProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    private AuthMiniTwitterClient authMiniTwitterClient;
    private AuthMiniTwitterService authMiniTwitterService;
    private MutableLiveData<ResponseUserProfile> userProfile;

    public ProfileRepository() {
        authMiniTwitterClient = AuthMiniTwitterClient.getInstance();
        authMiniTwitterService = authMiniTwitterClient.getAuthMiniTwitterService();
        userProfile = getProfile();
    }

    public MutableLiveData<ResponseUserProfile> getProfile() {
        if(userProfile == null) {
            userProfile = new MutableLiveData<>();
        }

        Call<ResponseUserProfile> call = authMiniTwitterService.mDoGetProfile();
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if(response.isSuccessful()) {
                    userProfile.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Error in Sever. Try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Trouble connection. Try again please.", Toast.LENGTH_SHORT).show();
            }
        });

        return userProfile;
    }
}
