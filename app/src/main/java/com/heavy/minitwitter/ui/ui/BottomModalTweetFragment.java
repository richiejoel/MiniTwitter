package com.heavy.minitwitter.ui.ui;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelStoreOwner;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.heavy.minitwitter.R;
import com.heavy.minitwitter.common.Constants;
import com.heavy.minitwitter.data.TweetViewModel;

public class BottomModalTweetFragment extends BottomSheetDialogFragment {

    private TweetViewModel tweetViewModel;
    private int idTweetEliminar;

    public static BottomModalTweetFragment newInstance(int idTweet) {
        BottomModalTweetFragment bottomModalTweetFragment = new BottomModalTweetFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_TWEET_ID, idTweet);
        bottomModalTweetFragment.setArguments(args);
        return bottomModalTweetFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            idTweetEliminar = getArguments().getInt(Constants.ARG_TWEET_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_modal_tweet_fragment, container, false);
        final NavigationView nav = view.findViewById(R.id.bottom_navigation_tweet);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.action_delete_tweet){
                    tweetViewModel.mDeleteTweet(idTweetEliminar);
                    getDialog().dismiss();
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //tweetViewModel = ViewModelProviders.of(this).get(TweetViewModel.class);
        tweetViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(TweetViewModel.class);
    }

}
