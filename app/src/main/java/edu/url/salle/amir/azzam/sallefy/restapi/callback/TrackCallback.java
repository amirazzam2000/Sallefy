package edu.url.salle.amir.azzam.sallefy.restapi.callback;

import java.util.List;

import edu.url.salle.amir.azzam.sallefy.model.Track;

public interface TrackCallback extends FailureCallback {
    void onTracksReceived(List<Track> tracks);
    void onNoTracks(Throwable throwable);
    void onPersonalTracksReceived(List<Track> tracks);
    void onUserTracksReceived(List<Track> tracks);
}
