package edu.url.salle.amir.azzam.sallefy.controller.ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.url.salle.amir.azzam.sallefy.R;

public class SongViewFragment extends Fragment {

    private SongViewViewModel mViewModel;

    public static SongViewFragment newInstance() {
        return new SongViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.song_view_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SongViewViewModel.class);
        // TODO: Use the ViewModel
    }

}
