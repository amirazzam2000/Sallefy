package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.PlayListAdapterHorizantal;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapter;
import edu.url.salle.amir.azzam.sallefy.controller.callbacks.TrackListCallback;
import edu.url.salle.amir.azzam.sallefy.controller.music.MusicCallback;
import edu.url.salle.amir.azzam.sallefy.controller.music.MusicService;
import edu.url.salle.amir.azzam.sallefy.model.Like;
import edu.url.salle.amir.azzam.sallefy.model.Playlist;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.PlaylistCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.TrackCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.PlaylistManager;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.TrackManager;


public class HomeFragment extends Fragment
        implements TrackListCallback, TrackCallback, PlaylistCallback {

    public static final String TAG = HomeFragment.class.getName();
    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";


    private TextView tvTitleBig;
    private TextView tvAuthorBig;
    private ImageView ivPictureBig;

    private Handler mHandler;
    private Runnable mRunnable;


    private RecyclerView mRecyclerViewMostPlayed;
    private RecyclerView mRecyclerViewMostRecent;
    private RecyclerView mRecyclerViewPopularPlaylist;
    private RecyclerView mRecyclerViewRecentPlaylist;
    // Service
    private MusicService mBoundService;
    private boolean mServiceBound = false;

    private ArrayList<Track> mPopularTracks;
    private ArrayList<Track> mRecentTracks;
    private ArrayList<Track> mPopularPlaylist;
    private ArrayList<Track> mRecentPlaylists;

    private int currentTrack = 0;
    private ConcurrentLinkedQueue<ArrayList<Track>> requestQ;
    private int requestNumber = 0;

    private ConcurrentLinkedQueue<ArrayList<Playlist>> requestQPlaylist;
    private int requestNumberPlaylist = 0;



    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_home, container, false);

        initViews(v);
        getData();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews(View v) {
        requestQ = new ConcurrentLinkedQueue<>();
        requestQPlaylist = new ConcurrentLinkedQueue<>();
        mRecyclerViewMostPlayed = (RecyclerView) v.findViewById(R.id.dynamic_recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        TrackListAdapter adapter = new TrackListAdapter(this, getActivity(), null);
        mRecyclerViewMostPlayed.setLayoutManager(manager);
        mRecyclerViewMostPlayed.setAdapter(adapter);

        mRecyclerViewMostRecent = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewRecent);
        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        TrackListAdapter adapter2 = new TrackListAdapter(this, getActivity(), null);
        mRecyclerViewMostRecent.setLayoutManager(manager2);
        mRecyclerViewMostRecent.setAdapter(adapter2);

        mRecyclerViewPopularPlaylist = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewPopularPlaylist);
        LinearLayoutManager managerP1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        PlayListAdapterHorizantal adapterP1 = new PlayListAdapterHorizantal(this, getActivity(), null);
        mRecyclerViewPopularPlaylist.setLayoutManager(managerP1);
        mRecyclerViewPopularPlaylist.setAdapter(adapterP1);

        mRecyclerViewRecentPlaylist = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewRecentPlaylist);
        LinearLayoutManager managerP2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        PlayListAdapterHorizantal adapterP2 = new PlayListAdapterHorizantal(this, getActivity(), null);
        mRecyclerViewRecentPlaylist.setLayoutManager(managerP2);
        mRecyclerViewRecentPlaylist.setAdapter(adapterP2);

        mHandler = new Handler();



        tvAuthorBig = v.findViewById(R.id.dynamic_artist_big);
        tvTitleBig = v.findViewById(R.id.dynamic_title_big);
        ivPictureBig = (ImageView) v.findViewById(R.id.big_image);


        //mSeekBar = (SeekBar) v.findViewById(R.id.dynamic_seekBar);
        /*mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mBoundService.setCurrentDuration(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/
    }





    private void getData() {
        TrackManager.getInstance(getActivity()).getMostPlayedTracks(this);
        TrackManager.getInstance(getActivity()).getMostRecentTracks(this);

        PlaylistManager.getInstance(getActivity()).getPopularPlaylist(this);
        PlaylistManager.getInstance(getActivity()).getRecentPlaylist(this);

        mPopularTracks = new ArrayList<>();
        mRecentTracks = new ArrayList<>();


    }

    @Override
    public void onTracksReceived(List<Track> tracks) {
        requestQ.add((ArrayList) tracks);
        requestNumber++;

        tvTitleBig.setText(tracks.get(0).getName());
        tvAuthorBig.setText(tracks.get(0).getUserLogin());
        if (tracks.get(0).getThumbnail() != null) {
            Glide.with(getContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(tracks.get(0).getThumbnail())
                    .into(ivPictureBig);
        }

        if(requestNumber == 2) {
            TrackListAdapter adapter = new TrackListAdapter(this, getActivity(), requestQ.poll());
            mRecyclerViewMostPlayed.setAdapter(adapter);

            adapter = new TrackListAdapter(this, getActivity(), requestQ.poll());
            mRecyclerViewMostRecent.setAdapter(adapter);
        }
    }

    @Override
    public void onTrackReceived(Track track) {

    }


    /**********************************************************************************************
     *   *   *   *   *   *   *   *   TrackCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/

    @Override
    public void onNoTracks(Throwable throwable) {

    }

    @Override
    public void onTrackDeleted() {

    }

    @Override
    public void onLikeReceived(Like like) {

    }

    @Override
    public void onNoLike(Throwable throwable) {

    }

    @Override
    public void onPersonalTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onUserTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onCreateTrack() {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onTrackSelected(Track track) {
        MusicControllerFragment musicFragment = (MusicControllerFragment) getFragmentManager().findFragmentById(R.id.musicPlayer);
        musicFragment.updateTrack(track);
    }

    @Override
    public void onTrackSelected(int index) {
        /*System.out.println("Index song: " + index);
        updateTrack(index);*/
    }


    /**********************************************************************************************
     *   *   *   *   *   *   *   *   MusicCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/

    @Override
    public void onPlaylistById(Playlist playlist) {

    }

    @Override
    public void onPlaylistsByUser(ArrayList<Playlist> playlists) {

    }

    @Override
    public void onAllList(ArrayList<Playlist> playlists) {
        requestQPlaylist.add(playlists);
        requestNumberPlaylist++;

        if(requestNumberPlaylist == 2) {
            PlayListAdapterHorizantal adapter = new PlayListAdapterHorizantal(this, getActivity(), requestQPlaylist.poll());
            mRecyclerViewPopularPlaylist.setAdapter(adapter);

            adapter = new PlayListAdapterHorizantal(this, getActivity(), requestQPlaylist.poll());
            mRecyclerViewRecentPlaylist.setAdapter(adapter);
        }
    }

    @Override
    public void onFollowingList(ArrayList<Playlist> playlists) {

    }
}
