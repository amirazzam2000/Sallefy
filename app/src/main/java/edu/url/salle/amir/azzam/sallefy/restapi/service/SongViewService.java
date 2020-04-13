package edu.url.salle.amir.azzam.sallefy.restapi.service;

import java.util.ArrayList;

import edu.url.salle.amir.azzam.sallefy.controller.music.MusicService;
import edu.url.salle.amir.azzam.sallefy.model.Track;

public interface SongViewService {
    public void onViewChangedToActivity(int index, ArrayList<Track> currentPlaylist, MusicService mBoundService);
    public void onSongsReceivedFromFragment();
    public void onViewChangedToFragment(int index, ArrayList<Track> currentPlaylist, MusicService mBoundService);
    public void onSongsReceivedFromActivity();
}
