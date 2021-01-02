package com.heavy.minitwitter.ui.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.heavy.minitwitter.R;
import com.heavy.minitwitter.data.TweetViewModel;
import com.heavy.minitwitter.retrofit.response.Tweet;
import com.heavy.minitwitter.ui.DashboardActivity;
import com.heavy.minitwitter.ui.adapters.MyTweetAdapter;

import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment {

    private List<Tweet> tweetList;
    private RecyclerView recyclerViewTweetsFavs;
    private MyTweetAdapter adapter;
    private TweetViewModel tweetViewModel;
    private SwipeRefreshLayout swipeRefreshLayoutFavs;
    private FloatingActionButton fab;

    /*private FavoritesViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(FavoritesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }*/

    public FavoritesFragment (){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.tweetViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(TweetViewModel.class);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        tweetList = new ArrayList<Tweet>();
        recyclerViewTweetsFavs = view.findViewById(R.id.recyclerTweetsFavs);
        swipeRefreshLayoutFavs = view.findViewById(R.id.swipeRefreshFavs);

        swipeRefreshLayoutFavs.setColorSchemeColors(getResources().getColor(R.color.colorBlue));
        recyclerViewTweetsFavs.setLayoutManager(new LinearLayoutManager(getContext()));

        fab = ((DashboardActivity) getActivity()).getFab();
        fab.hide();

        adapter = new MyTweetAdapter((ArrayList<Tweet>) tweetList, getContext());
        recyclerViewTweetsFavs.setAdapter(adapter);




        swipeRefreshLayoutFavs.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayoutFavs.setRefreshing(true);
                loadNewTweetsFavs();
            }
        });

        loadTweetsFavs();

        return view;
    }



    private void loadTweetsFavs(){
        this.tweetViewModel.getFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                adapter.setData(tweetList);
            }
        });

    }

    private void loadNewTweetsFavs(){
        this.tweetViewModel.getNewFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                swipeRefreshLayoutFavs.setRefreshing(false);
                adapter.setData(tweetList);
                tweetViewModel.getNewTweets().removeObserver(this);
            }
        });

    }
}
