package com.example.quadwrangle.game_view.ui.game;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.quadwrangle.game_model.Model;
import com.example.quadwrangle.game_model.Square;
import com.example.quadwrangle.game_model.database.SavedGamesDbManager;
import com.example.quadwrangle.game_view_model.savedGamesDbConnector;

public class GameViewModel extends ViewModel {

    // to Fragment
    private MutableLiveData<Integer> winner;
    public MutableLiveData<int[][]> mBoard;

    // to main activity
    public MutableLiveData<Integer> mTurn;
    public MutableLiveData<Integer> player1Score;
    public MutableLiveData<Integer> player2Score;
    public MutableLiveData<Square> mPressed_for_slide; // the first square for slide
    public MutableLiveData<Square> last_mPressed_for_slide; // the last selected square - so I can remove the glow
    public MutableLiveData<Boolean> isAI;

    public com.example.quadwrangle.game_view_model.savedGamesDbConnector savedGamesDbConnector;

    private final Model game;
    private static final int SIZE = 7;

    public MutableLiveData<Square> getmPressed_for_slide() {
        return mPressed_for_slide;
    }

    public MutableLiveData<Square> getLast_mPressed_for_slide() {
        return this.last_mPressed_for_slide;
    }

    public void run() {
        // code in the other thread, can reference "var" variable
        System.out.println("IN RUN");
        game.doMoveAI();
    }

    public MutableLiveData<Integer> getmTurn() {
        return this.mTurn;
    }

    public int getPlayer1Score() {
        return this.player1Score.getValue();
    }

    public int getPlayer2Score() {
        return this.player2Score.getValue();
    }

    public GameViewModel(Context context) {
        this.game = new Model(SIZE);
        this.mBoard = game.getmBoard();
        this.player1Score = game.getPlayer1Score();
        this.player2Score = game.getPlayer2Score();
        this.mTurn = game.getmTurn();
        this.mPressed_for_slide = game.getmPressed_for_slide();
        this.last_mPressed_for_slide = game.getLast_mPressed_for_slide();
        this.isAI = game.getIsAI();
        savedGamesDbConnector = new savedGamesDbConnector(context);
    }

    public boolean onTileClick(int row, int col) {
        // if the game is null start a new game
        if (game == null)
            reset();
        else {
            System.out.println("IN VMG: sq1: [" + row + ", " + col + "]");
            boolean moved = game.doMove(new Square(row, col)); // legal move + do move
            System.out.println("HAS MOVED?: " + moved);
            // if the game is over:
            if (game.isGameOver()) {
                if (game.getmWinner() != null) {
                    winner = game.getmWinner();
                    //if (winner.getValue() == -1)
                    //    this.player1Score.setValue(this.player1Score.getValue() + 1);
                    //else
                    //    this.player2Score.setValue(this.player2Score.getValue() + 1);
                }
                return true;
            }
            player1Score = game.getPlayer1Score(); // updating scores (because its Integer so its not a ktovet)
            player2Score = game.getPlayer2Score(); // updating scores (because its Integer so its not a ktovet)


            return moved;
        }
        return false;
    }

    public void doMoveAI() {
        if (isAI.getValue() && mTurn.getValue() == -1) {
            game.doMoveAI(); // DO AI MOVE IF AI IS ON
            player1Score = game.getPlayer1Score(); // updating scores (because its Integer so its not a ktovet)
            player2Score = game.getPlayer2Score(); // updating scores (because its Integer so its not a ktovet)
        }
    }

    public void saveGame(String name) {
        savedGamesDbConnector.saveGame(mBoard.getValue(), name, mTurn.getValue(), isAI.getValue());
    }


    public boolean isGameOver() {
        return this.game.isGameOver();
    }
    public MutableLiveData<Integer> getWinner() {
        return winner;
    }

    public void reset() {
        this.game.reset();
    }
}