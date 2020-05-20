package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.PlayListAdapterHorizantal;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVertical;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVerticalPlaylist;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVerticalUser;
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

public class showMore extends Fragment implements TrackCallback, PlaylistCallback, TrackListCallback, UserCallback{

    private static ArrayList<Track> tracks;
    private static ArrayList<Playlist> playlists;
    private static ArrayList<User> users;
    private RecyclerView mRecyclerView;



    public showMore(ArrayList<Track> tracks, ArrayList<Playlist> playlists, ArrayList<User> users) {
        showMore.tracks = tracks;
        showMore.playlists = playlists;
        showMore.users = users;

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_show_more, container, false);
        initView(root);
        return root;
    }

    private void initView(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.songList);
        LinearLayoutManager managerP2 = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(managerP2);

        if (tracks.size() >= 1){
            TrackListAdapterVertical adapterP2 = new TrackListAdapterVertical(this, getActivity(), tracks);
            mRecyclerView.setAdapter(adapterP2);

        }else if (playlists.size() >= 1){
            TrackListAdapterVerticalPlaylist adapterP2 = new TrackListAdapterVerticalPlaylist(this, getActivity(), playlists);
            mRecyclerView.setAdapter(adapterP2);

        }else if (users.size() >= 1){
            TrackListAdapterVerticalUser adapterP2 = new TrackListAdapterVerticalUser(this, getActivity(), users);
            mRecyclerView.setAdapter(adapterP2);
        }
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
    public void onPlaylistById(Playlist playlist) {

    }

    @Override
    public void onPlaylistsByUser(ArrayList<Playlist> playlists) {

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
        MusicControllerFragment musicFragment = (MusicControllerFragment) getFragmentManager().findFragmentById(R.id.musicPlayer);
    }

    @Override
    public void onTrackSelected(int index, ArrayList<Track> tracks) {
        MusicControllerFragment musicFragment = (MusicControllerFragment) getFragmentManager().findFragmentById(R.id.musicPlayer);
        musicFragment.updateTrack(index, tracks);
    }

    @Override
    public void onTrackOfflineSelected(int index, ArrayList<TrackRealm> tracks) {

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

    @Override
    public void onUserInfoReceived(User userData) {

    }

    @Override
    public void onUserFollowed(boolean value) {

    }

    @Override
    public void onUserSelected(User user) {
        OtherProfileViewFragment fragment = new OtherProfileViewFragment(user);
        FragmentTransaction t = this.getFragmentManager().beginTransaction();
        t.replace(R.id.nav_host_fragment, fragment);
        t.commit();
    }
}
