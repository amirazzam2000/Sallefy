package edu.url.salle.amir.azzam.sallefy.controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.ui.HomeFragment;
import edu.url.salle.amir.azzam.sallefy.controller.ui.LibraryFragment;
import edu.url.salle.amir.azzam.sallefy.controller.ui.MusicControllerFragment;
import edu.url.salle.amir.azzam.sallefy.controller.ui.ProfileFragment;
import edu.url.salle.amir.azzam.sallefy.controller.ui.SearchFragment;
import edu.url.salle.amir.azzam.sallefy.utils.Constants;
import edu.url.salle.amir.azzam.sallefy.utils.Session;

public class HomePageActivity extends AppCompatActivity {

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

        //getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.nav_host_fragment, new HomeFragment())
                .add(R.id.musicPlayer, new MusicControllerFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        /*if(backButtonCount >= 1){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.top_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent i = new Intent(getApplicationContext(), LogoutActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public void myMenuClicked(View view) {
        PopupMenu popup = new PopupMenu(getApplicationContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.music_menu, popup.getMenu());
        popup.show();
    }
}
