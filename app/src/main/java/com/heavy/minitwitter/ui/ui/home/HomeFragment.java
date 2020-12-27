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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heavy.minitwitter.R;
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
    private AuthMiniTwitterClient authMiniTwitterClient;
    private AuthMiniTwitterService authMiniTwitterService;

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
        tweetList = new ArrayList<Tweet>();
        recyclerViewTweets = view.findViewById(R.id.recyclerTweets);
        recyclerViewTweets.setLayoutManager(new LinearLayoutManager(getContext()));

        retrofitInit();
        loadTweets();

        return view;
    }

    private void retrofitInit() {
        authMiniTwitterClient = AuthMiniTwitterClient.getInstance();
        authMiniTwitterService = authMiniTwitterClient.getAuthMiniTwitterService();
    }

    private void loadTweets(){
        Call<List<Tweet>> call = authMiniTwitterService.mDoAllGetTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful()){
                    tweetList = response.body();
                    adapter = new MyTweetAdapter((ArrayList<Tweet>) tweetList, getContext());
                    recyclerViewTweets.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error in Sever. Try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(getContext(), "Trouble connection. Try again please.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
