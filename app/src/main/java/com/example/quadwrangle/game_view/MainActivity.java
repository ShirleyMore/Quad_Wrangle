package com.example.quadwrangle.game_view;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quadwrangle.databinding.ActivityMainBinding;
import com.example.quadwrangle.databinding.AppBarDrawerBinding;
import com.example.quadwrangle.game_model.AlphaBeta;
import com.example.quadwrangle.game_model.AlphaBetaBoard;
import com.example.quadwrangle.game_model.Move;
import com.example.quadwrangle.game_model.Square;
import com.example.quadwrangle.game_view_model.ViewModelGame;
import com.example.quadwrangle.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private ConstraintLayout layout;
    private ActivityMainBinding binding;
    private ViewModelGame viewModel;
    private SquareButton[][] board;
    private TextView score1;
    private TextView score2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.nav_activity_main);
        System.out.println("------------------- ON CREATE --------------------");

/*
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //binding.setViewModel(new ViewModelGame());
        viewModel = new ViewModelProvider(this).get(ViewModelGame.class);
        binding.setLifecycleOwner(this);
        //createBoard();
        score1 = findViewById(R.id.score1);
        score2 = findViewById(R.id.score2);

        //DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        //drawerLayout.openDrawer(GravityCompat.START);
        /*NavigationView navigationView = findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
                }
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });



        // drawer binding
        //drawer_binding = NavActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(drawer_binding.getRoot());

        createBoard();

        //sets toolbar
        //setSupportActionBar(drawer_binding.appBarDrawer.);
        //mAppBarConfiguration = new AppBarConfiguration.Builder(
          //      R.id.nav_home, R.id.nav_instructions, R.id.nav_leaderboard, R.id.nav_sign_in,
            //    R.id.nav_sign_out, R.id.nav_settings)
              //  .setOpenableLayout(drawer)
                //.build();


        // observer for mPressed_for_slide
        viewModel.mPressed_for_slide.observe(this, new Observer<Square>() {
            @Override
            public void onChanged(Square sq) {
                // when the data is changed we need to update the images
                // making the pressed square glow if it exists
                if (sq != null) {
                    int row = sq.getRow();
                    int col = sq.getCol();
                    board[row][col].setPressedImg(viewModel.mTurn.getValue());
                }
            }
        });

        // observer for last_mPressed_for_slide
        viewModel.last_mPressed_for_slide.observe(this, new Observer<Square>() {
            @Override
            public void onChanged(Square sq) {
                // when the data is changed we need to update the images
                // making the last pressed unglow
                if (sq != null) {
                    int row = sq.getRow();
                    int col = sq.getCol();
                    board[row][col].setPlayerImg(viewModel.mTurn.getValue());
                }
            }
        });

        // observer for score1
        viewModel.player1Score.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer score) {
                // when the data is changed we need to update the images
                // updating the score text for black player
                binding.score1.setText(String.valueOf(viewModel.player1Score.getValue()));
            }
        });

        // observer for score2
        viewModel.player2Score.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer score) {
                // when the data is changed we need to update the images
                // updating the score text for white player
                binding.score2.setText(String.valueOf(viewModel.player2Score.getValue()));
            }
        });
*/
    }



    private void createBoard() {
        System.out.println("------------------- Creating board --------------------");

        int rows = 7;
        int cols = 7;

        Context context = this.getBaseContext();
        layout = findViewById(R.id.layout);
        board = new SquareButton[rows][cols];

        int[][] idArray = new int[rows][cols];
        ConstraintSet cs = new ConstraintSet();

        // adding views to the ConstraintLayout
        for (int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                board[row][col] = new SquareButton(context, row, col);
                ConstraintLayout.LayoutParams lp =
                        new ConstraintLayout.LayoutParams(ConstraintSet.MATCH_CONSTRAINT,
                                ConstraintSet.MATCH_CONSTRAINT);
                int id = View.generateViewId();
                idArray[row][col] = id;
                board[row][col].setId(id);
                board[row][col].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SquareButton sq = (SquareButton) v;
                        System.out.println("START------------[" + sq.getRow() + ", " + sq.getCol() + "]");
                        boolean moved = viewModel.onTileClick(sq.getRow(), sq.getCol());

                        if (moved) {
                            int seconds_delay = (int)(0.5 * 1000);
                            new CountDownTimer(seconds_delay, seconds_delay) {

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    System.out.println("tick");
                                }

                                @Override
                                public void onFinish() {
                                    System.out.println("FINISHED!!!");
                                    viewModel.doMoveAI();
                                }
                            }.start();
                        }
                    }
                });
                layout.addView(board[row][col], lp);
            }
            viewModel.mBoard.observe(this, new Observer<int[][]>() {
                @Override
                public void onChanged(int[][] ints) {
                    // when the data is changed we need to update the images
                    // ints contains the images that need to be changed
                    for (int i = 0; i < ints.length; i++) {
                        for (int j = 0; j < ints[i].length; j++) {
                            board[i][j].setPlayerImg(ints[i][j]);
                        }
                    }
                }
            });
        }
        // Create a horizontal chain for each row and set the 1:1 dimensions.
        // but first make sure the layout frame has the right ratio set.
        cs.clone(layout);
        cs.setDimensionRatio(R.id.board, cols + " " + rows);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int id = idArray[row][col];
                cs.setDimensionRatio(id, "1:1");
                if (row == 0) {
                    // connect the top row to the top of the frame
                    cs.connect(id, ConstraintSet.TOP, R.id.board, ConstraintSet.TOP);
                }
                else {
                    // connect top to bottom of row above
                    cs.connect(id, ConstraintSet.TOP, idArray[row-1][0], ConstraintSet.BOTTOM);
                }
            }
            // create a horizontal chain that will determine the dimensions of our squares.
            cs.createHorizontalChain(R.id.board, ConstraintSet.LEFT,
                    R.id.board, ConstraintSet.RIGHT,
                    idArray[row], null, ConstraintSet.CHAIN_PACKED);
        }
        cs.applyTo(layout);
    }






}