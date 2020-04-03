package edu.url.salle.amir.azzam.sallefy.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.dialogs.StateDialog;
import edu.url.salle.amir.azzam.sallefy.model.Genre;
import edu.url.salle.amir.azzam.sallefy.model.Like;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.GenreCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.TrackCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.CloudinaryManager;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.GenreManager;
import edu.url.salle.amir.azzam.sallefy.utils.Constants;

public class UploadActivity  extends AppCompatActivity implements GenreCallback, TrackCallback {
    private EditText etTitle;
    private Spinner mSpinner;
    private TextView mFilename;
    private Button btnFind, btnCancel, btnAccept;

    private ArrayList<String> mGenres;
    private ArrayList<Genre> mGenresObjs;
    private Uri mFileUri;

    private Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_song);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.ic_logo_dark);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        mContext = getApplicationContext();
        initViews();
        getData();
    }

    private void initViews() {
        etTitle = (EditText) findViewById(R.id.create_song_title);
        mFilename = (TextView) findViewById(R.id.create_song_file_name);

        mSpinner = (Spinner) findViewById(R.id.create_song_genre);

        btnFind = (Button) findViewById(R.id.create_song_file);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAudioFromStorage();
            }
        });

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

    }

    private void getData() {
        GenreManager.getInstance(this).getAllGenres(this);
    }

    private boolean checkParameters() {
        if (!etTitle.getText().toString().equals("")) {
            if (mFileUri != null) {
                return true;
            }
        }
        return false;
    }

    private void showStateDialog(boolean completed) {
        StateDialog.getInstance(this).showStateDialog(completed);
    }

    private void getAudioFromStorage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/mpeg");
        startActivityForResult(Intent.createChooser(intent, "Choose a song"), Constants.STORAGE.SONG_SELECTED);
    }

    private void uploadToCloudinary() {
        Genre genre = new Genre();
        for (Genre g: mGenresObjs) {
            if (g.getName().equals(mSpinner.getSelectedItem().toString())) {
                genre = g;
            }
        }
        CloudinaryManager.getInstance(this, this).uploadAudioFile(mFileUri, etTitle.getText().toString(), genre);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.STORAGE.SONG_SELECTED && resultCode == RESULT_OK) {
            mFileUri = data.getData();
            mFilename.setText(mFileUri.toString());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**********************************************************************************************
     *   *   *   *   *   *   *   *   GenreCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/

    @Override
    public void onGenresReceive(ArrayList<Genre> genres) {
        mGenresObjs = genres;
        mGenres = (ArrayList<String>) genres.stream().map(Genre -> Genre.getName()).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, mGenres);
        mSpinner.setAdapter(adapter);
    }

    @Override
    public void onTracksByGenre(ArrayList<Track> tracks) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    /**********************************************************************************************
     *   *   *   *   *   *   *   *   TrackCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/

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

    }

    @Override
    public void onNoLike(Throwable throwable) {

    }


    @Override
    public void onUserTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onUserLikedTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onCreateTrack() {
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
