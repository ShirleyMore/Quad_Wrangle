package com.example.quadwrangle.game_view.ui.game;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.quadwrangle.R;
import com.example.quadwrangle.databinding.FragmentGameBinding;
import com.example.quadwrangle.game_model.Square;
import com.example.quadwrangle.game_view.SquareButton;

public class GameFragment extends Fragment {

    private FragmentGameBinding binding;
    private ConstraintLayout layout;
    private GameViewModel viewModel;
    private SquareButton[][] board;
    private ImageView save_game;
    private ImageView change_mode;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        System.out.println("CREATING GAME FRAGMENT");

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game,container, false);
        View root = binding.getRoot();
        binding.setLifecycleOwner(this);
        // initialize the view model
        viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        createBoard();
        TextView score1 = binding.score1;
        TextView score2 = binding.score2;
        TextView gameType = binding.gameTypeText;
        // set the text to the game type:
        viewModel.isAI.observe(binding.getLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    gameType.setText("Game Type: AI");
                else
                    gameType.setText("Game Type: 2 player");
            }
        });

        save_game = binding.saveGameButton;
        save_game.setOnClickListener(this::SAVE_GAME);

        change_mode = binding.changeModeButton;
        change_mode.setOnClickListener(this::CHANGE_MODE);

        ImageView currentColorImg = binding.currentColorImage;
        // observer for current player image
        viewModel.mTurn.observe(binding.getLifecycleOwner(), new Observer<Integer>() {
            @Override
            // change the current player color image
            public void onChanged(Integer integer) {
                if (integer == 1)
                    currentColorImg.setImageResource(R.drawable.disc_black);
                else
                    currentColorImg.setImageResource(R.drawable.disc_white);
            }
        });

        // observer for mPressed_for_slide
        viewModel.mPressed_for_slide.observe(binding.getLifecycleOwner(), new Observer<Square>() {
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
        viewModel.last_mPressed_for_slide.observe(binding.getLifecycleOwner(), new Observer<Square>() {
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
        viewModel.player1Score.observe(binding.getLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer score) {
                // when the data is changed we need to update the images
                // updating the score text for black player
                binding.score1.setText(String.valueOf(viewModel.player1Score.getValue()));
            }
        });

        // observer for score2
        viewModel.player2Score.observe(binding.getLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer score) {
                // when the data is changed we need to update the images
                // updating the score text for white player
                binding.score2.setText(String.valueOf(viewModel.player2Score.getValue()));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void SAVE_GAME(View view) {
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setCancelable(true); // make it so if we press everywhere it cancels
        dialog.setContentView(R.layout.save_game_dialog); // connect to layout
        // initialize views
        TextView save_name_text = dialog.findViewById(R.id.save_text);
        ImageView save_button = dialog.findViewById(R.id.save);
        // set the save button listener
        save_button.setOnClickListener((V) -> {
            String name = save_name_text.getText().toString();
            viewModel.saveGame(name);
            dialog.dismiss();
            Toast.makeText(getContext(), "GAME SAVED!", Toast.LENGTH_SHORT).show();
        });
        // show the dialog
        dialog.show();
    }


    public void CHANGE_MODE(View view) {
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setCancelable(true); // make it so if we press everywhere it cancels
        dialog.setContentView(R.layout.change_mode_dialog); // connect to layout

        // initialize views
        TextView AI_button = dialog.findViewById(R.id.AI);
        ImageView AiBg = dialog.findViewById(R.id.imageView11);
        TextView twoPlayer_button = dialog.findViewById(R.id.two_player);
        ImageView twoBg = dialog.findViewById(R.id.imageView12);
        TextView title1 = dialog.findViewById(R.id.change_title);
        // 2nd screen
        TextView title2 = dialog.findViewById(R.id.are_you_sure);
        TextView title3 = dialog.findViewById(R.id.save_before);
        TextView yes = dialog.findViewById(R.id.yes);
        ImageView yesButton = dialog.findViewById(R.id.yesbttn);


        // set the save button listener
        AI_button.setOnClickListener((V) -> {
            AiBg.setVisibility(View.GONE);
            twoBg.setVisibility(View.GONE);
            title1.setVisibility(View.GONE);
            AI_button.setVisibility(View.GONE);
            twoPlayer_button.setVisibility(View.GONE);
            title2.setText("Are you sure you want to start a new AI game?");
            title2.setVisibility(View.VISIBLE);
            title3.setVisibility(View.VISIBLE);
            yes.setVisibility(View.VISIBLE);
            yesButton.setVisibility(View.VISIBLE);

            yes.setOnClickListener((V2) -> {
                // start a new AI game!
                viewModel.newGame(this.getContext(), true);
                dialog.dismiss();
            });
        });

        twoPlayer_button.setOnClickListener((V) -> {
            AiBg.setVisibility(View.GONE);
            twoBg.setVisibility(View.GONE);
            title1.setVisibility(View.GONE);
            AI_button.setVisibility(View.GONE);
            twoPlayer_button.setVisibility(View.GONE);
            title2.setText("Are you sure you want to start a new 2 player game?");
            title2.setVisibility(View.VISIBLE);
            title3.setVisibility(View.VISIBLE);
            yes.setVisibility(View.VISIBLE);
            yesButton.setVisibility(View.VISIBLE);

            yes.setOnClickListener((V2) -> {
                // start a new 2 player game!
                viewModel.newGame(this.getContext(), false);
                dialog.dismiss();
            });
        });

        dialog.show();
    }



    private void createBoard() {
        System.out.println("------------------- Creating board --------------------");

        int rows = 7;
        int cols = 7;

        Context context = binding.getRoot().getContext();
        layout = binding.layout;
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

                        String str = viewModel.getResultForClick(sq.getRow(), sq.getCol());
                        // timerForAI or gameOverDialog or nothing

                        if (str.equals("timerForAI")) { // if the game is not over
                            int seconds_delay = (int) (0.5 * 1000);
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

                        if (str.equals("gameOverDialog")) // if the game is over after the last move:
                            game_over_dialog();

                        // str equals "nothing" - > do nothing

                    }
                });
                layout.addView(board[row][col], lp);
            }
            viewModel.mBoard.observe(binding.getLifecycleOwner(), new Observer<int[][]>() {
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

    public void game_over_dialog() {
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setCancelable(false); // make it not cancellable
        dialog.setContentView(R.layout.game_over_dialog); // connect to layout
        TextView new_game = dialog.findViewById(R.id.new_game);
        // handle high score
        TextView high_score = dialog.findViewById(R.id.high_score);
        boolean newHighScore = handleHighScore();
        if (newHighScore) { // if there's a new high score (only for AI)
            high_score.setText("New high score of " + viewModel.getPlayer1Score());
        }
        else { // for 2 player or no new score in AI
            high_score.setText("Good job!");
        }
        // handle the winner
        TextView winner = dialog.findViewById(R.id.winner);
        String winner_string = viewModel.getWinner();
        //String winner_string = "help";
        if (winner_string.equals("tie")) {
            winner.setText("It's a tie.");
        }
        else {
            winner.setText("The winner is " + winner_string + "!");
        }

        //
        TextView AI = dialog.findViewById(R.id.ai);
        TextView twoPlayer = dialog.findViewById(R.id.twopl);
        ImageView b1 = dialog.findViewById(R.id.b1);
        ImageView b2 = dialog.findViewById(R.id.b2);
        ImageView bgButtonForNewGame = dialog.findViewById(R.id.imageView9);

        new_game.setOnClickListener((V) -> {
            AI.setVisibility(View.VISIBLE);
            twoPlayer.setVisibility(View.VISIBLE);
            b1.setVisibility(View.VISIBLE);
            b2.setVisibility(View.VISIBLE);
            new_game.setVisibility(View.GONE);
            bgButtonForNewGame.setVisibility(View.GONE);
        });

        AI.setOnClickListener((VAI) -> {
            viewModel.newGame(this.getContext(), true);
            dialog.dismiss();
        });

        twoPlayer.setOnClickListener((V2Player) -> {
            viewModel.newGame(this.getContext(), false);
            dialog.dismiss();
        });
        dialog.show();
    }

    public boolean handleHighScore() {
        return viewModel.handleHighScore();
    }


}