package com.example.quadwrangle.game_model;

import java.util.*;
public class Board {
    public static int size; // one size because its a square board
    protected int[][] board;
    protected int pl1squares; // player 1 is 1
    protected int pl2squares; // player 2 is -1
    // empty is 0
    protected int currentPlayer; // 1 or (-1 == 2)
    // protected for AlphaBetaBoard to inherit

    public Board(int size) {
        Board.size = size;
        this.pl1squares = (size * 2) - 4;
        this.pl2squares = (size * 2) - 4;
        int[][] board = new int[size][size]; // creating a new board
        this.board = board;
        this.assembleStart();
        this.currentPlayer = 1; // player 1 always starts
    }

    public Board() { // custom board for testing
        Board.size = 7;
        this.pl1squares = 46;
        this.pl2squares = 1;
        int[][] board = {
                {0,-1,1,1,1,1,1},
                {1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1},
                {1,1,1,1,0,1,1},
                {1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1}};
        this.board = board;
        //this.assembleStart();
        this.currentPlayer = 1; // player 1 always starts
    }

    public int[][] getBoard() {
        return this.board;
    }

    public int getSize() {
        return Board.size;
    }

    public void printboard() {
        System.out.println("  0 1 2 3 4 5 6");
        int count = 0;
        for (int row = 0; row < size; row++) {
            print(count + " ");
            count++;
            for (int col = 0; col < size; col++) {
                if (this.board[row][col] == -1)
                    System.out.print("2 ");
                else
                    System.out.print(this.board[row][col] + " ");
            }
            System.out.println();
        }
    }


