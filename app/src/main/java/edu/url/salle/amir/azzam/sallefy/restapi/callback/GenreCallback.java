package edu.url.salle.amir.azzam.sallefy.restapi.callback;

import java.util.ArrayList;

import edu.url.salle.amir.azzam.sallefy.model.Genre;
import edu.url.salle.amir.azzam.sallefy.model.Track;

public interface GenreCallback {

    void onGenresReceive(ArrayList<Genre> genres);
    void onTracksByGenre(ArrayList<Track> tracks);
}
