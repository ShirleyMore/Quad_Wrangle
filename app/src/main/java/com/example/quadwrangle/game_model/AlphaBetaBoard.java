package com.example.quadwrangle.game_model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AlphaBetaBoard extends Board {
    final static int MAX_DEPTH = 3;
    private int depth; // the depth of the tree
    private int val; // the value of the board for comparison later
    private Move move; // the move of the board

    public void setMove(Move move) {
        this.move = move;
    }

    public AlphaBetaBoard(Board board) {
        super(board.getSize());
        updateBoard(board.board);
        this.currentPlayer = board.currentPlayer;
        this.depth = MAX_DEPTH;
        this.pl1squares = board.getPl1squares();
        this.pl2squares = board.getPl2squares();
    }

    public AlphaBetaBoard(AlphaBetaBoard board) {
        super(board.board.length);
        updateBoard(board.getBoard());
        this.currentPlayer = board.currentPlayer;
        this.pl1squares = board.getPl1squares();
        this.pl2squares = board.getPl2squares();
        this.depth = board.depth-1;
        this.val = board.getVal();
        this.move = board.getMove();
    }

    public int getDepth() {
        return depth;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public Move getMove() {
        return move;
    }

    public List<AlphaBetaBoard> getNextBoards() {
        return this.IterateNextBoards();
    }

    private void updateBoard(int[][] matrix) {
        for (int i = 0; i < this.board.length; i++) {
            System.arraycopy(matrix[i], 0, this.board[i], 0, this.board[i].length);
        }
    }

    public List<AlphaBetaBoard> IterateNextBoards() {
        List<AlphaBetaBoard> nextBoards = new ArrayList<>();
        // new lists for the moves
        List<Move> growthMovesForSquare = new ArrayList<>();
        List<Move> slideMovesForSquare = new ArrayList<>();
        List<Move> dropMovesForSquare = new ArrayList<>();
        // get all moves for each square
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                Square currentSq = new Square(i, j);
                growthMovesForSquare.addAll(getGrowthMovesForSquare(currentSq));
                slideMovesForSquare.addAll(getSlideMovesForSquare(currentSq));
                dropMovesForSquare.addAll(getDropMovesForSquare(currentSq));
            }
        }
        // check if the moves are drops or not:
        List<Move> moves = new ArrayList<>();
        if (growthMovesForSquare.isEmpty() && slideMovesForSquare.isEmpty()) // use drops
            moves.addAll(dropMovesForSquare);
        else {// slides and growths
            moves.addAll(growthMovesForSquare);
            moves.addAll(slideMovesForSquare);
        }
        for (Move move : moves) {
            AlphaBetaBoard currentAB = new AlphaBetaBoard(this);
            currentAB.move = move;
            doMove(move, currentAB); // do the move for the board
            // evaluate the board after the move was done times the player so the value <0 for -1
            currentAB.setVal(currentAB.evaluateBoard()*(this.currentPlayer));
            nextBoards.add(currentAB);  // add the new possible board move to the list
        }
        return nextBoards;
    }


    public static void doMove(Move move, AlphaBetaBoard currentAB) {
        Square sq1 = move.getSq1();
        Square sq2 = move.getSq2();
        if (move.getType().equals("slide")) { // if the move is slide then-
            currentAB.slide(sq1, sq2);
        }
        else if (move.getType().equals("growth")) { // if the move is growth then-
            currentAB.growth(sq1);
        }
        else // move is drop
            currentAB.drop(sq1);
        currentAB.updateScores(); // update the scores for the board
    }


    public List<AlphaBetaBoard> getNextBestBoards(int amount) {
        List<AlphaBetaBoard> nextBoards = this.IterateNextBoards();
        nextBoards.sort(Comparator.comparingInt(AlphaBetaBoard::getVal)); // sorting by parameter value from low to high
        if (this.currentPlayer == 1)
            Collections.reverse(nextBoards); // flipping so its high to low
        // getting the best 10 boards with highest scores
        nextBoards = nextBoards.stream().limit(amount).collect(Collectors.toList());
        //System.out.println("BEST BOARD IN GET NEXT BOARDS: " + nextBoards.get(0).val);
        return nextBoards;
    }


    public ArrayList<Move> getGrowthMovesForSquare(Square sq) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        if (isLegalMoveForGrowth(sq)) // if growth is possible
            possibleMoves.add(new Move(sq, sq, "growth"));
        return possibleMoves;
    }

    public ArrayList<Move> getSlideMovesForSquare(Square sq) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        // going over all the squares to check if the second square is any of them
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Square sq2 = new Square(i, j); // 2nd location
                // if the move is legal for slide
                if (isLegalMoveForSlide(sq, sq2)) {
                    // getting neighbours of the new location
                    Move move = new Move(sq, sq2, "slide");
                    possibleMoves.add(move); // add the move to the list
                }

            }
        }
        return possibleMoves;
    }

    public ArrayList<Move> getDropMovesForSquare(Square sq) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        if (isLegalMoveForDrop(sq)) // if growth is possible
            possibleMoves.add(new Move(sq, sq, "drop"));
        return possibleMoves;
    }



    public void printAllMovesForBoard() {

        int count = 0;
        boolean canDrop = !this.checkANYPossibleSlideORGrowthOnBoard(); // can drop only if there is no slide or growth
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                Square sq = new Square(i,j);
                System.out.println("[" + i + ", " + j + "] square has: "+ board[i][j]); /*
                ArrayList<Move> movesForSq = getMovesForSquare(sq, canDrop);
                if (movesForSq.isEmpty()) System.out.println(" NO MOVES --------------");
                for (Move move : movesForSq) {
                    System.out.println(count + " " + move.toString());
                    count++;
                } */
            }
        }
    }

    // abs because in iterateNextBoards I do it times the current player
    public int evaluateBoard() {
        if (currentPlayer == 1)
            return pl1squares-pl2squares;
        return pl2squares-pl1squares;
    }
}
