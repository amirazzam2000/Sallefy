package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVertical;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVerticalPlaylist;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVerticalUser;
import edu.url.salle.amir.azzam.sallefy.controller.callbacks.TrackListCallback;
import edu.url.salle.amir.azzam.sallefy.model.Playlist;
import edu.url.salle.amir.azzam.sallefy.model.Search;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.model.TrackRealm;
import edu.url.salle.amir.azzam.sallefy.model.User;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.PlaylistCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.UserCallback;


public class SearchResults extends Fragment implements TrackListCallback, PlaylistCallback , UserCallback {
    public static final String TAG = SearchFragment.class.getName();
    private Search searchResults;
    private RecyclerView mRecyclerVieSongs;
    private RecyclerView mRecyclerViePlaylist;
    private RecyclerView mRecyclerVieUsers;

    private TextView song;
    private TextView playlist;
    private TextView user;
    private TextView search;


    private Button songMore, playlistMore , usersMore;

    private Track[] aux10Tracks;
    private Playlist[] aux10Playlist;
    private User[] aux10Users;

    private String searchString;

    public static SearchResults getInstance() {
        return new SearchResults("search results for \" \":",null);
    }

    public SearchResults(String search,Search searchResults){
        this.searchResults = searchResults;
        searchString = search;
        aux10Tracks = new Track[4];
        aux10Playlist = new Playlist[4];
        aux10Users = new User[4];
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search_results, container, false);
        initView(root);
        return root;
    }

    @SuppressLint("SetTextI18n")
    private void initView(View v) {

        song = v.findViewById(R.id.song);
        playlist = v.findViewById(R.id.users);
        user = v.findViewById(R.id.users);
        search = v.findViewById(R.id.Search);

        songMore = v.findViewById(R.id.songMore);
        usersMore = v.findViewById(R.id.userMore);
        playlistMore = v.findViewById(R.id.playlistMore);


        search.setText("search results for \""+searchString +"\":");

        mRecyclerVieSongs = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewSong);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        TrackListAdapterVertical adapter = null;
        if (searchResults.getFoundSongs().size() > 4){
            for (int i = 0; i < 4; i++) {
                aux10Tracks[i] = searchResults.getFoundSongs().get(i);
            }
            adapter = new TrackListAdapterVertical(this, getActivity(), new ArrayList<Track>(Arrays.asList(aux10Tracks)));
        }
        else{
            adapter = new TrackListAdapterVertical(this, getActivity(), (ArrayList<Track>)searchResults.getFoundSongs());

        }
        mRecyclerVieSongs.setLayoutManager(manager);
        mRecyclerVieSongs.setAdapter(adapter);
        if( searchResults.getFoundSongs().size() == 0){
            song.setVisibility(View.GONE);
        }

        mRecyclerViePlaylist = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewPlayList);
        LinearLayoutManager managerPlaylist = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        TrackListAdapterVerticalPlaylist adapterPlaylist = null;
        if (searchResults.getFoundPlaylists().size() > 4){
            for (int i = 0; i < 4; i++) {
                aux10Playlist[i] = searchResults.getFoundPlaylists().get(i);
            }
            adapterPlaylist = new TrackListAdapterVerticalPlaylist(this, getActivity(), new ArrayList<Playlist>(Arrays.asList(aux10Playlist)));
        }
        else{
            adapterPlaylist = new TrackListAdapterVerticalPlaylist(this, getActivity(), (ArrayList<Playlist>)searchResults.getFoundPlaylists());
        }
        mRecyclerViePlaylist.setLayoutManager(managerPlaylist);
        mRecyclerViePlaylist.setAdapter(adapterPlaylist);
        if( searchResults.getFoundPlaylists().size() == 0){
            playlist.setVisibility(View.GONE);
        }


        mRecyclerVieUsers = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewUser);
        LinearLayoutManager managerUser = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        TrackListAdapterVerticalUser adapterUser = null;
        if (searchResults.getFoundUsers().size() > 4){
            for (int i = 0; i < 4; i++) {
                aux10Users[i] = (User)searchResults.getFoundUsers().get(i);
            }
            adapterUser = new TrackListAdapterVerticalUser(this, getActivity(), new ArrayList<User>(Arrays.asList(aux10Users)));
        }
        else{
            adapterUser = new TrackListAdapterVerticalUser(this, getActivity(), (ArrayList<User>) searchResults.getFoundUsers());
        }
        mRecyclerVieUsers.setLayoutManager(managerUser);
        mRecyclerVieUsers.setAdapter(adapterUser);
        if( searchResults.getFoundUsers().size() == 0){
            user.setVisibility(View.GONE);
        }


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

        songMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new showMore((ArrayList<Track>) searchResults.getFoundSongs(),new ArrayList<>(),new ArrayList<>());
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        playlistMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new showMore(new ArrayList<>(),(ArrayList<Playlist>) searchResults.getFoundPlaylists(),new ArrayList<>());
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        usersMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new showMore(new ArrayList<>(),new ArrayList<>(), (ArrayList<User>) searchResults.getFoundUsers());
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*String toast;
        if (searchResults.getFoundSongs().get(0).getName() == null )
            toast = "hello";
        else
            toast = searchResults.getFoundSongs().get(0).getName() ;
        Toast.makeText(getContext(),toast, Toast.LENGTH_LONG).show();*/
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
    public void onTrackSelected(Track track) {
        MusicControllerFragment musicFragment = (MusicControllerFragment) getFragmentManager().findFragmentById(R.id.musicPlayer);
        //musicFragment.updateTrack(track);
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
    public void onFailure(Throwable throwable) {

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

    @Override
    public void onFollowersUserReceived(List<User> followers) {

    }

    @Override
    public void onFollowingUsersReceived(List<User> following) {

    }

}
