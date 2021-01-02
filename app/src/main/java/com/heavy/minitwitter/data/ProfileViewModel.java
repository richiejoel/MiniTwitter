package com.heavy.minitwitter.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.heavy.minitwitter.retrofit.response.ResponseUserProfile;

public class ProfileViewModel extends AndroidViewModel {

    public ProfileRepository profileRepository;
    public LiveData<ResponseUserProfile> userProfile;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository();
        userProfile = profileRepository.getProfile();
    }
}