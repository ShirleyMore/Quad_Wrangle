package com.example.quadwrangle.game_view.ui.game;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.quadwrangle.game_model.Model;
import com.example.quadwrangle.game_model.Square;
import com.example.quadwrangle.game_model.database.SavedGame;
import com.example.quadwrangle.game_view_model.SavedGamesDbConnector;

import java.util.Arrays;

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

    public SavedGamesDbConnector savedGamesDbConnector;

    private Model game;
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

    public GameViewModel(Context context, boolean isAI) {
        this.game = new Model(SIZE, isAI);
        this.mBoard = game.getmBoard();
        this.player1Score = game.getPlayer1Score();
        this.player2Score = game.getPlayer2Score();
        this.mTurn = game.getmTurn();
        this.mPressed_for_slide = game.getmPressed_for_slide();
        this.last_mPressed_for_slide = game.getLast_mPressed_for_slide();
        this.isAI = game.getIsAI();
        savedGamesDbConnector = new SavedGamesDbConnector(context);
    }

    public boolean onTileClick(int row, int col) {
        // if the game is null start a new game

        boolean moved = game.doMove(new Square(row, col)); // legal move + do move

        // if the game is over:
        if (game.isGameOver()) {
            if (game.getmWinner() != null) {
                winner = game.getmWinner();
            }
            return false;
        }
        player1Score = game.getPlayer1Score(); // updating scores (because its Integer so its not a ktovet)
        player2Score = game.getPlayer2Score(); // updating scores (because its Integer so its not a ktovet)


        return moved;
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

    public void newGame(Context context, boolean isAI) {
        this.game.newGame(isAI);
        this.mBoard = game.getmBoard();
        this.player1Score = game.getPlayer1Score();
        this.player2Score = game.getPlayer2Score();
        this.mTurn = game.getmTurn();
        this.mPressed_for_slide = game.getmPressed_for_slide();
        this.last_mPressed_for_slide = game.getLast_mPressed_for_slide();
        this.isAI = game.getIsAI();
        savedGamesDbConnector = new SavedGamesDbConnector(context);
    }

    public void loadGame(SavedGame extraData) {
        int[][] board_int = makeIntBoard(extraData.getBoard());
        boolean isAi = extraData.getGameType().equals("AI");
        int currentPlayer = Integer.parseInt(extraData.getNextPlayer());
        this.game.loadGame(board_int, currentPlayer, isAi);
        this.mBoard = game.getmBoard();
        this.player1Score = game.getPlayer1Score();
        this.player2Score = game.getPlayer2Score();
        this.mTurn = game.getmTurn();
        this.mPressed_for_slide = game.getmPressed_for_slide();
        this.last_mPressed_for_slide = game.getLast_mPressed_for_slide();
        this.isAI = game.getIsAI();
    }

    private int[][] makeIntBoard(String board_str) {
        int[][] board = new int[7][7];
        String[] rows = board_str.split("\n");
        for (int i = 0; i < 7; i++) {
            String row = rows[i];
            String[] cells = row.split(",");
            for (int j = 0; j < 7; j++) {
                String cell = cells[j];
                board[i][j] = Integer.parseInt(cell);
            }
        }
        System.out.println(Arrays.deepToString(board));
        return board;
    }
    /*
     public static String board_toString(int [][] board) {
        // board will always be 7x7
        StringBuilder boardStr = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                boardStr.append(board[i][j]).append(",");
            }
            boardStr.append("\n");
        }
        return boardStr.toString();
    }
     */

}