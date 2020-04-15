package edu.url.salle.amir.azzam.sallefy.controller.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.url.salle.amir.azzam.sallefy.controller.ui.MusicControllerFragment;
import edu.url.salle.amir.azzam.sallefy.model.Track;

public class MusicPlayBackManager implements MusicCallback{
    private ArrayList<MusicUpdatesCallback> callbacks;
    private static MusicPlayBackManager music;
    private MusicService mBoundService;
    private boolean mServiceBound = false;
    private ArrayList<Track> mList;
    private int currentTrack;
    private int mDuration;


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            mBoundService = binder.getService();
            mBoundService.setCallback(music);
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };


    public MusicPlayBackManager(){
        callbacks = new ArrayList<>();
    }

    public static MusicPlayBackManager getInstance(){
        if(music == null)
            music = new MusicPlayBackManager();

        return music;
    }

    @Override
    public void onMusicPlayerPrepared () {
        System.out.println("Entra en el prepared");
        //mSeekBar.setMax(mBoundService.getMaxDuration());
        mDuration = mBoundService.getMaxDuration();
        for (MusicUpdatesCallback call: callbacks) {
            call.musicReady();
        }
    }

    public void addCallback(MusicUpdatesCallback callback){
        callbacks.add(callback);
    }


    public  boolean ismServiceBound() {
        return mServiceBound;
    }

    public  void setMServiceBound(boolean mServiceBound) {
        mServiceBound = mServiceBound;
    }

    public  MusicService getMBoundService() {
        return mBoundService;
    }

    public  int getMDuration() {
        return mDuration;
    }

    public  void setMDuration(int mDuration) {
       this.mDuration = mDuration;
    }

    public  ArrayList<Track> getMList() {
        return mList;
    }

    public  void setMList(ArrayList<Track> mList) {
        this.mList = mList;
    }

    public  int getCurrentTrack() {
        return currentTrack;
    }

    public  void setCurrentTrack(int currentTrack) {
        this.currentTrack = currentTrack;
    }

    public  ServiceConnection getMServiceConnection() {
        return mServiceConnection;
    }

    public void setMServiceConnection(ServiceConnection mServiceConnection) {
        this.mServiceConnection = mServiceConnection;
    }
}
