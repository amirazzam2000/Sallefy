package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.HomePageActivity;
import edu.url.salle.amir.azzam.sallefy.controller.LoginActivity;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapter;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.TrackCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.TrackManager;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.UserManager;

public class HomeFragment extends Fragment implements TrackCallback {

    public static final String TAG = HomeFragment.class.getName();

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        doLogin();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void doLogin() {
        TrackManager.getInstance(getContext()).getAllTracks(HomeFragment.this);
    }
/*
    private void initViews(View v) {
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mAdapter = new TrackListAdapter(null, getContext(), this, R.layout.);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.home_recyclerview);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
*/



    @Override
    public void onTracksReceived(List<Track> tracks) {
        System.out.println("hello");
        System.out.println(tracks.get(0).getName());
    }

    @Override
    public void onNoTracks(Throwable throwable) {
        try {
            throw throwable;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("no");
    }

    @Override
    public void onPersonalTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onUserTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
