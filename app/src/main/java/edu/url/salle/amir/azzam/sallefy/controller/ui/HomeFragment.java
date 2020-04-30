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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import edu.url.salle.amir.azzam.sallefy.model.TrackRealm;
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
    private Button nextBig;
    private Button previousBig;
    private Button bigSelected;
    private int currentBig;
    private ArrayList<Track> mBigList;

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

 /*       ivPictureBig.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            public void onSwipeTop() {
                Toast.makeText(getContext(), "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(getContext(), "bottom", Toast.LENGTH_SHORT).show();
            }
        });
*/    }

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


        tvAuthorBig = (TextView) v.findViewById(R.id.dynamic_artist_big);
        tvTitleBig = (TextView) v.findViewById(R.id.dynamic_title_big);
        ivPictureBig = (ImageView) v.findViewById(R.id.big_image);
        nextBig = (Button) v.findViewById(R.id.nextBigSong);
        previousBig = (Button) v.findViewById(R.id.previousBigSong);
        bigSelected = (Button) v.findViewById(R.id.selectBig);

        nextBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBig = ((currentBig+1)%(mBigList.size()));
                currentBig = currentBig >= mBigList.size() ? 0:currentBig;

                tvTitleBig.setText(mBigList.get(currentBig).getName());
                tvAuthorBig.setText(mBigList.get(currentBig).getUserLogin());
                if (mBigList.get(currentBig).getThumbnail() != null) {
                    Glide.with(getContext())
                            .asBitmap()
                            .placeholder(R.drawable.ic_audiotrack)
                            .load(mBigList.get(currentBig).getThumbnail())
                            .into(ivPictureBig);
                }else{
                    Glide.with(getContext())
                            .asBitmap()
                            .placeholder(R.drawable.ic_audiotrack)
                            .load(R.drawable.ic_logo)
                            .into(ivPictureBig);
                }
            }
        });

        previousBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBig = ((currentBig-1)%(mBigList.size()));
                currentBig = currentBig < 0 ? (mBigList.size()-1):currentBig;

                tvTitleBig.setText(mBigList.get(currentBig).getName());
                tvAuthorBig.setText(mBigList.get(currentBig).getUserLogin());
                if (mBigList.get(currentBig).getThumbnail() != null) {
                    Glide.with(getContext())
                            .asBitmap()
                            .placeholder(R.drawable.ic_audiotrack)
                            .load(mBigList.get(currentBig).getThumbnail())
                            .into(ivPictureBig);
                }else{
                    Glide.with(getContext())
                            .asBitmap()
                            .placeholder(R.drawable.ic_audiotrack)
                            .load(R.drawable.ic_logo)
                            .into(ivPictureBig);
                }
            }
        });

        bigSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrackSelected(currentBig, mBigList);
            }
        });


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

        if(requestNumber == 2) {
            TrackListAdapter adapter = new TrackListAdapter(this, getActivity(), requestQ.poll());
            mRecyclerViewMostPlayed.setAdapter(adapter);

            mBigList = requestQ.poll();
            adapter = new TrackListAdapter(this, getActivity(), mBigList);
            mRecyclerViewMostRecent.setAdapter(adapter);

            tvTitleBig.setText(tracks.get(0).getName());
            tvAuthorBig.setText(tracks.get(0).getUserLogin());
            if (tracks.get(0).getThumbnail() != null) {
                Glide.with(getContext())
                        .asBitmap()
                        .placeholder(R.drawable.ic_audiotrack)
                        .load(tracks.get(0).getThumbnail())
                        .into(ivPictureBig);
            }else{
                Glide.with(getContext())
                        .asBitmap()
                        .placeholder(R.drawable.ic_audiotrack)
                        .load(R.drawable.ic_logo)
                        .into(ivPictureBig);
            }
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
    public void onUserLikedTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onCreateTrack() {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onTrackSelected(Track track) {
        //MusicControllerFragment musicFragment = (MusicControllerFragment) getFragmentManager().findFragmentById(R.id.musicPlayer);
        //musicFragment.updateTrack(track);
    }

    @Override
    public void onTrackSelected(int index, ArrayList<Track> tracks) {
        /*System.out.println("Index song: " + index);
        updateTrack(index);*/
        MusicControllerFragment musicFragment = (MusicControllerFragment) getFragmentManager().findFragmentById(R.id.musicPlayer);
        musicFragment.updateTrack(index, tracks);
    }

    @Override
    public void onTrackOfflineSelected(int index, ArrayList<TrackRealm> tracks) {

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

    @Override
    public void onPlayListCreated(Playlist playlist) {

    }

    @Override
    public void onPlaylistFollowed() {

    }

    @Override
    public void onPlaylistSelected(Playlist playlist) {
        PlaylistFragment fragment = new PlaylistFragment(playlist);
        FragmentTransaction t = this.getParentFragmentManager().beginTransaction();
        t.replace(R.id.nav_host_fragment, fragment);
        t.addToBackStack(null);
        t.commit();
    }
}
