package edu.url.salle.amir.azzam.sallefy.controller;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.music.MusicPlayBackManager;
import edu.url.salle.amir.azzam.sallefy.model.Like;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.TrackCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.TrackManager;
import edu.url.salle.amir.azzam.sallefy.utils.RealmManager;


public class SongViewActivity extends AppCompatActivity implements TrackCallback {

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

    private Button download;
    private Button unDownload;

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
            updateSeekBar();
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
        SongViewActivity activity = this;
        unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicPlayBackManager.getInstance().getMList() != null && MusicPlayBackManager.getInstance().getMList().size() > 0)
                    TrackManager.getInstance(getApplicationContext()).like(activity, MusicPlayBackManager.getInstance().getMList().get(MusicPlayBackManager.getInstance().getCurrentTrack()).getId());
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicPlayBackManager.getInstance().getMList() != null && MusicPlayBackManager.getInstance().getMList().size() > 0)
                    TrackManager.getInstance(getApplicationContext()).like(activity, MusicPlayBackManager.getInstance().getMList().get(MusicPlayBackManager.getInstance().getCurrentTrack()).getId());
            }
        });
        if (MusicPlayBackManager.getInstance().getMList() != null && MusicPlayBackManager.getInstance().getMList().size() > 0)
            TrackManager.getInstance(getApplicationContext()).isLiked(this, MusicPlayBackManager.getInstance().getMList().get(MusicPlayBackManager.getInstance().getCurrentTrack()).getId());

        download = findViewById(R.id.download_fill);
        unDownload = findViewById(R.id.download_unfill);

        download.setVisibility(View.GONE);
        unDownload.setVisibility(View.VISIBLE);

        unDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicPlayBackManager.getInstance().getMList() != null && MusicPlayBackManager.getInstance().getMList().size() > 0) {
                    Track t = MusicPlayBackManager.getInstance().getMList().get(MusicPlayBackManager.getInstance().getCurrentTrack());
                    downloadSong(t);
                }
            }
        });


    }

    private void downloadSong(Track t) {
        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(t.getUrl());

        DownloadManager.Request request = new DownloadManager.Request(uri);
        String name = t.getName();
        name = name.replace(' ', '_');
        request.setTitle(name);
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(true);
        //request.setDestinationUri(Uri.parse(Environment
         //       .getExternalStorageDirectory().toString()+ "/Sallefy/"+name +".mp3"));
        //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"Sallefy/"+name +".mp3");
        // request.setDestinationInExternalPublicDir(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)),"Sallefy/"+name +".mp3");
        /*File f = new File(String.valueOf(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC)));
        if (!f.exists()){
            f.mkdir();
        }*/
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,"Sallefy/"+ name +".mp3");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            downloadmanager.enqueue(request);
            //t.setUrl(Environment.DIRECTORY_DOWNLOADS+"/Sallefy/"+name +".mp3");
            //t.setUrl(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))+"/Sallefy/"+name +".mp3");
            t.setUrl((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC )) +"/Sallefy/"+ name +".mp3");

            RealmManager.getInstance(getApplicationContext()).add(t);
            unDownload.setVisibility(View.GONE);
            download.setVisibility(View.VISIBLE);
        }else{
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Track t = MusicPlayBackManager.getInstance().getMList().get(MusicPlayBackManager.getInstance().getCurrentTrack());
                    downloadSong(t);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
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
        }else{
            this.like.setVisibility(View.GONE);
            unlike.setVisibility(View.VISIBLE);
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
