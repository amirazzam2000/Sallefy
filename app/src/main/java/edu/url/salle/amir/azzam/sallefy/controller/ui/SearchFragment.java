package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.model.Search;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.SearchCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.SearchManager;

public class SearchFragment extends Fragment implements SearchCallback {

    private EditText searchBar;
    private String searchText;
    private ScrollView scrollView;
    /*
    private ImageButton mGenreRock;
    private ImageButton mGenreJazz;
    private ImageButton mGenrePop;
    private ImageButton mGenreRap;
    private ImageButton mGenreRnB;
    private ImageButton mGenreClassical;

     */

    public static final String TAG = SearchFragment.class.getName();

    public static SearchFragment getInstance() {
        return new SearchFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);
        initViews(root);
        return root;
    }

    private void initViews(View v) {
        scrollView = v.findViewById(R.id.scroll);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getParentFragmentManager().popBackStack();
                }
                return false;
            }
        } );

        SearchCallback searchCallback = this;
        searchBar = (EditText) v.findViewById(R.id.search_bar);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                scrollView.smoothScrollTo(1000,1000);
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    if (event == null || !event.isShiftPressed()) {
                        //Toast.makeText(getContext(), searchBar.getText().toString(), Toast.LENGTH_LONG).show();
                        // the user is done typing.
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                        searchText = searchBar.getText().toString();
                        SearchManager.getInstance(getActivity()).search(searchText, searchCallback);

                        return true; // consume.
                    }
                }
                return false; // pass on to other listeners.
            }
        });


        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                scrollView.smoothScrollTo(700,700);

            }

        });


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
    public void onSearchResultsReceive(Search search_result) {

        //Toast.makeText(getContext(), searchBar.getText().toString(), Toast.LENGTH_LONG).show();
        Fragment fragment = new SearchResults(searchText,search_result);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onFailureReceive(Throwable throwable) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
