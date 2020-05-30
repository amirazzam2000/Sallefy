package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.SongViewActivity;
import edu.url.salle.amir.azzam.sallefy.controller.music.MusicCallback;
import edu.url.salle.amir.azzam.sallefy.controller.music.MusicPlayBackManager;
import edu.url.salle.amir.azzam.sallefy.controller.music.MusicService;
import edu.url.salle.amir.azzam.sallefy.controller.music.MusicUpdatesCallback;
import edu.url.salle.amir.azzam.sallefy.model.Like;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.TrackCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.TrackManager;
import edu.url.salle.amir.azzam.sallefy.restapi.service.SongViewService;


public class MusicControllerFragment extends Fragment implements MusicCallback , SongViewService, MusicUpdatesCallback, TrackCallback, PopupMenu.OnMenuItemClickListener {
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
    /*private Button like;
    private Button unlike;*/

    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";


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
        //mList = new ArrayList<>();
        MusicPlayBackManager.getInstance().setMList(new ArrayList<>());
        tvAuthor = v.findViewById(R.id.dynamic_artist);
        tvTitle = v.findViewById(R.id.dynamic_title);
        ivPicture = (ImageView) v.findViewById(R.id.track_img);
        Glide.with(getContext())
                .asBitmap()
                .placeholder(R.drawable.ic_audiotrack)
                .load(R.drawable.ic_logo)
                .into(ivPicture);




        btnBackward = (ImageButton)v.findViewById(R.id.dynamic_backward_btn);

