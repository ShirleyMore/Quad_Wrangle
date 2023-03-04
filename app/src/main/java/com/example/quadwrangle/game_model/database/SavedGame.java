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
