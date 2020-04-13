package edu.url.salle.amir.azzam.sallefy.controller.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.UploadActivity;
import edu.url.salle.amir.azzam.sallefy.controller.UploadPlaylistActivity;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVerticalSelect;
import edu.url.salle.amir.azzam.sallefy.controller.callbacks.TrackListCallback;
import edu.url.salle.amir.azzam.sallefy.model.Track;

public class ChoseSongDialog implements TrackListCallback {

    private static ChoseSongDialog sManager;
    private static Object mutex = new Object();

    private Context mContext;
    private Dialog mDialog;
    private RecyclerView songsView;


    private Button btnAccept;

    public static ChoseSongDialog getInstance(Context context) {
        if (sManager == null) {
            sManager = new ChoseSongDialog(context);
        }
        return sManager;
    }

    private ChoseSongDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext);
    }

    public void showDialog(ArrayList<Track> songs, UploadPlaylistActivity activity) {
        mDialog.setContentView(R.layout.select_songs);
        mDialog.setCanceledOnTouchOutside(true);

        songsView = (RecyclerView) mDialog.findViewById(R.id.songs);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        TrackListAdapterVerticalSelect adapter = new TrackListAdapterVerticalSelect(this, mContext, songs);
        songsView.setLayoutManager(manager);
        songsView.setAdapter(adapter);

        btnAccept = (Button) mDialog.findViewById(R.id.dialog_state_button);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.receiveMySongs(adapter.getChosenSongs());
                mDialog.cancel();
            }
        });


        mDialog.show();
    }




    public boolean isDialogShown() {
        return mDialog.isShowing();
    }


    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackSelected(int index, ArrayList<Track> tracks) {

    }
}
