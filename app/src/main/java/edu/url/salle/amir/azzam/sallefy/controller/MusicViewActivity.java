package edu.url.salle.amir.azzam.sallefy.controller;

import androidx.appcompat.app.AppCompatActivity;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.music.MusicService;
import edu.url.salle.amir.azzam.sallefy.controller.ui.MusicControllerFragment;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.SongViewManger;
import edu.url.salle.amir.azzam.sallefy.restapi.service.SongViewService;

import android.os.Bundle;

import java.util.ArrayList;

public class MusicViewActivity extends AppCompatActivity implements SongViewService {

    private static int currentTrack;
    private static ArrayList<Track> tracks;
    private static MusicService musicService;

    public static MusicViewActivity getInstance(){
        return new MusicViewActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_view_activty);
    }

    @Override
    public void onViewChangedToActivity(int index, ArrayList<Track> currentPlaylist, MusicService mBoundService) {
       /* currentTrack = index;
        tracks = currentPlaylist;
        musicService = mBoundService;
        SongViewManger.getInstance(this).transactionToActivityDone(MusicControllerFragment.getInstance());*/
    }

    @Override
    public void onSongsReceivedFromFragment() {
        /*super.onBackPressed();*/
    }

    @Override
    public void onViewChangedToFragment(int index, ArrayList<Track> currentPlaylist, MusicService mBoundService) {

    }

    @Override
    public void onSongsReceivedFromActivity() {

    }

    /*@Override
    public void onBackPressed(){
        onViewChangedToActivity(currentTrack,tracks,musicService);
    }*/

}
