package com.heavy.minitwitter.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.heavy.minitwitter.R;
import com.heavy.minitwitter.common.Constants;
import com.heavy.minitwitter.common.SharedPreferencesManager;
import com.heavy.minitwitter.data.TweetViewModel;
import com.heavy.minitwitter.ui.ui.home.HomeFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewTweetDialogFragment extends DialogFragment implements View.OnClickListener {

    ImageView imgCloseDialog;
    CircleImageView imgProfileDialog;
    Button btnTweeter;
    EditText edtMessagesDialog;
    private TweetViewModel tweetViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.new_tweet_full_dialog, container, false);
        imgCloseDialog = view.findViewById(R.id.imgCloseDialog);
        imgProfileDialog = view.findViewById(R.id.imgProfileDialog);
        btnTweeter = view.findViewById(R.id.btnTweeter);
        edtMessagesDialog = view.findViewById(R.id.edtMessagesDialog);

        imgCloseDialog.setOnClickListener(this);
        btnTweeter.setOnClickListener(this);

        String photoUrl = SharedPreferencesManager.getSomeStringValue(Constants.PREF_PHOTOURL);
        if(!photoUrl.isEmpty()){
            Glide.with(getActivity())
                    .load(Constants.API_MINITWITTER_FILES_URL + photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imgProfileDialog);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgCloseDialog:
                mCloseDialog();
                break;
            case R.id.btnTweeter:
                mCreateTweet();
                break;
        }
    }

    private void mCreateTweet(){
        if(edtMessagesDialog.getText().toString().isEmpty()){
            Toast.makeText(getContext(),"Please, write a tweet", Toast.LENGTH_SHORT).show();
            return;
        }
        this.tweetViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
        this.tweetViewModel.mCreateTweet(edtMessagesDialog.getText().toString());
        /*HomeFragment object = new HomeFragment();
        object.getAdapter().notifyDataSetChanged();*/
        getDialog().dismiss();
    }

    private void mCloseDialog(){
        if(!edtMessagesDialog.getText().toString().isEmpty()){
            mShowAlertDialogConfirm();
            return;
        }
         getDialog().dismiss();

    }

    private void mShowAlertDialogConfirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_message_confirm)
                .setTitle(R.string.dialog_title_confirm);
        builder.setPositiveButton(R.string.dialog_btn_delete_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               dialog.dismiss();
               getDialog().dismiss();
            }
        });
        builder.setNegativeButton(R.string.dialog_btn_cancel_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
