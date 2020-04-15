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

import java.util.ArrayList;

import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.MusicViewActivity;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVertical;
import edu.url.salle.amir.azzam.sallefy.model.Playlist;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.PlaylistCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.SongViewManger;

public class PlaylistFragment extends Fragment implements PlaylistCallback {

    private ImageView ivPlaylistCoverPhoto;
    private ImageButton ibPlaylistThumbnail;
    private TextView tvPlaylistName;
    private TextView tvPlaylistAuthor;
    private RecyclerView rvPlaylistSongs;
    private Button btnFollow;
    private Button btnAuthorProfile;


    public static PlaylistFragment getInstance() {
        return new PlaylistFragment();
    }

    private void initView(View v) {

        tvPlaylistName = v.findViewById(R.id.dynamic_artist);
        tvPlaylistAuthor = v.findViewById(R.id.dynamic_title);
        ivPlaylistCoverPhoto = (ImageView) v.findViewById(R.id.track_img);

        rvPlaylistSongs = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewSong);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        //TrackListAdapterVertical adapter = new TrackListAdapterVertical(this, getActivity(), (ArrayList<Track>) searchResults.getFoundSongs());
        rvPlaylistSongs.setLayoutManager(manager);
        //rvPlaylistSongs.setAdapter(adapter);

        ibPlaylistThumbnail = (ImageButton)v.findViewById(R.id.playlistThumbnail);

        btnFollow = v.findViewById(R.id.playlistFollowButton);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                currentTrack = ((currentTrack+1)%(mList.size()));
                currentTrack = currentTrack >= mList.size() ? 0:currentTrack;
                updateTrack(currentTrack);

                 */
            }
        });

        btnAuthorProfile = v.findViewById(R.id.playlistAuthor);
        btnAuthorProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                currentTrack = ((currentTrack+1)%(mList.size()));
                currentTrack = currentTrack >= mList.size() ? 0:currentTrack;
                updateTrack(currentTrack);

                 */
            }
        });

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_playlist_view, container, false);
        initView(root);
        return root;
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
    public void onFailure(Throwable throwable) {

    }
}
