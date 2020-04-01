package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

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

public class ProfileFragment extends Fragment implements UserCallback, TrackListCallback, TrackCallback, PlaylistCallback {

    private RecyclerView mRecyclerViewUser;
    private RecyclerView mRecyclerViewMySongs;
    private RecyclerView mRecyclerViewMyPlaylists;
    private RecyclerView mRecyclerViewLikedSongs;
    private RecyclerView mRecyclerViewLikedPlaylist;

    private ArrayList<Track> mUser;
    private ArrayList<Track> mMySongs;
    private ArrayList<Track> mMyPlaylists;
    private ArrayList<Track> mLikedSongs;
    private ArrayList<Track> mLikedPlaylist;

    private TextView tvTitleBig;
    private TextView tvAuthorBig;
    private ImageView ivPictureBig;

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

        mRecyclerViewMyPlaylists = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewPopularPlaylist);
        LinearLayoutManager managerP1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        PlayListAdapterHorizantal adapterP1 = new PlayListAdapterHorizantal(this, getActivity(), null);
        mRecyclerViewMyPlaylists.setLayoutManager(managerP1);
        mRecyclerViewMyPlaylists.setAdapter(adapterP1);

        mRecyclerViewLikedPlaylist = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewRecentPlaylist);
        LinearLayoutManager managerP2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        PlayListAdapterHorizantal adapterP2 = new PlayListAdapterHorizantal(this, getActivity(), null);
        mRecyclerViewLikedPlaylist.setLayoutManager(managerP2);
        mRecyclerViewLikedPlaylist.setAdapter(adapterP2);

        tvAuthorBig = v.findViewById(R.id.dynamic_artist_big);
        tvTitleBig = v.findViewById(R.id.dynamic_title_big);
        ivPictureBig = (ImageView) v.findViewById(R.id.big_image);

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
        UserAdapter adapter = new UserAdapter(userData, getActivity());
        mRecyclerViewUser.setAdapter(adapter);
    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackSelected(int index) {

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
            mRecyclerViewMySongs.setAdapter(adapter);

            adapter = new TrackListAdapter(this, getActivity(), requestQ.poll());
            mRecyclerViewLikedSongs.setAdapter(adapter);
        }
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
    public void onCreateTrack() {

    }
}
