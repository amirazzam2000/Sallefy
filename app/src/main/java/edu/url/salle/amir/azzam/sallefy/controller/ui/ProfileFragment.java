package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.url.salle.amir.azzam.sallefy.controller.UploadActivity;
import edu.url.salle.amir.azzam.sallefy.controller.UploadPlaylistActivity;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.PlaylistManager;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.TrackManager;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.UserManager;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.PlayListAdapterHorizantal;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapter;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.UserAdapter;
import edu.url.salle.amir.azzam.sallefy.controller.callbacks.TrackListCallback;
import edu.url.salle.amir.azzam.sallefy.model.Like;
import edu.url.salle.amir.azzam.sallefy.model.Playlist;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.model.User;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.PlaylistCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.TrackCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.UserCallback;
import edu.url.salle.amir.azzam.sallefy.utils.PreferenceUtils;

public class ProfileFragment extends Fragment implements UserCallback, TrackListCallback, TrackCallback, PlaylistCallback {

    private RecyclerView mRecyclerViewUser;
    private RecyclerView mRecyclerViewMySongs;
    private RecyclerView mRecyclerViewMyPlaylists;
    private RecyclerView mRecyclerViewLikedSongs;
    private RecyclerView mRecyclerViewFollowingPlaylist;

    private Button addSongs;
    private Button addPlaylist;

    private ArrayList<Track> mUser;
    private ArrayList<Track> mMySongs;
    private ArrayList<Track> mMyPlaylists;
    private ArrayList<Track> mLikedSongs;
    private ArrayList<Track> mLikedPlaylist;


    TextView tvFollowing;
    TextView tvFollowers;

    //Queues for getting the songs
    private ConcurrentLinkedQueue<ArrayList<Track>> requestQ;
    private int requestNumber = 0;

    //Queues for getting the playlists
    private ConcurrentLinkedQueue<ArrayList<Playlist>> requestQPlaylist;
    private int requestNumberPlaylist = 0;

    public static final String TAG = ProfileFragment.class.getName();

    public static ProfileFragment getInstance() {
        return new ProfileFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(root);
        getData();
        return root;
    }

    private void initView(View v) {

        requestQ = new ConcurrentLinkedQueue<>();
        requestQPlaylist = new ConcurrentLinkedQueue<>();

        mRecyclerViewUser = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        UserAdapter adapter = new UserAdapter(null, getActivity());
        mRecyclerViewUser.setLayoutManager(manager);
        mRecyclerViewUser.setAdapter(adapter);

        mRecyclerViewMySongs = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewMostPlayed);
        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        TrackListAdapter adapter1 = new TrackListAdapter(this, getActivity(), null);
        mRecyclerViewMySongs.setLayoutManager(manager1);
        mRecyclerViewMySongs.setAdapter(adapter1);

        mRecyclerViewLikedSongs = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewLikedSong);
        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        TrackListAdapter adapter2 = new TrackListAdapter(this, getActivity(), null);
        mRecyclerViewLikedSongs.setLayoutManager(manager2);
        mRecyclerViewLikedSongs.setAdapter(adapter2);

        mRecyclerViewMyPlaylists = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewPlayList);
        LinearLayoutManager managerP1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        PlayListAdapterHorizantal adapterP1 = new PlayListAdapterHorizantal(this, getActivity(), null);
        mRecyclerViewMyPlaylists.setLayoutManager(managerP1);
        mRecyclerViewMyPlaylists.setAdapter(adapterP1);

        mRecyclerViewFollowingPlaylist = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewLikedList);
        LinearLayoutManager managerP2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        PlayListAdapterHorizantal adapterP2 = new PlayListAdapterHorizantal(this, getActivity(), null);
        mRecyclerViewFollowingPlaylist.setLayoutManager(managerP2);
        mRecyclerViewFollowingPlaylist.setAdapter(adapterP2);

        tvFollowers = (TextView) v.findViewById(R.id.textView9);
        tvFollowing = (TextView) v.findViewById(R.id.textView10);

        addSongs = (Button) v.findViewById(R.id.addSongs);
        addPlaylist = (Button) v.findViewById(R.id.addPlaylists);

        addSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UploadActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);

            }
        });

        addPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UploadPlaylistActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);

            }
        });

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void getData() {
        String login = PreferenceUtils.getUser(getActivity());
        UserManager.getInstance(getContext()).getUserData(login,this);

        TrackManager.getInstance(getActivity()).getOwnTracks(this);
        TrackManager.getInstance(getActivity()).getOwnLikedTracks(this);

        PlaylistManager.getInstance(getActivity()).getPlaylistsByUser(login,this);
        PlaylistManager.getInstance(getActivity()).getFollowingPlaylists(this);

        mMySongs = new ArrayList<>();
        mMyPlaylists = new ArrayList<>();

        mLikedSongs = new ArrayList<>();
        mLikedPlaylist = new ArrayList<>();
    }

    @Override
    public void onLoginSuccess(UserToken userToken) {

    }

    @Override
    public void onLoginFailure(Throwable throwable) {

    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public void onRegisterFailure(Throwable throwable) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUserInfoReceived(User userData) {
        UserAdapter adapter = new UserAdapter(userData, getActivity());
        mRecyclerViewUser.setAdapter(adapter);
        tvFollowers.setText(userData.getFollowers() + "");
        tvFollowing.setText(userData.getFollowing() + "");
    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onTrackSelected(Track track) {
        MusicControllerFragment musicFragment = (MusicControllerFragment) getFragmentManager().findFragmentById(R.id.musicPlayer);
        //musicFragment.updateTrack(track);
    }

    @Override
    public void onTrackSelected(int index , ArrayList<Track> tracks) {
        MusicControllerFragment musicFragment = (MusicControllerFragment) getFragmentManager().findFragmentById(R.id.musicPlayer);
        musicFragment.updateTrack(index,tracks);
    }

    @Override
    public void onPlaylistById(Playlist playlist) {

    }

    @Override
    public void onPlaylistsByUser(ArrayList<Playlist> playlists) {
        PlayListAdapterHorizantal adapter = new PlayListAdapterHorizantal(this, getActivity(), playlists);
        mRecyclerViewMyPlaylists.setAdapter(adapter);
    }

    @Override
    public void onAllList(ArrayList<Playlist> playlists) {

    }

    @Override
    public void onFollowingList(ArrayList<Playlist> playlists) {
        PlayListAdapterHorizantal adapter = new PlayListAdapterHorizantal(this, getActivity(), playlists);
        mRecyclerViewFollowingPlaylist.setAdapter(adapter);
    }

    @Override
    public void onPlayListCreated(Playlist playlist) {

    }

    @Override
    public void onTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onTrackReceived(Track track) {

    }

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
    public void onPersonalTracksReceived(List<Track> tracks){
        TrackListAdapter adapter = new TrackListAdapter(this, getActivity(), (ArrayList<Track>) tracks);
        mRecyclerViewMySongs.setAdapter(adapter);
    }


    @Override
    public void onUserTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onUserLikedTracksReceived(List<Track> tracks) {
        TrackListAdapter adapter = new TrackListAdapter(this, getActivity(), (ArrayList<Track>) tracks);
        mRecyclerViewLikedSongs.setAdapter(adapter);
    }

    @Override
    public void onCreateTrack() {

    }
}
