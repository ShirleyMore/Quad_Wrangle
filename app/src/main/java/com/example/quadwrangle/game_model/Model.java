package com.example.quadwrangle.game_model;

import static com.example.quadwrangle.game_model.AlphaBeta.bestMove;

import androidx.lifecycle.MutableLiveData;

import com.example.quadwrangle.game_view.SquareButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Model {
    protected Board board;

    private final MutableLiveData<int[][]> mBoard; // board livedata
    private final MutableLiveData<Integer> mTurn; // player turn livedata
    private MutableLiveData<Integer> mWinner;
    private MutableLiveData<Square> mPressed_for_slide; // the first square for slide
    private MutableLiveData<Square> last_mPressed_for_slide; // the last selected square - so I can remove the glow
    private MutableLiveData<Integer> player1Score;
    private MutableLiveData<Integer> player2Score;
    private MutableLiveData<Boolean> isAI;

    public MutableLiveData<int[][]> getmBoard() {return this.mBoard; }
    public MutableLiveData<Integer> getmTurn() {return this.mTurn; }
    public MutableLiveData<Integer> getmWinner() {return this.mWinner; }
    public MutableLiveData<Square> getmPressed_for_slide() {
        return this.mPressed_for_slide;
    }
    public MutableLiveData<Square> getLast_mPressed_for_slide() {
        return this.last_mPressed_for_slide;
    }

    public MutableLiveData<Integer> getPlayer1Score() {
        // because it is an integer- we don't get a ktovet, we just get the number
        // so every time I want the score I need to get it from board
        this.player1Score.setValue(board.getPl1squares());
        return this.player1Score;
    }

    public MutableLiveData<Boolean> getIsAI() {
        return this.isAI;
    }

    public MutableLiveData<Integer> getPlayer2Score() {
        // because it is an integer- we don't get a ktovet, we just get the number
        // so every time I want the score I need to get it from board
        this.player2Score.setValue(board.getPl2squares());
        return this.player2Score;
    }

    // constructor
    public Model(int size) {
        this.board = new Board(size); // creating a new board
        //this.board = new Board(); //<- for testing
        this.mBoard = new MutableLiveData<>();
        this.mBoard.setValue(board.getBoard()); // setting the board to the live data
        this.mTurn = new MutableLiveData<>();
        this.mTurn.setValue(board.getCurrentPlayer()); // setting current player to livedata
        this.mPressed_for_slide = new MutableLiveData<>();
        this.last_mPressed_for_slide = new MutableLiveData<>();
        this.player1Score = new MutableLiveData<>(); // player 1 score counter
        this.player1Score.setValue(board.getPl1squares());
        this.player2Score = new MutableLiveData<>(); // player 2 score counter
        this.player2Score.setValue(board.getPl2squares());
        this.isAI = new MutableLiveData<>();
        isAI.setValue(true);
        this.mWinner = new MutableLiveData<>();
        mWinner.setValue(0);
    }

    public boolean isGameOver() {
        return board.isGameOver();
    }

    public void updateWinner() {
        mWinner.setValue(board.getWinner());
    }

    public boolean legalMove(Square sq1, Square sq2) {
        return board.isLegalMove(sq1, sq2);
    }

    // returns true if the move worked and false otherwise
    public boolean doMove(Square sq1) {

        if (board.isGameOver()) { // game over
            mWinner.setValue(board.getWinner());
            return false;
        }

        // if it is an opponent square = bad move
        if (this.mBoard.getValue()[sq1.getRow()][sq1.getCol()] == this.board.getCurrentPlayer()*(-1)) {
            return false;
        }
        // if you pressed the saved one- remove the pressed
        if (this.mPressed_for_slide.getValue() != null) {
            // if the square is the same one remove pressed
            if (sq1.equals(this.mPressed_for_slide.getValue())) {
                this.last_mPressed_for_slide.setValue(this.mPressed_for_slide.getValue()); // give last value to last pressed
                this.mPressed_for_slide.setValue(null);
                return false;
            }
            // if the square and the last pressed have the same color = they meant to press the new one
            // = change the pressed to the new sq
            else if (this.mBoard.getValue()[this.mPressed_for_slide.getValue().getRow()][this.mPressed_for_slide.getValue().getCol()]
                    == this.mBoard.getValue()[sq1.getRow()][sq1.getCol()]) {
                this.last_mPressed_for_slide.setValue(this.mPressed_for_slide.getValue()); // give last value to last pressed
                this.mPressed_for_slide.setValue(sq1);
                return false;
            }
        }
        else if (!board.isEmptySquare(sq1)) {// if the square is occupied
                this.last_mPressed_for_slide.setValue(this.mPressed_for_slide.getValue()); // give last value to last pressed
                mPressed_for_slide.setValue(sq1); // add the square to the live data
                return false; // kind of worked
        }
        // now the square is empty:
        Square sq2 = mPressed_for_slide.getValue(); // getting the second square if it exists
        boolean moved;
        if (sq2 != null) {// if a second square exists
            // slide from sq2 (saved) to sq1 (new)
            moved = board.move(sq2, sq1); // do slide
            if (!moved) {// slide wasn't possible => not diagonal => keep last pressed
                return false;
            }

        }
        else {// do growth or drop
            moved = board.move(sq1, sq1); // do move + legal move
            this.last_mPressed_for_slide.setValue(this.mPressed_for_slide.getValue()); // give last value to last pressed
            mPressed_for_slide.setValue(null); // set the pressed square to null
        }
        this.mBoard.setValue(board.getBoard()); // update to livedata
        if (moved) // reset the pressed if the move worked
            mPressed_for_slide.setValue(null);

        if (board.isGameOver()) {
            return false; // end game
        }
        else {
            this.mTurn.setValue(board.getCurrentPlayer());
            //if (board.getCurrentPlayer() == -1)
        }
        if (moved) // if there was a change
            board.updateScores(); // update the scores after the move
        return moved;

    }

    public void doMoveAI() {

        if (board.isGameOver()) { // if the game is over
            this.mWinner.setValue(board.getWinner());
            return;
        }
        AlphaBetaBoard ab = new AlphaBetaBoard(board); // create new alpha beta board from current board
        Move move = bestMove(ab); // get the best move (AI)
        AlphaBetaBoard.doMove(move, ab); // do the move on the alpha beta board
        mBoard.setValue(ab.getBoard()); // transfer the board with the move done to the mutable
        board = ab; // update board
        board.nextPlayer(); // change player to the next player
        this.mTurn.setValue(board.getCurrentPlayer()); // update mutable
        board.updateScores(); // update the scores after the move

    }

    public void newGame(boolean isAI) {
        this.board = new Board(7);
        this.mBoard.setValue(board.getBoard());
        this.mTurn.setValue(board.getCurrentPlayer());
        this.isAI.setValue(isAI);
        mWinner.setValue(0);

    }

    public void loadGame(int[][] board_int, int currentPlayer, boolean isAI) {
        this.board = new Board(7, board_int, currentPlayer);
        this.mBoard.setValue(board.getBoard());
        this.mTurn.setValue(board.getCurrentPlayer());
        this.isAI.setValue(isAI);
        mWinner.setValue(0);
    }

}
