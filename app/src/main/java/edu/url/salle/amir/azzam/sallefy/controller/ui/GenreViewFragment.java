package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.model.Genre;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.GenreCallback;


public class GenreViewFragment extends Fragment implements GenreCallback {

    private ImageView ivGenreCoverPhoto;
    private ImageButton ibGenreThumbnail;
    private TextView tvGenreName;
    private TextView tvGenreAuthor;
    private RecyclerView rvGenreSongs;
    private Button btnFollow;
    private Button btnAuthorProfile;


    private OnFragmentInteractionListener mListener;
    public static GenreViewFragment getInstance() {
        return new GenreViewFragment();
    }

    public GenreViewFragment() {
        // Required empty public constructor
    }

    private void initView(View v) {

        tvGenreName = v.findViewById(R.id.dynamic_artist);
        ivGenreCoverPhoto = (ImageView) v.findViewById(R.id.track_img);

        rvGenreSongs = (RecyclerView) v.findViewById(R.id.dynamic_recyclerViewSong);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rvGenreSongs.setLayoutManager(manager);
        //rvPlaylistSongs.setAdapter(adapter);

        ibGenreThumbnail = (ImageButton)v.findViewById(R.id.playlistThumbnail);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genre_view, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onGenresReceive(ArrayList<Genre> genres) {

    }

    @Override
    public void onTracksByGenre(ArrayList<Track> tracks) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
