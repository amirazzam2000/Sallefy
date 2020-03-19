package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.url.salle.amir.azzam.sallefy.R;

public class LibraryFragment extends Fragment  {

    public static final String TAG = LibraryFragment.class.getName();

    public static LibraryFragment getInstance() {
        return new LibraryFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_library, container, false);
        return root;
    }


}
