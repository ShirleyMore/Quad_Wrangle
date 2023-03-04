package com.example.quadwrangle.game_view_model;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.quadwrangle.game_model.Board;
import com.example.quadwrangle.game_model.database.GameInSavedGamesPage;
import com.example.quadwrangle.game_model.database.SavedGame;
import com.example.quadwrangle.game_model.database.SavedGamesDbManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class savedGamesDbConnector {

    private SavedGamesDbManager manager;

    public savedGamesDbConnector(Context context) {
        manager = new SavedGamesDbManager(context);
    }

    public GameInSavedGamesPage[] getAllSavedGamesForPage(long id){
        ArrayList<SavedGame> games = manager.getAllGamesForUser(id);
        GameInSavedGamesPage[] gamesArr = new GameInSavedGamesPage[games.size()];
        int i = 0;
        for (SavedGame game : games) {
            gamesArr[i] = new GameInSavedGamesPage(game.getSaveName(), game.getDateSaved(), game.getGameType());
            i++;
        }
        return gamesArr;
    }

    public void saveGame(int[][] board, String name, int nextPlayer, boolean isAI) {
        UserDbLeaderboardConnector userDbLeaderboardConnector = new UserDbLeaderboardConnector(null);
        long userid = userDbLeaderboardConnector.getMyId();
        // transfer Board into SavedGame
        String board_str = board_toString(board);
        Date now = new Date();
        long time = now.getTime();
        String date_str = date_ToString(time);
        String gameType;
        if (isAI)
            gameType = "AI";
        else
            gameType = "2player";
        String nextPlayer_str = nextPlayer+"";
        SavedGame savedGame = new SavedGame(userid, name, date_str, board_str, nextPlayer_str, gameType);
        manager.createSavedGame(savedGame);
    }

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

    public static String date_ToString(long date) {
        String pattern = "dd/MM/yyyy HH:mm:ss";
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

}
