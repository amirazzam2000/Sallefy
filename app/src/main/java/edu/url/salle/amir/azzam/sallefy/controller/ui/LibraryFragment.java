package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVertical;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVerticalRealm;
import edu.url.salle.amir.azzam.sallefy.controller.callbacks.TrackListCallback;
import edu.url.salle.amir.azzam.sallefy.controller.music.MusicService;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.model.TrackRealm;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.SearchCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.SearchManager;
import edu.url.salle.amir.azzam.sallefy.utils.RealmManager;
import io.realm.Realm;

public class LibraryFragment extends Fragment implements TrackListCallback {

    public static final String TAG = LibraryFragment.class.getName();

    private RecyclerView songsList;

    private  TrackRealm[] tracks;


    public static LibraryFragment getInstance() {
        return new LibraryFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_library, container, false);
        initViews(root);
        return root;
    }

    private void initViews(View v) {
        songsList = v.findViewById(R.id.dynamic_recyclerView);
        tracks = RealmManager.getInstance(getContext()).getSongs();

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        TrackListAdapterVerticalRealm adapter = new TrackListAdapterVerticalRealm( this, getActivity(), new ArrayList<TrackRealm>(Arrays.asList(tracks)));
        songsList.setLayoutManager(manager);
        songsList.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackSelected(int index, ArrayList<Track> tracks) {

    }

    @Override
    public void onTrackOfflineSelected(int index, ArrayList<TrackRealm> tracks) {
        MusicControllerFragment musicFragment = (MusicControllerFragment) getParentFragmentManager().findFragmentById(R.id.musicPlayer);
        ArrayList<Track> tracks1 = new ArrayList<>();
        for (TrackRealm t :
                tracks) {
            tracks1.add(new Track(t));
        }
        assert musicFragment != null;
        musicFragment.updateTrackOffline(index,tracks1);
    }
}
