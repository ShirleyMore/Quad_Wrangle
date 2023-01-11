package com.example.quadwrangle.game_view_model;

import android.content.Context;

import com.example.quadwrangle.game_model.database.GameInSavedGamesPage;
import com.example.quadwrangle.game_model.database.SavedGame;
import com.example.quadwrangle.game_model.database.SavedGamesDbManager;

import java.util.ArrayList;

public class savedGamesDbConnector {

    private SavedGamesDbManager manager;

    public savedGamesDbConnector(Context context) {
        manager = new SavedGamesDbManager(context);
    }

    public GameInSavedGamesPage[] getAllSavedGamesForPage(int id){
        ArrayList<SavedGame> games = manager.getAllGamesForUser(id);
        GameInSavedGamesPage[] gamesArr = new GameInSavedGamesPage[games.size()];
        int i = 0;
        for (SavedGame game : games) {
            gamesArr[i] = new GameInSavedGamesPage(game.getSaveName(), game.getDateSaved(), game.getGameType());
            i++;
        }
        return gamesArr;
    }
}
