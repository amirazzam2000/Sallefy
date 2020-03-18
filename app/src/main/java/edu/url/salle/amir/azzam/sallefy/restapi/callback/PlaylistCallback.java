package edu.url.salle.amir.azzam.sallefy.restapi.callback;

import edu.url.salle.amir.azzam.sallefy.model.Playlist;

public interface PlaylistCallback {

    void onPlaylistCreated(Playlist playlist);
    void onPlaylistFailure(Throwable throwable);
}