        MusicPlayBackManager.getInstance().addCallback(this);
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*currentTrack = ((currentTrack-1)%(mList.size()));
                currentTrack = currentTrack < 0 ? (mList.size()-1):currentTrack;
                updateTrack(currentTrack);*/
                MusicPlayBackManager.getInstance().setCurrentTrack(((MusicPlayBackManager.getInstance().getCurrentTrack()-1)%(MusicPlayBackManager.getInstance().getMList().size())));
                MusicPlayBackManager.getInstance().setCurrentTrack( MusicPlayBackManager.getInstance().getCurrentTrack() < 0 ? (MusicPlayBackManager.getInstance().getMList().size()-1):MusicPlayBackManager.getInstance().getCurrentTrack());
                updateTrack(MusicPlayBackManager.getInstance().getCurrentTrack());
            }
        });
        btnForward = (ImageButton)v.findViewById(R.id.dynamic_forward_btn);
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*currentTrack = ((currentTrack+1)%(mList.size()));
                currentTrack = currentTrack >= mList.size() ? 0:currentTrack;
                updateTrack(currentTrack);*/
                MusicPlayBackManager.getInstance().setCurrentTrack(((MusicPlayBackManager.getInstance().getCurrentTrack()+1)%(MusicPlayBackManager.getInstance().getMList().size())));
                MusicPlayBackManager.getInstance().setCurrentTrack( MusicPlayBackManager.getInstance().getCurrentTrack() >= MusicPlayBackManager.getInstance().getMList().size() ? 0 :MusicPlayBackManager.getInstance().getCurrentTrack());
                updateTrack(MusicPlayBackManager.getInstance().getCurrentTrack());
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
                Intent i = new Intent(getActivity(), SongViewActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });

        /*like = v.findViewById(R.id.like_fill);
        like.setVisibility(View.GONE);

        unlike = v.findViewById(R.id.like_unfill);
        unlike.setVisibility(View.VISIBLE);
        MusicControllerFragment fragment = this;
        unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicPlayBackManager.getInstance().getMList() != null && MusicPlayBackManager.getInstance().getMList().size() > 0)
                TrackManager.getInstance(getContext()).like(fragment, MusicPlayBackManager.getInstance().getMList().get(MusicPlayBackManager.getInstance().getCurrentTrack()).getId());
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicPlayBackManager.getInstance().getMList() != null && MusicPlayBackManager.getInstance().getMList().size() > 0)
                    TrackManager.getInstance(getContext()).like(fragment, MusicPlayBackManager.getInstance().getMList().get(MusicPlayBackManager.getInstance().getCurrentTrack()).getId());
            }
        });*/
    }


    private void startStreamingService () {
        Intent intent = new Intent(getContext(), MusicService.class);
        //getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        getActivity().bindService(intent, MusicPlayBackManager.getInstance().getMServiceConnection(), Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (MusicPlayBackManager.getInstance().getMBoundService() != null) {
            resumeSongText();
            if (MusicPlayBackManager.getInstance().getMBoundService().isPlaying()) {
                playAudio();
            } else {
                pauseAudio();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (MusicPlayBackManager.getInstance().ismServiceBound()) {
            //pauseAudio();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        /*if (MusicPlayBackManager.getInstance().ismServiceBound()) {
            getActivity().unbindService(MusicPlayBackManager.getInstance().getMServiceConnection());
            MusicPlayBackManager.getInstance().setMServiceBound(false);
        }*/
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
        MusicPlayBackManager.getInstance().setPlaying(true);

        if (!MusicPlayBackManager.getInstance().getMBoundService().isPlaying()) {
            MusicPlayBackManager.getInstance().getMBoundService().togglePlayer();
        }
        //updateSeekBar();
        btnPlayStop.setImageResource(R.drawable.ic_pause);
        btnPlayStop.setTag(STOP_VIEW);
        //Toast.makeText(getContext(), "Playing Audio", Toast.LENGTH_SHORT).show();
    }

    private void pauseAudio() {
        MusicPlayBackManager.getInstance().setPlaying(false);

        if (MusicPlayBackManager.getInstance().getMBoundService().isPlaying()) { MusicPlayBackManager.getInstance().getMBoundService().togglePlayer(); }
        btnPlayStop.setImageResource(R.drawable.ic_play);
        btnPlayStop.setTag(PLAY_VIEW);
        //Toast.makeText(getContext(), "Pausing Audio", Toast.LENGTH_SHORT).show();
    }

    public void updateSeekBar() {
        System.out.println("max duration: " + MusicPlayBackManager.getInstance().getMBoundService().getMaxDuration());
        System.out.println("progress:" + MusicPlayBackManager.getInstance().getMBoundService().getCurrentPosition());
        //mSeekBar.setMax(mBoundService.getMaxDuration());
        //mSeekBar.setProgress(mBoundService.getCurrrentPosition());

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

    public void updateTrack(int index, ArrayList<Track> tracks) {
        /*like.setVisibility(View.GONE);
        unlike.setVisibility(View.VISIBLE);*/
        TrackManager.getInstance(getContext()).isLiked(this, tracks.get(index).getId());
        MusicPlayBackManager.getInstance().setMList(tracks);
        Track track = MusicPlayBackManager.getInstance().getMList().get(index);
        MusicPlayBackManager.getInstance().setCurrentTrack(index);
        tvAuthor.setText(track.getUserLogin());
        tvTitle.setText(track.getName());
        tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvTitle.setSingleLine(true);
        tvTitle.setMarqueeRepeatLimit(-1);
        tvTitle.setSelected(true);

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

        MusicPlayBackManager.getInstance().getMBoundService().playStream(track);

        btnPlayStop.setImageResource(R.drawable.ic_pause);
        btnPlayStop.setTag(STOP_VIEW);
        //updateSeekBar();
    }

    public void updateTrackOffline(int index, ArrayList<Track> tracks) {
        /*like.setVisibility(View.GONE);
        unlike.setVisibility(View.VISIBLE);*/
        TrackManager.getInstance(getContext()).isLiked(this, tracks.get(index).getId());
        MusicPlayBackManager.getInstance().setMList(tracks);
        Track track = MusicPlayBackManager.getInstance().getMList().get(index);
        MusicPlayBackManager.getInstance().setCurrentTrack(index);
        tvAuthor.setText(track.getUserLogin());
        tvTitle.setText(track.getName());
        tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvTitle.setSingleLine(true);
        tvTitle.setMarqueeRepeatLimit(-1);
        tvTitle.setSelected(true);

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

        MusicPlayBackManager.getInstance().getMBoundService().playStreamInternal(track);

        btnPlayStop.setImageResource(R.drawable.ic_pause);
        btnPlayStop.setTag(STOP_VIEW);
        //updateSeekBar();
    }

    public void updateTrack(int index) {
        Track track = MusicPlayBackManager.getInstance().getMList().get(index);

        /*like.setVisibility(View.GONE);
        unlike.setVisibility(View.VISIBLE);*/
        TrackManager.getInstance(getContext()).isLiked(this, track.getId());

        tvAuthor.setText(track.getUserLogin());
        tvTitle.setText(track.getName());
        tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvTitle.setSingleLine(true);
        tvTitle.setMarqueeRepeatLimit(-1);
        tvTitle.setSelected(true);

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

        MusicPlayBackManager.getInstance().getMBoundService().playStream(track);

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
        Track track = MusicPlayBackManager.getInstance().getMBoundService().getCurrentTrack();
        if (track != null) {
            tvAuthor.setText(track.getUserLogin());
            tvTitle.setText(track.getName());
            tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tvTitle.setSingleLine(true);
            tvTitle.setMarqueeRepeatLimit(-1);
            tvTitle.setSelected(true);
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

    @Override
    public void musicReady() {
        System.out.println("Entra en el prepared");
        //mSeekBar.setMax(mBoundService.getMaxDuration());
        mDuration =  MusicPlayBackManager.getInstance().getMBoundService().getMaxDuration();
        playAudio();
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
        /*if(like.getLiked()){
            this.like.setVisibility(View.VISIBLE);
            unlike.setVisibility(View.GONE);
        }else{
            this.like.setVisibility(View.GONE);
            unlike.setVisibility(View.VISIBLE);
        }*/
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

    public void myMenuClicked(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.music_menu, popup.getMenu());
        popup.show();
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.music_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.like:
                if (MusicPlayBackManager.getInstance().getMList() != null && MusicPlayBackManager.getInstance().getMList().size() > 0)
                    TrackManager.getInstance(getContext()).like(this, MusicPlayBackManager.getInstance().getMList().get(MusicPlayBackManager.getInstance().getCurrentTrack()).getId());
                return true;
            case R.id.download:
                SongViewActivity.getInstance().downloadSong(MusicPlayBackManager.getInstance().getMList().get(MusicPlayBackManager.getInstance().getCurrentTrack()));
                return true;
            default:
                return false;
        }
    }
}
