package com.example.quadwrangle.game_view;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quadwrangle.R;
import com.example.quadwrangle.game_model.database.SavedGame;
import com.example.quadwrangle.game_view.ui.game.GameFragment;
import com.example.quadwrangle.game_view.ui.home.HomeFragment;
import com.example.quadwrangle.game_view_model.UserDbLeaderboardConnector;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.quadwrangle.databinding.ActivityDrawerBinding;

public class DrawerActivity extends AppCompatActivity implements HomeFragment.ILoadedGame{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDrawerBinding binding;
    private UserDbLeaderboardConnector userDbLeaderboardConnector;
    public InterfaceDataCommunicatorFromActivity interfaceDataCommunicatorFromActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize binding
        binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set toolbar
        setSupportActionBar(binding.appBarDrawer.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_saved_games, R.id.nav_game, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // initialize userDbLeaderboardConnector
        userDbLeaderboardConnector = new UserDbLeaderboardConnector(this.getBaseContext());

        // set text of nav header
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setDrawerHeader();
            }
        });
    }

    public void setDrawerHeader() {
        TextView username_text = findViewById(R.id.Username_text);
        TextView score_text = findViewById(R.id.Highscore_text);
        username_text.setText("Hello " + userDbLeaderboardConnector.getMyUsername());
        score_text.setText("Your high score is " + userDbLeaderboardConnector.getMyHighScore());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void loadGame(SavedGame extraData) {
        // https://stackoverflow.com/a/19679112/21079386
        // frag b = the one that sends the data = saved games table
        // frag a = the one that receives that data = game fragment
        interfaceDataCommunicatorFromActivity.updateData(extraData);
    }


    public interface InterfaceDataCommunicatorFromActivity {
        void updateData(SavedGame game);
    }
}