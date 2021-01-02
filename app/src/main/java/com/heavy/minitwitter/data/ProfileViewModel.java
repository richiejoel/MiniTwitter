package com.heavy.minitwitter.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.heavy.minitwitter.retrofit.request.RequestUserProfile;
import com.heavy.minitwitter.retrofit.response.ResponseUserProfile;

public class ProfileViewModel extends AndroidViewModel {

    public ProfileRepository profileRepository;
    public LiveData<ResponseUserProfile> userProfile;
    public LiveData<String> photoProfile;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository();
        userProfile = profileRepository.getProfile();
        photoProfile = profileRepository.getPhotoProfile();
    }

    public void mUpdateProfile(RequestUserProfile requestUserProfile){
        profileRepository.mUpdateProfile(requestUserProfile);
    }

    public void mUploadPhoto(String photo) {
        profileRepository.mUploadPhoto(photo);
    }
}