    private void assembleStart() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (((row == 0) && ((col != 0)) && (col != size-1)) ||
                        ((col == 0) && ((row != 0)) && (row != size-1)))
                    this.board[row][col] = 1;
                else if (((row == size-1) && ((col != 0)) && (col != size-1)) ||
                        ((col == size-1) && ((row != 0)) && (row != size-1)))
                    this.board[row][col] = -1;
            }
        }
    }

    public void nextPlayer() {
        this.currentPlayer = this.currentPlayer * (-1);
    }

    // גדילה  Growth– אבן יכולה לגדל אבן חדשה בריבוע ריק במאונך לה או באלכסון לה. כל אבני היריב המאונכות לאבן החדשה מומרות לאבנים מאותו הצבע.
    // = change orthogonal
    // = place orthogonal or diagonal
    protected void growth(Square sq) {
        placeRock(sq);
        ArrayList<Square> lstNoDiagonals = sq.getNeighboursNoDiagonals();
        turnOverEnemiesAround(lstNoDiagonals); // change orthogonal and diagonal squares to current player type
        changePlayerSquareCount(1, this.currentPlayer); // add the new growth to count
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public void placeRock(Square sq) {
        this.board[sq.getRow()][sq.getCol()] = this.currentPlayer;
    }

    public void removeRock(Square sq) {
        this.board[sq.getRow()][sq.getCol()] = 0;
    }

    // החלקה Slide – אבן יכולה להחליק על פני הלוח בקו ישר או באלכסון כמו מלכה בשחמט (לכל מיקום עד הגעה לאבן אחרת).
    // כל אבני היריב הסמוכות למקום אליו האבן החליקה מומרות לאבנים מאותו הצבע.
    // = change orthogonal & diagonal
    // = place
    protected void slide(Square sq, Square newSpot) {
        placeRock(newSpot); // place rock in the new spot
        removeRock(sq); // remove current rock
        ArrayList<Square> lst = newSpot.getNeighboursWithDiagonals(); // I do this function on the new spot because
        // the rock was moved.
        turnOverEnemiesAround(lst); // turn over existing close enemies
    }

    public boolean isLegalMoveForGrowth(Square sq) {
        if (!isEmptySquare(sq))
            return false;
        ArrayList<Square> lstWithDiagonals = sq.getNeighboursWithDiagonals(); // all squares orthogonal and diagonal to the square that wants to be placed
        // if there is a friendly square around
        return areFriendlyNeighboursAround(lstWithDiagonals);
    }

    public boolean isLegalMove(Square sq, Square newSpot) {
        if (sq.equals(newSpot)) { // if the squares are the same = growth or drop
            if (checkANYPossibleSlideORGrowthOnBoard()) // if there is a possible slide or growth
                return isLegalMoveForGrowth(sq); // check if the growth is possible
            else // no possible slide or growth = drop
                if (isEmptySquare(sq))
                    if (isLegalMoveForDrop(sq))
                        return true; // true because if the square is empty drop is okay
        }
        else if(isLegalMoveForSlide(sq, newSpot)) // if the spots are different = slide
            return true;
        return false;
    }

    // 1 growth // 2 slide // 3 drop // 0 not possible
    public int isLegalMoveInt(Square sq, Square newSpot) {
        // if the square is empty
        if (isEmptySquare(sq)) {// if the square is empty
            if (sq.equals(newSpot)) { // if the squares are the same = growth or drop
                if (checkANYPossibleSlideORGrowthOnBoard()) { // if there is a possible slide or growth
                    if (isLegalMoveForGrowth(sq))  // check if the growth is possible
                        return 1; // growth
                    else
                        return 0; // not possible
                } else // no possible slide or growth = drop
                    if (isLegalMoveForDrop(sq))
                        return 3; // 3 because if the square is empty drop is okay
            }
        }
        // if the square is full
        else if (board[sq.getRow()][sq.getCol()] == currentPlayer) // if it is the right color
                if(isLegalMoveForSlide(sq, newSpot)) // if the spots are different = slide
                    return 2;
        return 0;
    }

    public void moveVoid(Square sq1, Square sq2) {
        int move = isLegalMoveInt(sq1, sq2);
        switch (move) {
            case 0:
                return;
            case 1:
                growth(sq1);
                break;
            case 2:
                slide(sq1, sq2);
                break;
            case 3:
                drop(sq1);
                break;
        }
        nextPlayer();
    }

    // this function does a move and returns if it worked
    // I made it so I wont have to check if the move is legal + check which move it is
    // so that I don't have to go over the matrix twice.
    public boolean move(Square sq1, Square sq2) {
        System.out.println("IN BOARD: loc1: "+sq1 +" loc2: "+ sq2);
        int move = isLegalMoveInt(sq1, sq2);
        System.out.println("Move type: " + move);
        switch (move) {
            case 0:
                return false;
            case 1:
                growth(sq1);
                break;
            case 2:
                slide(sq1, sq2);
                break;
            case 3:
                drop(sq1);
                break;
        }
        nextPlayer();
        System.out.println("SCORE " + this.pl1squares + " " + this.pl2squares);
        return true;
    }

    protected boolean isLegalMoveForSlide(Square sq, Square newSpot) {
        // if the new spot is occupied
        if (!isEmptySquare(newSpot))
            return false;

        // if the first spot is empty or the enemy's pawn
        if (board[sq.getRow()][sq.getCol()] != this.currentPlayer)
            return false;

        // if they didn't move at all
        if (sq.equals(newSpot))
            return false;

        int r1 = sq.getRow(), c1 = sq.getCol();
        int r2 = newSpot.getRow(), c2 = newSpot.getCol();
        int dx = r2 - r1;
        int dy = c2 - c1;

        if (dx == 0) { // straight line - on the same col
            dy = (dy > 0) ? 1 : -1; // make the distance -1 or +1 so I know what to add later
            return moveSlide(r1,r2,c1,c2,dx,dy);
        }

        else if (dy == 0) { // straight line - on the same row
            dx = (dx > 0) ? 1 : -1; // make the distance -1 or +1 so I know what to add later
            return moveSlide(r1,r2,c1,c2,dx,dy);
        }

        // If the new space is diagonal to the current square
        else if (Math.abs(r1 - r2) == Math.abs(c1 - c2)) {
            // make the distance -1 or +1 so I know what to add later
            dx = (dx > 0) ? 1 : -1;
            dy = (dy > 0) ? 1 : -1;
            return moveSlide(r1,r2,c1,c2,dx,dy);
        }

        else
            return false; // move is not allowed
    }

    protected boolean moveSlide(int r1, int r2, int c1, int c2, int dx, int dy) {
        int nextC = c1 + dy;
        int nextR = r1 + dx;
        if ((nextC == c2) && (nextR == r2)) // we reached the spot!
            return true;
        else if ((board[nextR][nextC] != 0)) // if the place is occupied
            return false;
        return moveSlide(nextR, r2, nextC, c2, dx, dy);
    }

    private int getValue(Square sq) {
        return this.board[sq.getRow()][sq.getCol()];
    }

    protected boolean areFriendlyNeighboursAround(ArrayList<Square> lst) {
        int p = this.currentPlayer;
        for (Square s : lst)
            if (getValue(s) == p) { // if any of the squares around are neighbours
                return true; // there is a neighbour = true
            }
        return false; // no neighbours = false
    }

    protected void turnOverEnemiesAround(ArrayList<Square> lst) {
        int countTurned = 0;
        int p = this.currentPlayer;
        for (Square s : lst)
            if (getValue(s) == p*(-1)) { // if the square is an enemy square
                placeRock(s); // change to current player type
                countTurned++;
            }
        // add the amount turned to the counters
        changePlayerSquareCount(countTurned, this.currentPlayer);
        // remove from other counter
        changePlayerSquareCount(-countTurned, this.currentPlayer*(-1));
    }

    private void changePlayerSquareCount(int num, int player) {
        if (player == 1)
            this.pl1squares += num;
        else
            this.pl2squares += num;
    }


    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    public int getPl1squares() {
        return this.pl1squares;
    }

    public int getPl2squares() {
        return this.pl2squares;
    }

    public void print(String str) {
        System.out.print(str);
    }

    public void println(String str) {
        System.out.println(str);
    }

    public int getPlaced(int player) {
        if (player == 1)
            return this.pl1squares;
        else
            return this.pl2squares;
    }

    public boolean isGameOver() {
        // if the board is full
        if ((this.pl1squares + this.pl2squares) == (size * size))
            return true;
        // if there are no possible moves for current player because there is an option
        // that there are no rocks left from a color
        // if there is a possible move return false
        return !checkANYPossibleMoveOnBoard();// there is no possible move = return game over
    }

    // returns if there is ANY possible move
    private boolean checkANYPossibleMoveOnBoard() {
        boolean drop = false;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Square sq = new Square(row, col);
                if (isEmptySquare(sq)) {// if the square is empty
                    if (checkAvailableGrowth(sq)) // if this place is allowed for growth
                        return true;
                    else if ((checkAvailableDrop(sq))) // also checking if a drop is possible so I don't have to go over the matrix twice
                        drop = true;
                }
                else // if the square is occupied
                    if (getValue(sq) == this.currentPlayer) // if it is the current players type
                        if (checkAvailableSlide(sq)) // if it is possible to slide this square
                            return true;
            }
        }
        return drop; // if I get down here - it means there was no possible slide or growth- so I return if there was a possible drop.
    }

    // this function returns if there is ANY possible growth or slide on the board
    protected boolean checkANYPossibleSlideORGrowthOnBoard() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Square sq = new Square(row, col);
                if (isEmptySquare(sq)) // if the square is empty
                    if (checkAvailableGrowth(sq)) // if this place is allowed for growth
                        return true;
                    else // if the square is occupied
                        if (getValue(sq) == this.currentPlayer) // if it is the current players type
                            if (checkAvailableSlide(sq)) // if it is possible to slide this square
                                return true;
            }
        }
        return false; // there were no slide or growth available
    }


    public boolean isEmptySquare(Square sq) {
        return (getValue(sq) == 0);
    }

    private int getWinner() {
        if (this.pl1squares > this.pl2squares)
            return 1;
        else if (this.pl1squares == this.pl2squares)
            return 3; // tie
        else
            return 2;
    }

    public boolean checkWin() {
        if (isGameOver()) {// if the game is not over
            System.out.println("Game Over!");
            System.out.println("Player " + getWinner() + " is the winner!");
            return true;
        }
        return false;
    }

    private boolean checkAvailableSlide(Square sq) {
        // sq is an occupied friendly square -> I need to check if it is possible to move it
        // diagonally or orthogonally.
        ArrayList<Square> squares = sq.getNeighboursWithDiagonals();
        if (areAnyEmpty(squares)) // if one of the squares around is empty = possible slide
            return true;
        return false;
    }

    private boolean checkAvailableGrowth(Square sq) {
        // sq is an empty square -> I have to check if it has friendly neighbours with diagonals
        ArrayList<Square> squares = sq.getNeighboursWithDiagonals();
        if (areFriendlyNeighboursAround(squares)) // if there are friendly around = available growth
            return true;
        return false;
    }

    private boolean checkAvailableDrop(Square sq) {
        ArrayList<Square> lst = sq.getNeighboursNoDiagonals(); // getting squares around no diagonals
        if (!areFriendlyNeighboursAround(lst)) // if there were no neighbours
            return true;
        return false;
    }

    protected void drop(Square sq) {
        placeRock(sq);
        changePlayerSquareCount(getCurrentPlayer(), 1);
    }

    protected boolean isLegalMoveForDrop(Square sq) {
        if (!isEmptySquare(sq)) // if the square is occupied
            return false;
        ArrayList<Square> lst = sq.getNeighboursNoDiagonals(); // getting squares around no diagonals
        if (!areFriendlyNeighboursAround(lst)) // if there are no friendly neighbours
            return true;
        return false;
    }

    private boolean areAnyEmpty(ArrayList<Square> lst) {
        for (Square s : lst)
            if (getValue(s) == 0) // if a square is empty = true
                return true;
        return false;
    }

    public int getScore(int player) {
        if (player == 1)
            return pl1squares;
        else
            return pl2squares;
    }

}