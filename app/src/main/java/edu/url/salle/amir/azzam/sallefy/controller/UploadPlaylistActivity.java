package edu.url.salle.amir.azzam.sallefy.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVertical;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVerticalSelect;
import edu.url.salle.amir.azzam.sallefy.controller.callbacks.TrackListCallback;
import edu.url.salle.amir.azzam.sallefy.controller.dialogs.ChoseSongDialog;
import edu.url.salle.amir.azzam.sallefy.controller.dialogs.StateDialog;
import edu.url.salle.amir.azzam.sallefy.model.Genre;
import edu.url.salle.amir.azzam.sallefy.model.Like;
import edu.url.salle.amir.azzam.sallefy.model.Playlist;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.PlaylistCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.TrackCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.CloudinaryManager;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.GenreManager;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.PlaylistManager;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.TrackManager;
import edu.url.salle.amir.azzam.sallefy.utils.Constants;
import edu.url.salle.amir.azzam.sallefy.utils.PreferenceUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UploadPlaylistActivity extends AppCompatActivity implements TrackCallback , TrackListCallback, PlaylistCallback {
    private ArrayList<Track> ChosenSongs;
    private ArrayList<Track> allSongs;
    private EditText etTitle, etDescription;
    private RecyclerView songsView;
    private TextView mCoverName, mImageName;
    private Button btnCover, btnCancel, btnAccept, btnImage, btnChoseSongs;
    private Switch aSwitch;
    private Uri mCoverUri;
    private Uri mFileImage;

    private int numRequests;
    private int numRequestsReceived;
    private boolean isPublic;
    private static String Cover;
    private static String Thumbnail;

    private final int COVER = 1;
    private final int THUMBNAIL = 2;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_playlist);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = getApplicationContext();
        initViews();
        getData();
    }
    private void initViews() {
        etTitle = (EditText) findViewById(R.id.create_song_title);
        etDescription = (EditText) findViewById(R.id.playlistDescription);
        mCoverName = (TextView) findViewById(R.id.playlistCoverText);
        ChosenSongs = new ArrayList<>();
        allSongs = new ArrayList<>();
        aSwitch = (Switch) findViewById(R.id.switch_is_public);

        btnCover = (Button) findViewById(R.id.playlistCover);
        btnCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromStorage(COVER);
            }
        });

        btnImage = (Button) findViewById(R.id.playlistThumbnail);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromStorage(THUMBNAIL);
            }
        });

        mImageName = (TextView) findViewById(R.id.playlistThumbnailURI);

        btnCancel = (Button) findViewById(R.id.create_song_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAccept = (Button) findViewById(R.id.create_song_accept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkParameters()) {
                    etTitle.setFocusable(false);
                    showStateDialog(false);
                    uploadToCloudinary();
                }
            }
        });

        btnChoseSongs = (Button) findViewById(R.id.addSongs);
        btnChoseSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStateDialog();
            }
        });



        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                        isPublic = true;
                } else {
                       isPublic = false;
                }
            }
        });
    }



    private boolean checkParameters() {
        if (!etTitle.getText().toString().equals("")) {
                return true;
        }
        return false;
    }


    private void getImageFromStorage(int num) {
        Intent intent = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Choose an Image"), num);
    }

    private void uploadToCloudinary() {
        CloudinaryManager.getInstance(this,this).setUploadPlaylistActivity(this);
        numRequests = 0;
        if (mCoverUri != null){
            CloudinaryManager.getInstance(this,this).uploadImageFile(mCoverUri, etTitle.getText().toString() +"(Cover)", false);
            numRequests++;
        }
        if(mFileImage != null) {
            CloudinaryManager.getInstance(this, this).uploadImageFile(mFileImage, etTitle.getText().toString() + "(Thumbnail)", false);
            numRequests++;
        }
        if(mFileImage == null & mCoverUri == null)
            finishUploading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COVER && resultCode == RESULT_OK && null != data) {
            mCoverUri = data.getData();
            mCoverName.setText(mCoverUri.toString());
        } else if (requestCode == THUMBNAIL && resultCode == RESULT_OK && null != data){
            mFileImage =  data.getData();
            assert mFileImage != null;
            mImageName.setText(mFileImage.getPath());
        }

    }

    private void showStateDialog() {
        ChoseSongDialog.getInstance(this).showDialog(allSongs, this);
    }


    private void showStateDialog(boolean completed) {
        StateDialog.getInstance(this).showStateDialog(completed);
    }

    private void getData() {
        TrackManager.getInstance(this).getAllTracks(this);
    }


    public void receiveMySongs(ArrayList<Track> chosenSongs) {
        ChosenSongs = chosenSongs;
        songsView = (RecyclerView) findViewById(R.id.selected_song);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        TrackListAdapterVertical adapter = new TrackListAdapterVertical(this, getApplicationContext(), chosenSongs);
        songsView.setLayoutManager(manager);
        songsView.setAdapter(adapter);
    }

    public void upload(ConcurrentLinkedQueue<Map> requests2){
        if (numRequestsReceived == 0 && requests2.peek() != null) {
            Cover = ((String) requests2.poll().get("url"));
            numRequestsReceived++;
        }

        if (requests2.peek() != null) {
            Thumbnail = ((String) requests2.poll().get("url"));
            numRequestsReceived++;
        }

        if(numRequestsReceived == numRequests)
            finishUploading();
    }
    public void finishUploading(){
        Playlist playlist = new Playlist();
        if (Cover != null)playlist.setCover(Cover);
        if (Thumbnail != null) playlist.setThumbnail(Thumbnail);
        playlist.setTracks(ChosenSongs);
        playlist.setDescription(etDescription.getText().toString());
        playlist.setName(etTitle.getText().toString());
        playlist.setPublicAccessible(isPublic);
        playlist.setUserLogin(PreferenceUtils.getUser(mContext));
        PlaylistManager.getInstance(mContext).createPlaylist(playlist, this);
    }

    @Override
    public void onTracksReceived(List<Track> tracks) {
        allSongs = (ArrayList<Track>) tracks;
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

    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackSelected(int index, ArrayList<Track> tracks) {

    }
    public void onCreatePlaylist() {
        StateDialog.getInstance(this).showStateDialog(true);
        Thread watchDialog = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (StateDialog.getInstance(mContext).isDialogShown()){}
                    finish();
                } catch (Exception e) {
                }
            }
        });
        watchDialog.start();
    }

    @Override
    public void onPlaylistById(Playlist playlist) {

    }

    @Override
    public void onPlaylistsByUser(ArrayList<Playlist> playlists) {

    }

    @Override
    public void onAllList(ArrayList<Playlist> playlists) {

    }

    @Override
    public void onFollowingList(ArrayList<Playlist> playlists) {

    }

    @Override
    public void onPlayListCreated(Playlist playlist) {
        StateDialog.getInstance(this).showStateDialog(true);
        Thread watchDialog = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (StateDialog.getInstance(mContext).isDialogShown()){}
                    finish();
                } catch (Exception e) {
                }
            }
        });
        watchDialog.start();
    }
}
