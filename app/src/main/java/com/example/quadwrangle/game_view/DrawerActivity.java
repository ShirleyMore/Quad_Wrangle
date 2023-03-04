package com.example.quadwrangle.game_view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quadwrangle.R;
import com.example.quadwrangle.game_model.MyService;
import com.example.quadwrangle.game_model.database.SavedGame;
import com.example.quadwrangle.game_view.ui.game.GameFragment;
import com.example.quadwrangle.game_view.ui.home.HomeFragment;
import com.example.quadwrangle.game_view_model.UserDbLeaderboardConnector;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.quadwrangle.databinding.ActivityDrawerBinding;

public class DrawerActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDrawerBinding binding;
    private UserDbLeaderboardConnector userDbLeaderboardConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize binding
        binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // handle music button
        boolean musicOnOrOff = MyService.getWorking();
        if (musicOnOrOff)
            binding.music.setImageResource(R.drawable.ic_pause);
        else
            binding.music.setImageResource(R.drawable.ic_play);


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

    // https://developer.android.com/guide/fragments/communicate MVVM
    // https://stackoverflow.com/questions/13700798/basic-communication-between-two-fragments interface

    public void stopOrStartMusic(View view) {
        MyService.setWorking();
        boolean on_or_off = MyService.getWorking();
        ImageView background = (ImageView) view;
        if (on_or_off) { // on
            background.setImageResource(R.drawable.ic_pause);
            startService(new Intent(this, MyService.class));
        }
        else { // off
            background.setImageResource(R.drawable.ic_play);
            stopService(new Intent(this, MyService.class));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (handleMenu(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    public boolean handleMenu(MenuItem item) {
        if (item.getItemId() == R.id.action_howToPlay) {
            openHowToPlayDialog();
            return true;

        }
        return false;
    }

    private void openHowToPlayDialog() {
        //todo: cant save a finished game (check if board is full)
        final Dialog dialog = new Dialog(DrawerActivity.this);
        dialog.setCancelable(true); // make it so if we press everywhere it cancels
        dialog.setContentView(R.layout.rules_dialog); // connect to layout
        // initialize views
        TextView instructions = dialog.findViewById(R.id.instructions);
        instructions.setText("yes");
        // show the dialog
        dialog.show();
    }

}