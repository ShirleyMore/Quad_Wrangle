package com.example.quadwrangle.game_model.database;

public class GameInSavedGamesPage {

    // attributes
    private String name;
    private String date;
    private String type;

    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }

    public String getDate() {
        System.out.println(date);
        return date;
    }

    public GameInSavedGamesPage(String name, String date, String type) {
        this.name = name;
        this.date = date;
        this.type = type;
    }
}
