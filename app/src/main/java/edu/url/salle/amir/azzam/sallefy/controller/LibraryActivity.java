package edu.url.salle.amir.azzam.sallefy.controller;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.ui.HomeFragment;
import edu.url.salle.amir.azzam.sallefy.controller.ui.LibraryFragment;
import edu.url.salle.amir.azzam.sallefy.controller.ui.ProfileFragment;
import edu.url.salle.amir.azzam.sallefy.controller.ui.SearchFragment;

public class LibraryActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private int backButtonCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.ic_logo_dark);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        bottomNav = findViewById(R.id.nav_view);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFrag = null;

                switch (menuItem.getItemId()){
                    case R.id.navigation_home :
                        selectedFrag = new HomeFragment();
                        break;
                    case R.id.navigation_search:
                        selectedFrag = new SearchFragment();
                        break;
                    case R.id.navigation_Library:
                        selectedFrag = new LibraryFragment();
                        break;
                    case R.id.navigation_profile:
                        selectedFrag = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFrag)
                        .addToBackStack(null)
                        .commit();

                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
    }

}
