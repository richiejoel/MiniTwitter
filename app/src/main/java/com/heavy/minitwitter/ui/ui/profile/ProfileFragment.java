package com.heavy.minitwitter.ui.ui.profile;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.heavy.minitwitter.R;
import com.heavy.minitwitter.common.Constants;
import com.heavy.minitwitter.data.ProfileViewModel;
import com.heavy.minitwitter.data.TweetViewModel;
import com.heavy.minitwitter.retrofit.request.RequestUserProfile;
import com.heavy.minitwitter.retrofit.response.ResponseUserProfile;
import com.heavy.minitwitter.ui.DashboardActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    private FloatingActionButton fab;
    private Button btnSave, btnChangePassword;
    private CircleImageView imgAvatarProfile;
    private EditText edtUsernameProfile, edtEmailProfile, edtPasswordCurrentProfile, edtWebsiteProfile, edtDescriptionProfile;
    boolean loadingData = true;
    private PermissionListener allPermissionListener;

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
            String username = edtUsernameProfile.getText().toString();
            String email = edtEmailProfile.getText().toString();
            String descripcion = edtDescriptionProfile.getText().toString();
            String website = edtWebsiteProfile.getText().toString();
            String password = edtPasswordCurrentProfile.getText().toString();

            if(username.isEmpty()) {
                edtUsernameProfile.setError("Username is required");
            } else if(email.isEmpty()) {
                edtEmailProfile.setError("Email is required");
            } else if(password.isEmpty()) {
                edtPasswordCurrentProfile.setError("Pasword is required");
            } else {
                RequestUserProfile requestUserProfile = new RequestUserProfile(username, email, descripcion, website, password);
                profileViewModel.mUpdateProfile(requestUserProfile);
                Toast.makeText(getActivity(), "Sent information to server", Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(false);
            }
        });

        btnChangePassword.setOnClickListener(v -> {
            System.out.println("Hii");
        });

        imgAvatarProfile.setOnClickListener(v -> {
            mCheckPermissions();
        });

        profileViewModel.userProfile.observe(getActivity(), new Observer<ResponseUserProfile>() {
            @Override
            public void onChanged(@Nullable ResponseUserProfile responseUserProfile) {
                loadingData = false;
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
                    Glide.with(getContext())
                            .load(Constants.API_MINITWITTER_FILES_URL + responseUserProfile.getPhotoUrl())
                            .dontAnimate()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(imgAvatarProfile);
                }
                if(!loadingData) {
                    btnSave.setEnabled(true);
                    Toast.makeText(getActivity(), "Operation SuccessFull", Toast.LENGTH_SHORT).show();
                }
            }
        });

        profileViewModel.photoProfile.observe(getActivity(), photo -> {
            Glide.with(getContext())
                    .load(Constants.API_MINITWITTER_FILES_URL + photo)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .into(imgAvatarProfile);
        });



        return view;
    }

    private void mCheckPermissions() {
        PermissionListener dialogOnDeniedPermissionListener =
                DialogOnDeniedPermissionListener.Builder.withContext(getContext())
                        .withTitle("Permisos")
                        .withMessage("Los permisos solicitados son necesarios para poder seleccionar una foto de perfil")
                        .withButtonText("Aceptar")
                        .withIcon(R.mipmap.ic_launcher)
                        .build();

        allPermissionListener = new CompositePermissionListener(
                (PermissionListener) getActivity(),
                dialogOnDeniedPermissionListener
        );

        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(allPermissionListener)
                .check();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
