package com.example.quadwrangle.game_view.ui.gallery;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.quadwrangle.R;
import com.example.quadwrangle.databinding.FragmentGalleryBinding;
import com.example.quadwrangle.game_model.Square;
import com.example.quadwrangle.game_view.SquareButton;
import com.example.quadwrangle.game_view_model.ViewModelGame;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private ConstraintLayout layout;
    private ViewModelGame viewModel;
    private SquareButton[][] board;
    private TextView score1;
    private TextView score2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery ,container, false);
        View root = binding.getRoot();
        binding.setLifecycleOwner(this);
        // initialize the view model
        viewModel = new ViewModelProvider(this).get(ViewModelGame.class);

        createBoard();
        score1 = binding.score1;
        score2 = binding.score2;

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
}