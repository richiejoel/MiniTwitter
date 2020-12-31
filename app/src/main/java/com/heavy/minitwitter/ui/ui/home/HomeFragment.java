package com.heavy.minitwitter.ui.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heavy.minitwitter.R;
import com.heavy.minitwitter.data.TweetViewModel;
import com.heavy.minitwitter.retrofit.AuthMiniTwitterClient;
import com.heavy.minitwitter.retrofit.AuthMiniTwitterService;
import com.heavy.minitwitter.retrofit.response.Tweet;
import com.heavy.minitwitter.ui.adapters.MyTweetAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private List<Tweet> tweetList;
    private RecyclerView recyclerViewTweets;
    private MyTweetAdapter adapter;
    private TweetViewModel tweetViewModel;

    /*private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }*/

    public HomeFragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.tweetViewModel = new ViewModelProvider(this).get(TweetViewModel.class);
        tweetList = new ArrayList<Tweet>();
        recyclerViewTweets = view.findViewById(R.id.recyclerTweets);
        recyclerViewTweets.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyTweetAdapter((ArrayList<Tweet>) tweetList, getContext());
        recyclerViewTweets.setAdapter(adapter);

        loadTweets();

        return view;
    }



    private void loadTweets(){
        this.tweetViewModel.getTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                adapter.setData(tweetList);
            }
        });

    }

}
