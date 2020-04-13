package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.MusicViewActivity;
import edu.url.salle.amir.azzam.sallefy.controller.music.MusicCallback;
import edu.url.salle.amir.azzam.sallefy.controller.music.MusicService;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.SongViewManger;
import edu.url.salle.amir.azzam.sallefy.restapi.service.SongViewService;


public class MusicControllerFragment extends Fragment implements MusicCallback , SongViewService {
    public static final String TAG = MusicControllerFragment.class.getName();

    private Handler mHandler;
    private Runnable mRunnable;

    private TextView tvTitle;
    private TextView tvAuthor;
    private ImageView ivPicture;

    private ImageButton btnBackward;
    private ImageButton btnPlayStop;
    private ImageButton btnForward;
    private int mDuration;

    private Button viewButton;

    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";
    private MusicService mBoundService;
    private boolean mServiceBound = false;
    private ArrayList<Track> mList;
    private int currentTrack;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            mBoundService = binder.getService();
            mBoundService.setCallback(MusicControllerFragment.this);
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };

    public MusicControllerFragment() {
    }

    public static MusicControllerFragment getInstance() {
        return new MusicControllerFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_music_controller_fragmetn, container, false);
        mDuration = 0;
        initView(root);
        return root;
    }

    private void initView(View v) {
        mHandler = new Handler();
        mList = new ArrayList<>();
        tvAuthor = v.findViewById(R.id.dynamic_artist);
        tvTitle = v.findViewById(R.id.dynamic_title);
        ivPicture = (ImageView) v.findViewById(R.id.track_img);
        Glide.with(getContext())
                .asBitmap()
                .placeholder(R.drawable.ic_audiotrack)
                .load(R.drawable.ic_logo)
                .into(ivPicture);


        btnBackward = (ImageButton)v.findViewById(R.id.dynamic_backward_btn);
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTrack = ((currentTrack-1)%(mList.size()));
                currentTrack = currentTrack < 0 ? (mList.size()-1):currentTrack;
                updateTrack(currentTrack);
            }
        });
        btnForward = (ImageButton)v.findViewById(R.id.dynamic_forward_btn);
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTrack = ((currentTrack+1)%(mList.size()));
                currentTrack = currentTrack >= mList.size() ? 0:currentTrack;
                updateTrack(currentTrack);
            }
        });

        btnPlayStop = (ImageButton)v.findViewById(R.id.dynamic_play_btn);
        btnPlayStop.setTag(PLAY_VIEW);
        btnPlayStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (btnPlayStop.getTag().equals(PLAY_VIEW)) {
                    playAudio();
                } else {
                    pauseAudio();
                }
            }
        });

        viewButton = (Button) v.findViewById(R.id.openMusicView);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongViewManger.getInstance(getContext()).transactToActivity(currentTrack,mList, mBoundService, MusicViewActivity.getInstance());
            }
        });
    }


    private void startStreamingService () {
        Intent intent = new Intent(getContext(), MusicService.class);
        getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mBoundService != null) {
            resumeSongText();
            if (mBoundService.isPlaying()) {
                playAudio();
            } else {
                pauseAudio();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mServiceBound) {
            //pauseAudio();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mServiceBound) {
            getActivity().unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        startStreamingService();
    }


    private void playAudio() {
        if (!mBoundService.isPlaying()) { mBoundService.togglePlayer(); }
        //updateSeekBar();
        btnPlayStop.setImageResource(R.drawable.ic_pause);
        btnPlayStop.setTag(STOP_VIEW);
        //Toast.makeText(getContext(), "Playing Audio", Toast.LENGTH_SHORT).show();
    }

    private void pauseAudio() {
        if (mBoundService.isPlaying()) { mBoundService.togglePlayer(); }
        btnPlayStop.setImageResource(R.drawable.ic_play);
        btnPlayStop.setTag(PLAY_VIEW);
        //Toast.makeText(getContext(), "Pausing Audio", Toast.LENGTH_SHORT).show();
    }

    public void updateSeekBar() {
        System.out.println("max duration: " + mBoundService.getMaxDuration());
        System.out.println("progress:" + mBoundService.getCurrrentPosition());
        //mSeekBar.setMax(mBoundService.getMaxDuration());
        //mSeekBar.setProgress(mBoundService.getCurrrentPosition());

        if(mBoundService.isPlaying()) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
            mHandler.postDelayed(mRunnable, 1000);
        }
    }

    public void updateTrack(int index, ArrayList<Track> tracks) {
        mList = tracks;
        Track track = mList.get(index);
        currentTrack = index;
        tvAuthor.setText(track.getUserLogin());
        tvTitle.setText(track.getName());

        if (track.getThumbnail() != null) {
            Glide.with(getContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(track.getThumbnail())
                    .into(ivPicture);
        }else{
            Glide.with(getContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(R.drawable.ic_logo)
                    .into(ivPicture);
        }

        mBoundService.playStream(track);

        btnPlayStop.setImageResource(R.drawable.ic_pause);
        btnPlayStop.setTag(STOP_VIEW);
        //updateSeekBar();
    }

    public void updateTrack(int index) {
        Track track = mList.get(index);

        tvAuthor.setText(track.getUserLogin());
        tvTitle.setText(track.getName());

        if (track.getThumbnail() != null) {
            Glide.with(getContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(track.getThumbnail())
                    .into(ivPicture);
        }else{
            Glide.with(getContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(R.drawable.ic_logo)
                    .into(ivPicture);
        }

        mBoundService.playStream(track);

        btnPlayStop.setImageResource(R.drawable.ic_pause);
        btnPlayStop.setTag(STOP_VIEW);
        //updateSeekBar();
    }

/*    public void updateSeekBar() {
        System.out.println("max duration: " + mBoundService.getMaxDuration());
        System.out.println("progress:" + mBoundService.getCurrrentPosition());
        //mSeekBar.setMax(mBoundService.getMaxDuration());
        //mSeekBar.setProgress(mBoundService.getCurrrentPosition());

        if(mBoundService.isPlaying()) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
            mHandler.postDelayed(mRunnable, 1000);
        }
    }*/

    private void resumeSongView(boolean isPlaying) {
        if (isPlaying) {
            btnPlayStop.setImageResource(R.drawable.ic_pause);
            btnPlayStop.setTag(STOP_VIEW);
        } else {
            btnPlayStop.setImageResource(R.drawable.ic_play);
            btnPlayStop.setTag(PLAY_VIEW);
        }
    }


    private void resumeSongText() {
        Track track = mBoundService.getCurrentTrack();
        if (track != null) {
            tvAuthor.setText(track.getUserLogin());
            tvTitle.setText(track.getName());
            if (track.getThumbnail() != null) {
                Glide.with(getContext())
                        .asBitmap()
                        .placeholder(R.drawable.ic_audiotrack)
                        .load(track.getThumbnail())
                        .into(ivPicture);
            }
            else {
                Glide.with(getContext())
                        .asBitmap()
                        .placeholder(R.drawable.ic_audiotrack)
                        .load(R.drawable.ic_logo)
                        .into(ivPicture);
            }
        }
    }

    @Override
    public void onMusicPlayerPrepared() {
        System.out.println("Entra en el prepared");
        //mSeekBar.setMax(mBoundService.getMaxDuration());
        mDuration =  mBoundService.getMaxDuration();
        playAudio();

    }

    @Override
    public void onViewChangedToActivity(int index, ArrayList<Track> currentPlaylist, MusicService mBoundService) {
       /* currentTrack = index;
        mList = currentPlaylist;
        this.mBoundService = mBoundService;
        SongViewManger.getInstance(getContext()).transactionToActivityDone(MusicViewActivity.getInstance());*/
    }

    @Override
    public void onSongsReceivedFromFragment() {
       /* Intent i = new Intent(getActivity(), MusicViewActivity.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);*/
    }

    @Override
    public void onViewChangedToFragment(int index, ArrayList<Track> currentPlaylist, MusicService mBoundService) {

    }

    @Override
    public void onSongsReceivedFromActivity() {

    }
}
