package com.example.quadwrangle.game_model;

import java.util.*;

public class Square {
    private int row;
    private int col;

    public Square(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public static boolean inLimit(int row, int col) {
        int size = Board.size;
        if (((row >= 0) && (row < size)) && ((col >= 0) && (col < size)))
            return true;
        return false;
    }

    public ArrayList<Square> getNeighboursNoDiagonals() {
        ArrayList<Square> lst = new ArrayList<Square>();
        if (inLimit(this.row-1, this.col))
            lst.add(new Square(this.row-1, this.col));
        if (inLimit(this.row+1, this.col))
            lst.add(new Square(this.row+1, this.col));
        if (inLimit(this.row, this.col-1))
            lst.add(new Square(this.row, this.col-1));
        if (inLimit(this.row, this.col+1))
            lst.add(new Square(this.row, this.col+1));
        return lst;
    }

    public ArrayList<Square> getNeighboursWithDiagonals() {
        ArrayList<Square> lst = new ArrayList<Square>();
        for (int r = (this.row-1); r <= (this.row+1); r++)
            for (int c = (this.col-1); c <= (this.col+1); c++)
                if (inLimit(r, c) && !(r == row && c == col))
                    lst.add(new Square(r, c));
        return lst;
    }

    public boolean equals(Square sq) {
        return ((sq.getRow() == this.getRow()) && (sq.getCol() == this.getCol()));

    }

    public String toString() {
        return ("[" + this.row + ", " + this.col + "]");
    }

}
