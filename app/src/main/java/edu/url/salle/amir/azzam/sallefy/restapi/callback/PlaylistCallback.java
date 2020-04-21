package edu.url.salle.amir.azzam.sallefy.restapi.callback;

import java.util.ArrayList;
import java.util.List;

import edu.url.salle.amir.azzam.sallefy.model.Like;
import edu.url.salle.amir.azzam.sallefy.model.Playlist;
import edu.url.salle.amir.azzam.sallefy.model.Track;

public interface PlaylistCallback extends FailureCallback{
    void onPlaylistById(Playlist playlist);

    void onPlaylistsByUser(ArrayList<Playlist> playlists);

    void onAllList(ArrayList<Playlist> playlists);

    void onFollowingList(ArrayList<Playlist> playlists);

    void onPlayListCreated(Playlist playlist);

    void onPlaylistFollowed();

    void onPlaylistSelected(Playlist playlist);
}
