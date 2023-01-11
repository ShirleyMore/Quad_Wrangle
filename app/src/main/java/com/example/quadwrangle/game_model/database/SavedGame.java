package com.example.quadwrangle.game_model.database;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SavedGame {
    private long gameId;
    private long userId;
    private String saveName;
    private String dateSaved;
    private String board;
    private String nextPlayer;
    private String gameType; // AI // 2player

    public SavedGame(long gameId, long userId, String saveName, String dateSaved, String board, String nextPlayer, String gameType) {
        this.gameId = gameId;
        this.userId = userId;
        this.saveName = saveName;
        this.dateSaved = dateSaved;
        this.board = board;
        this.nextPlayer = nextPlayer;
        this.gameType = gameType;
    }

    public SavedGame(long userId, String saveName, String dateSaved, String board, String nextPlayer, String gameType) {
        this.userId = userId;
        this.saveName = saveName;
        this.dateSaved = dateSaved;
        this.board = board;
        this.nextPlayer = nextPlayer;
        this.gameType = gameType;
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

    public void setId(long id) {
        this.gameId = id;
    }

    public long getGameId() {
        return gameId;
    }

    public long getUserId() {
        return userId;
    }

    public String getSaveName() {
        return saveName;
    }

    public String getDateSaved() {
        return dateSaved;
    }

    public String getBoard() {
        return board;
    }

    public String getNextPlayer() {
        return nextPlayer;
    }

    public String getGameType() {
        return gameType;
    }

    @Override
    public String toString() {
        return "SavedGame{" +
                "gameId=" + gameId +
                ", userId=" + userId +
                ", saveName='" + saveName + '\'' +
                ", dateSaved='" + dateSaved + '\'' +
                ", board='" + board + '\'' +
                ", nextPlayer='" + nextPlayer + '\'' +
                ", gameType='" + gameType + '\'' +
                '}';
    }
}
