package com.example.quadwrangle.game_model;

import java.util.ArrayList;

public class Move {
    private final Square sq1; // initial position
    private final Square sq2; // new position
    private final ArrayList<Square> neighbours; // neighbours of sq2
    private String type; // type = "growth"/"slide"/"drop"
    private final int empty_neighbours_count;
    private final int enemy_neighbours_count;
    private final int friendly_neighbours_count;

    public Move(Square sq1, Square sq2, ArrayList<Square> closeOpponents, String moveType, int empty_neighbours_count, int enemy_neighbours_count1, int friendly_neighbours_count) {
        this.sq1 = sq1;
        this.sq2 = sq2;
        this.neighbours = closeOpponents;
        this.type = moveType;
        this.empty_neighbours_count = empty_neighbours_count;
        this.enemy_neighbours_count = enemy_neighbours_count1;
        this.friendly_neighbours_count = friendly_neighbours_count;
    }

    public Square getSq1() {
        return sq1;
    }

    public Square getSq2() {
        return sq2;
    }

    public ArrayList<Square> getNeighbours() {
        return neighbours;
    }

    public String toString() {
        return ("[sq1:" + sq1 + ",sq2:" + sq2 + ",type:"+type);
    }

    public String getType() {
        return this.type;
    }

    public int getEmpty_neighbours_count() {
        return empty_neighbours_count;
    }

    public int getEnemy_neighbours_count() {
        return enemy_neighbours_count;
    }

    public int getFriendly_neighbours_count() {
        return friendly_neighbours_count;
    }

    public int getNeighbours_count() {
        return enemy_neighbours_count + empty_neighbours_count + friendly_neighbours_count;
    }
}
