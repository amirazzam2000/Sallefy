package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chootdev.blurimg.BlurImage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.MusicViewActivity;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVertical;
import edu.url.salle.amir.azzam.sallefy.controller.callbacks.TrackListCallback;
import edu.url.salle.amir.azzam.sallefy.model.Playlist;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.model.TrackRealm;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.PlaylistCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.PlaylistManager;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.SongViewManger;
import edu.url.salle.amir.azzam.sallefy.utils.PreferenceUtils;

public class PlaylistFragment extends Fragment implements PlaylistCallback, TrackListCallback {

    private ImageView ivPlaylistCoverPhoto;
    private ImageView ibPlaylistThumbnail;
    private TextView tvPlaylistName;
    private Button tvPlaylistAuthor;
    private RecyclerView rvPlaylistSongs;
    private Button btnFollow;
    private Button btnFollowing;
    private Button btnAuthorProfile;
    private Playlist playlist;


    public static PlaylistFragment getInstance() {
        return new PlaylistFragment(null);
    }

    public PlaylistFragment(Playlist playlist){
        this.playlist = playlist;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_playlist_view, container, false);
        initView(root);
        return root;
    }

    @SuppressLint("CutPasteId")
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

        tvPlaylistName = v.findViewById(R.id.playlistName);
        tvPlaylistName.setText(playlist.getName());

        tvPlaylistAuthor = v.findViewById(R.id.playlistAuthor);
        tvPlaylistAuthor.setTextColor(Color.BLACK);
        tvPlaylistAuthor.setText(playlist.getUser().getLogin());

        ivPlaylistCoverPhoto = (ImageView) v.findViewById(R.id.playlistCover);
        if (playlist.getCover() != null) {
            Glide.with(getContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(playlist.getCover())
                    .into(ivPlaylistCoverPhoto);
        }

        rvPlaylistSongs = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewSong);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        TrackListAdapterVertical adapter = new TrackListAdapterVertical( this, getActivity(), (ArrayList<Track>) playlist.getTracks());
        rvPlaylistSongs.setLayoutManager(manager);
        rvPlaylistSongs.setAdapter(adapter);

        ibPlaylistThumbnail = (ImageView)v.findViewById(R.id.playlistThumbnail);
        if (playlist.getThumbnail() != null) {
            Glide.with(getContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(playlist.getThumbnail() )
                    .into(ibPlaylistThumbnail);
        }else{
            Glide.with(getContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(R.drawable.ic_logo)
                    .into(ibPlaylistThumbnail);
        }

        btnFollow = v.findViewById(R.id.playlistFollowButton);
        btnFollow.setVisibility(View.VISIBLE);
        btnFollowing = v.findViewById(R.id.playlistFollowedButton);
        btnFollowing.setVisibility(View.GONE);
        if(playlist.isFollowed()){
            btnFollow.setVisibility(View.GONE);
            btnFollowing.setVisibility(View.VISIBLE);
        }

        if (playlist.getUser().getLogin().equals(PreferenceUtils.getUser(getContext()))){
            btnFollow.setVisibility(View.GONE);
            btnFollowing.setVisibility(View.GONE);
        }

        PlaylistFragment thisFragment = this;
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playlist.isFollowed()) {
                    PlaylistManager.getInstance(getContext()).follow(playlist.getId(),thisFragment);
                }
            }
        });

        btnFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playlist.isFollowed()) {
                    PlaylistManager.getInstance(getContext()).follow(playlist.getId(),thisFragment);
                }
            }
        });

        btnAuthorProfile = v.findViewById(R.id.playlistAuthor);
        btnAuthorProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherProfileViewFragment fragment = new OtherProfileViewFragment(playlist.getUser());
                FragmentTransaction t = thisFragment.getFragmentManager().beginTransaction();
                t.replace(R.id.nav_host_fragment, fragment);
                t.addToBackStack(null);
                t.commit();
            }
        });



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
        playlist.setFollowed(!playlist.isFollowed());
        if(playlist.isFollowed()) {
            btnFollow.setVisibility(View.GONE);
            btnFollowing.setVisibility(View.VISIBLE);
        }else{
            btnFollow.setVisibility(View.VISIBLE);
            btnFollowing.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPlaylistSelected(Playlist playlist) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackSelected(int index, ArrayList<Track> tracks) {
        MusicControllerFragment musicFragment = (MusicControllerFragment) getFragmentManager().findFragmentById(R.id.musicPlayer);
        musicFragment.updateTrack(index, tracks);
    }

    @Override
    public void onTrackOfflineSelected(int index, ArrayList<TrackRealm> tracks) {

    }
}
