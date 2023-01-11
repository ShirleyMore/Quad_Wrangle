package com.example.quadwrangle.game_model.database;

public class User {

    private long id;
    private String name;
    private String password;
    private int highScore;

    public User(String name, String password, int highScore) {
        this.name = name;
        this.password = password;
        this.highScore = highScore;
    }

    public User(long id, String name, String password, int highScore) {
        this.name = name;
        this.password = password;
        this.highScore = highScore;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getHighScore() {
        return highScore;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", highScore=" + highScore +
                '}';
    }
}
