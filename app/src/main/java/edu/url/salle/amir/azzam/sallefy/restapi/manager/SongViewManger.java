package edu.url.salle.amir.azzam.sallefy.restapi.manager;


import android.content.Context;

import java.util.ArrayList;

import edu.url.salle.amir.azzam.sallefy.controller.music.MusicService;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.restapi.service.SongViewService;

public class SongViewManger {

    private static SongViewManger sSongViewManger;
    private Context mContext;
    private SongViewService mService;

    public static SongViewManger getInstance(Context context) {
        if (sSongViewManger == null) {
            sSongViewManger = new SongViewManger(context);
        }
        return sSongViewManger;
    }

    private SongViewManger(Context cntxt) {
        mContext = cntxt;
    }

    public void transactToActivity(int index, ArrayList<Track> currentPlaylist, MusicService mBoundService, SongViewService songView){
        songView.onViewChangedToActivity(index, currentPlaylist, mBoundService);

    }

    public void transactionToActivityDone(SongViewService songView){
        songView.onSongsReceivedFromFragment();
    }
}

