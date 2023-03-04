package com.example.quadwrangle.game_view_model;

import android.content.Context;

import com.example.quadwrangle.game_model.database.User;
import com.example.quadwrangle.game_model.database.UserDbManager;
import com.example.quadwrangle.game_model.database.UserInLeaderboard;

import java.util.ArrayList;

public class UserDbLeaderboardConnector {
    private UserDbManager manager;
    private static User MyUser;

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

    public boolean doesAlreadyExist(String name) {
        return manager.doesExistWithTheSameName(name);
    }

    public String signUp(String username, String password) {
        MyUser = manager.createUser(new User(username, password, 0));
        if (MyUser == null)
            return null;
        else
            return MyUser.getName();
    }

    public String logIn(String username, String password) {
        MyUser = manager.logIn(username, password);
        if (MyUser == null)
            return null;
        else
            return MyUser.getName();
    }

    public String getMyUsername() {
        return this.MyUser.getName();
    }

    public int getMyHighScore() {
        return this.MyUser.getHighScore();
    }


    public long getMyId() {
        System.out.println(MyUser);
        return this.MyUser.getId();
    }

    public void updateScore(int score, String username) {
        long id = manager.getIdForUsername(username);
        manager.updateScore(score, id);
    }

}
