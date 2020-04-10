package edu.url.salle.amir.azzam.sallefy.controller.callbacks;


import java.util.ArrayList;

import edu.url.salle.amir.azzam.sallefy.model.Track;

public interface TrackListCallback {
    void onTrackSelected(Track track);
    void onTrackSelected(int index, ArrayList<Track> tracks);
}
