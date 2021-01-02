package com.heavy.minitwitter.ui.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.heavy.minitwitter.R;
import com.heavy.minitwitter.common.Constants;
import com.heavy.minitwitter.data.ProfileViewModel;
import com.heavy.minitwitter.data.TweetViewModel;
import com.heavy.minitwitter.retrofit.response.ResponseUserProfile;
import com.heavy.minitwitter.ui.DashboardActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    private FloatingActionButton fab;
    private Button btnSave, btnChangePassword;
    private CircleImageView imgAvatarProfile;
    private EditText edtUsernameProfile, edtEmailProfile, edtPasswordCurrentProfile, edtWebsiteProfile, edtDescriptionProfile;

    /*
    private ProfileViewModel notificationsViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.profileViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(ProfileViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imgAvatarProfile = view.findViewById(R.id.imgAvatarProfile);
        edtUsernameProfile = view.findViewById(R.id.edtUsernameProfile);
        edtEmailProfile = view.findViewById(R.id.edtEmailProfile);
        edtWebsiteProfile = view.findViewById(R.id.edtWebsiteProfile);
        edtDescriptionProfile = view.findViewById(R.id.edtDescriptionProfile);
        edtPasswordCurrentProfile = view.findViewById(R.id.edtPasswordCurrentProfile);
        btnSave = view.findViewById(R.id.btnSave);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        fab = ((DashboardActivity) getActivity()).getFab();
        fab.hide();

        btnSave.setOnClickListener(v -> {
            System.out.println("Hii");
        });

        btnChangePassword.setOnClickListener(v -> {
            System.out.println("Hii");
        });

        profileViewModel.userProfile.observe(getActivity(), new Observer<ResponseUserProfile>() {
            @Override
            public void onChanged(@Nullable ResponseUserProfile responseUserProfile) {
                if(responseUserProfile.getDescripcion().isEmpty()){
                    responseUserProfile.setDescripcion("");
                }
                if(responseUserProfile.getWebsite().isEmpty()){
                    responseUserProfile.setDescripcion("");
                }
                edtUsernameProfile.setText(responseUserProfile.getUsername());
                edtEmailProfile.setText(responseUserProfile.getEmail());
                edtWebsiteProfile.setText(responseUserProfile.getWebsite());
                edtDescriptionProfile.setText(responseUserProfile.getDescripcion());
                if(!responseUserProfile.getPhotoUrl().isEmpty()) {
                    Glide.with(getActivity())
                            .load(Constants.API_MINITWITTER_FILES_URL + responseUserProfile.getPhotoUrl())
                            .into(imgAvatarProfile);
                }
            }
        });



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
