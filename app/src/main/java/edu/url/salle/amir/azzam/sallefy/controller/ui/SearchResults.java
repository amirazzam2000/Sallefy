package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapter;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVertical;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVerticalPlaylist;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVerticalUser;
import edu.url.salle.amir.azzam.sallefy.controller.callbacks.TrackListCallback;
import edu.url.salle.amir.azzam.sallefy.model.Playlist;
import edu.url.salle.amir.azzam.sallefy.model.Search;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.model.User;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.TrackCallback;


public class SearchResults extends Fragment implements TrackListCallback {
    public static final String TAG = SearchFragment.class.getName();
    private Search searchResults;
    private RecyclerView mRecyclerVieSongs;
    private RecyclerView mRecyclerViePlaylist;
    private RecyclerView mRecyclerVieUsers;

    public static SearchResults getInstance() {
        return new SearchResults(null);
    }

    public SearchResults(Search searchResults){
        this.searchResults = searchResults;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search_results, container, false);
        initView(root);
        return root;
    }

    private void initView(View v) {
        mRecyclerVieSongs = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewSong);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        TrackListAdapterVertical adapter = new TrackListAdapterVertical(this, getActivity(), (ArrayList<Track>) searchResults.getFoundSongs());
        mRecyclerVieSongs.setLayoutManager(manager);
        mRecyclerVieSongs.setAdapter(adapter);

        mRecyclerViePlaylist = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewPlayList);
        LinearLayoutManager managerPlaylist = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        TrackListAdapterVerticalPlaylist adapterPlaylist = new TrackListAdapterVerticalPlaylist(this, getActivity(), (ArrayList<Playlist>) searchResults.getFoundPlaylists());
        mRecyclerViePlaylist.setLayoutManager(managerPlaylist);
        mRecyclerViePlaylist.setAdapter(adapterPlaylist);

        mRecyclerVieUsers = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewUser);
        LinearLayoutManager managerUser = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        TrackListAdapterVerticalUser adapterUser = new TrackListAdapterVerticalUser(this, getActivity(), (ArrayList<User>) searchResults.getFoundUsers());
        mRecyclerVieUsers.setLayoutManager(managerUser);
        mRecyclerVieUsers.setAdapter(adapterUser);


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
        musicFragment.updateTrack(track);
    }

    @Override
    public void onTrackSelected(int index) {

    }
}
