package com.example.quadwrangle.game_view_model;

import android.content.Context;

import com.example.quadwrangle.game_model.database.User;
import com.example.quadwrangle.game_model.database.UserDbManager;
import com.example.quadwrangle.game_model.database.UserInLeaderboard;

import java.util.ArrayList;

public class UserDbLeaderboardConnector {
    private UserDbManager manager;

    public UserDbLeaderboardConnector(Context context) {
        manager = new UserDbManager(context);
    }

    public UserInLeaderboard[] getAllUsersInLeaderboard(){
        ArrayList<User> users = manager.getAllUsers();
        UserInLeaderboard[] usersForLeaderboard = new UserInLeaderboard[users.size()];
        int i = 0;
        for (User user : users) {
            usersForLeaderboard[i] = new UserInLeaderboard(user.getName(), user.getHighScore());
            i++;
        }
        return usersForLeaderboard;
    }

}
