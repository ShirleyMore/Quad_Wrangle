package com.example.quadwrangle.game_model;

import java.util.ArrayList;

public class Move {
    private final Square sq1; // initial position
    private final Square sq2; // new position
    private String type; // type = "growth"/"slide"/"drop"

    public Move(Square sq1, Square sq2, String moveType) {
        this.sq1 = sq1;
        this.sq2 = sq2;
        this.type = moveType;
    }

    public Square getSq1() {
        return sq1;
    }

    public Square getSq2() {
        return sq2;
    }

    public String toString() {
        return ("[sq1:" + sq1 + ",sq2:" + sq2 + ",type:"+type+"]");
    }

    public String getType() {
        return this.type;
    }


}
