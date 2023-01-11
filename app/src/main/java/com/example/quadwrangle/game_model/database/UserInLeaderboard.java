package com.example.quadwrangle.game_model.database;

public class UserInLeaderboard {

    // attributes
    private String username;
    private int score;

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public UserInLeaderboard(String username, int score) {
        this.username = username;
        this.score = score;
    }

}
