package edu.url.salle.amir.azzam.sallefy.controller.callbacks;


import java.util.ArrayList;

import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.model.TrackRealm;

public interface TrackListCallback {
    void onTrackSelected(Track track);
    void onTrackSelected(int index, ArrayList<Track> tracks);
    void onTrackOfflineSelected(int index, ArrayList<TrackRealm> tracks);
}
