package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.UploadActivity;
import edu.url.salle.amir.azzam.sallefy.controller.UploadPlaylistActivity;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.PlayListAdapterHorizantal;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapter;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.UserAdapter;
import edu.url.salle.amir.azzam.sallefy.controller.callbacks.TrackListCallback;
import edu.url.salle.amir.azzam.sallefy.model.Like;
import edu.url.salle.amir.azzam.sallefy.model.Playlist;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.model.TrackRealm;
import edu.url.salle.amir.azzam.sallefy.model.User;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.PlaylistCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.TrackCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.UserCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.PlaylistManager;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.TrackManager;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.UserManager;
import edu.url.salle.amir.azzam.sallefy.utils.PreferenceUtils;

/**
 *
 * create an instance of this fragment.
 */
public class OtherProfileViewFragment extends Fragment  implements UserCallback, TrackListCallback, TrackCallback, PlaylistCallback {

    private RecyclerView mRecyclerViewUser;
    private RecyclerView mRecyclerViewMySongs;
    private RecyclerView mRecyclerViewMyPlaylists;
    ;

    private Button btnFollow;
    private Button btnFollowing;
    private User user;

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

    public OtherProfileViewFragment(User user){
        this.user = user;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_other_profile_view, container, false);
        initView(root);
        getData();
        return root;
    }

    private void initView(View v) {

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getParentFragmentManager().popBackStack();
                }
                return false;
            }
        } );
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


        mRecyclerViewMyPlaylists = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewPlayList);
        LinearLayoutManager managerP1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        PlayListAdapterHorizantal adapterP1 = new PlayListAdapterHorizantal(this, getActivity(), null);
        mRecyclerViewMyPlaylists.setLayoutManager(managerP1);
        mRecyclerViewMyPlaylists.setAdapter(adapterP1);


        tvFollowers = (TextView) v.findViewById(R.id.textView9);
        tvFollowing = (TextView) v.findViewById(R.id.textView10);

        btnFollow = v.findViewById(R.id.playlistFollowButton);
        btnFollow.setVisibility(View.VISIBLE);
        btnFollowing = v.findViewById(R.id.playlistFollowedButton);
        btnFollowing.setVisibility(View.GONE);

        UserManager.getInstance(getContext()).isFollowing(user.getLogin(),this);
        OtherProfileViewFragment thisFragment = this;
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance(getContext()).follow(user.getLogin(),thisFragment);
            }
        });
        btnFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance(getContext()).follow(user.getLogin(),thisFragment);
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
        String login = user.getLogin();
        UserManager.getInstance(getContext()).getUserData(login,this);

        TrackManager.getInstance(getActivity()).getUserTracks(login,this);

        PlaylistManager.getInstance(getActivity()).getPlaylistsByUser(login,this);


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
    public void onUserFollowed(boolean value) {
        if(value){
            btnFollow.setVisibility(View.GONE);
            btnFollowing.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onUserSelected(User user) {

    }

    @Override
    public void onFollowersUserReceived() {

    }

    @Override
    public void onFollowingUsersReceived() {

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
    public void onTrackOfflineSelected(int index, ArrayList<TrackRealm> tracks) {

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
        FragmentTransaction t = this.getFragmentManager().beginTransaction();
        t.replace(R.id.nav_host_fragment, fragment);
        t.commit();
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

    }


    @Override
    public void onUserTracksReceived(List<Track> tracks) {
        TrackListAdapter adapter = new TrackListAdapter(this, getActivity(), (ArrayList<Track>) tracks);
        mRecyclerViewMySongs.setAdapter(adapter);
    }

    @Override
    public void onUserLikedTracksReceived(List<Track> tracks) {
    }

    @Override
    public void onCreateTrack() {

    }
}
