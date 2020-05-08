package edu.url.salle.amir.azzam.sallefy.controller;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapter;
import edu.url.salle.amir.azzam.sallefy.controller.callbacks.TrackListCallback;
import edu.url.salle.amir.azzam.sallefy.controller.music.MusicPlayBackManager;
import edu.url.salle.amir.azzam.sallefy.controller.ui.MusicControllerFragment;
import edu.url.salle.amir.azzam.sallefy.model.Like;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.TrackCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.TrackManager;


public class DynamicPlaybackActivity extends AppCompatActivity implements TrackCallback {

    private static final String STOP_VIEW = "stop_view";
    private static final String PLAY_VIEW = "play_view";
    private static final String TAG = "DynamicPlaybackActivity";

    private TextView tvTitle;
    private TextView tvAuthor;
    private ImageView ivPicture;

    private ImageButton btnBackward;
    private ImageButton btnPlayStop;
    private ImageButton btnForward;
    private SeekBar mSeekBar;

    private Button like;
    private Button unlike;

    private Handler mHandler;
    private Runnable mRunnable;

    private BarVisualizer mVisualizer;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_song_view);
        initViews();
        getData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVisualizer != null)
            mVisualizer.release();
    }

    private void initViews() {
        mHandler = new Handler();


        tvAuthor = findViewById(R.id.dynamic_artist);
        tvTitle = findViewById(R.id.dynamic_title);
        ivPicture = (ImageView) findViewById(R.id.track_img);
        Glide.with(getApplicationContext())
                .asBitmap()
                .placeholder(R.drawable.ic_audiotrack)
                .load(R.drawable.ic_logo)
                .into(ivPicture);

        btnBackward = (ImageButton)findViewById(R.id.dynamic_backward_btn);
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayBackManager.getInstance().setCurrentTrack(((MusicPlayBackManager.getInstance().getCurrentTrack()-1)%(MusicPlayBackManager.getInstance().getMList().size())));
                MusicPlayBackManager.getInstance().setCurrentTrack( MusicPlayBackManager.getInstance().getCurrentTrack() < 0 ? (MusicPlayBackManager.getInstance().getMList().size()-1):MusicPlayBackManager.getInstance().getCurrentTrack());
                updateTrack(MusicPlayBackManager.getInstance().getCurrentTrack());
            }
        });
        btnForward = (ImageButton)findViewById(R.id.dynamic_forward_btn);
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayBackManager.getInstance().setCurrentTrack(((MusicPlayBackManager.getInstance().getCurrentTrack()+1)%(MusicPlayBackManager.getInstance().getMList().size())));
                MusicPlayBackManager.getInstance().setCurrentTrack( MusicPlayBackManager.getInstance().getCurrentTrack() >= MusicPlayBackManager.getInstance().getMList().size() ? 0 :MusicPlayBackManager.getInstance().getCurrentTrack());
                updateTrack(MusicPlayBackManager.getInstance().getCurrentTrack());
            }
        });

        btnPlayStop = (ImageButton)findViewById(R.id.dynamic_play_btn);
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

        mSeekBar = (SeekBar) findViewById(R.id.dynamic_seekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MusicPlayBackManager.getInstance().getMBoundService().seekTo(progress);
                }
                if (MusicPlayBackManager.getInstance().getMDuration() > 0) {
                    int newProgress = ((progress*100)/MusicPlayBackManager.getInstance().getMDuration());
                    System.out.println("New progress: " + newProgress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(MusicPlayBackManager.getInstance().ismServiceBound() && MusicPlayBackManager.getInstance().getMList() != null && MusicPlayBackManager.getInstance().getMList().size() > 0){
            if (MusicPlayBackManager.getInstance().isPlaying())
                playAudio();
            Track track = MusicPlayBackManager.getInstance().getMList().get(MusicPlayBackManager.getInstance().getCurrentTrack());
            tvAuthor.setText(track.getUserLogin());
            tvTitle.setText(track.getName());

            if (track.getThumbnail() != null) {
                Glide.with((getApplicationContext()))
                        .asBitmap()
                        .placeholder(R.drawable.ic_audiotrack)
                        .load(track.getThumbnail())
                        .into(ivPicture);
            }else{
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .placeholder(R.drawable.ic_audiotrack)
                        .load(R.drawable.ic_logo)
                        .into(ivPicture);
            }

        }else{
            updateSeekBar();
            tvAuthor.setText("Author");
            tvTitle.setText("Track name");
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(R.drawable.ic_logo)
                    .into(ivPicture);
        }

        like = findViewById(R.id.like_fill);
        like.setVisibility(View.GONE);

        unlike = findViewById(R.id.like_unfill);
        unlike.setVisibility(View.VISIBLE);
        DynamicPlaybackActivity activity = this;
        unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicPlayBackManager.getInstance().getMList() != null && MusicPlayBackManager.getInstance().getMList().size() > 0)
                    TrackManager.getInstance(getApplicationContext()).like(activity, MusicPlayBackManager.getInstance().getMList().get(MusicPlayBackManager.getInstance().getCurrentTrack()).getId());
            }
        });
        if (MusicPlayBackManager.getInstance().getMList() != null && MusicPlayBackManager.getInstance().getMList().size() > 0)
            TrackManager.getInstance(getApplicationContext()).isLiked(this, MusicPlayBackManager.getInstance().getMList().get(MusicPlayBackManager.getInstance().getCurrentTrack()).getId());

    }

    private void playAudio() {
        MusicPlayBackManager.getInstance().setPlaying(true);
        if (!MusicPlayBackManager.getInstance().getMBoundService().isPlaying()) {
            MusicPlayBackManager.getInstance().getMBoundService().togglePlayer();
        }
        updateSeekBar();
        btnPlayStop.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
        btnPlayStop.setTag(STOP_VIEW);
        //Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_SHORT).show();
    }

    private void pauseAudio() {
        MusicPlayBackManager.getInstance().setPlaying(false);
        if (MusicPlayBackManager.getInstance().getMBoundService().isPlaying()) { MusicPlayBackManager.getInstance().getMBoundService().togglePlayer(); }
        btnPlayStop.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
        btnPlayStop.setTag(PLAY_VIEW);
        //Toast.makeText(getApplicationContext(), "Pausing Audio", Toast.LENGTH_SHORT).show();
    }



    public void updateSeekBar() {
        mSeekBar.setMax(MusicPlayBackManager.getInstance().getMBoundService().getMaxDuration());
        mSeekBar.setProgress(MusicPlayBackManager.getInstance().getMBoundService().getCurrentPosition());
        if(MusicPlayBackManager.getInstance().getMBoundService().isPlaying()) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
            mHandler.postDelayed(mRunnable, 1000);
        }
    }


    public void updateTrack(int index) {
        Track track = MusicPlayBackManager.getInstance().getMList().get(index);
        like.setVisibility(View.GONE);
        unlike.setVisibility(View.VISIBLE);
        TrackManager.getInstance(getApplicationContext()).isLiked(this, track.getId());


        tvAuthor.setText(track.getUserLogin());
        tvTitle.setText(track.getName());

        if (track.getThumbnail() != null) {
            Glide.with((getApplicationContext()))
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(track.getThumbnail())
                    .into(ivPicture);
        }else{
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(R.drawable.ic_logo)
                    .into(ivPicture);
        }

        MusicPlayBackManager.getInstance().getMBoundService().playStream(track);

        btnPlayStop.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
        btnPlayStop.setTag(STOP_VIEW);
        updateSeekBar();
    }


    public void updateSessionMusicData(int offset) {
        /*int oldIndex = Session.getInstance(getApplicationContext()).getIndex();
        int size = Session.getInstance(getApplicationContext()).getTracks().size();
        int newIndex = (oldIndex + offset)%size;
        Session.getInstance(getApplicationContext()).setIndex(newIndex);
        Track newTrack = Session.getInstance(getApplicationContext()).getTracks().get(newIndex);
        Session.getInstance(getApplicationContext()).setTrack(newTrack);*/
    }


    private void getData() {

    }


    @Override
    public void onTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onTrackReceived(Track track) {

    }

    @Override
    public void onNoTracks(Throwable throwable) {

    }

    @Override
    public void onTrackDeleted() {

    }

    @Override
    public void onLikeReceived(Like like) {
        if(like.getLiked()){
            this.like.setVisibility(View.VISIBLE);
            unlike.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNoLike(Throwable throwable) {

    }

    @Override
    public void onPersonalTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onUserTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onUserLikedTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onCreateTrack() {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
