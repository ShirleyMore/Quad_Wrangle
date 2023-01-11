package com.example.quadwrangle.game_model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AlphaBetaBoard extends Board {
    final static int LEVEL = 3;
    private int depth; // the depth of the tree
    private int val; // the value of the board for comparison later
    private Move move; // the move of the board
    private final double[][] scores = {
            {1.8,1.7,1.6,1.5,1.6,1.7,1.8},
            {1.7,1.6,1.5,1.4,1.5,1.6,1.7},
            {1.6,1.5,1.4,1.3,1.4,1.5,1.6},
            {1.5,1.4,1.3,1.2,1.3,1.4,1.5},
            {1.6,1.5,1.4,1.3,1.4,1.5,1.6},
            {1.7,1.6,1.5,1.4,1.5,1.6,1.7},
            {1.8,1.7,1.6,1.5,1.6,1.7,1.8}};

    public void setMove(Move move) {
        this.move = move;
    }

    public AlphaBetaBoard(Board board) {
        super(board.getSize());
        updateBoard(board.board);
        this.currentPlayer = board.currentPlayer;
        this.depth = LEVEL;
        this.pl1squares = board.getPl1squares();
        this.pl2squares = board.getPl2squares();
        //this.val = this.evaluateBoard();
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
        boolean canDrop = !this.checkANYPossibleSlideORGrowthOnBoard(); // can drop only if there is no slide or growth
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                Square currentSq = new Square(i,j);
                List<Move> movesForSq = getMovesForSquare(currentSq, canDrop);
                for (Move move : movesForSq) {
                    AlphaBetaBoard currentAB = new AlphaBetaBoard(this);
                    currentAB.move = move;
                    doMove(move, currentAB); // do the move for the board
                    // evaluate the board after the move was done times the player so the value <0 for -1
                    currentAB.setVal(currentAB.evaluateBoard()*(this.currentPlayer));
                    nextBoards.add(currentAB);  // add the new possible board move to the list
                    //System.out.println("IN ITERATE NEXT BOARDS: " + currentAB.val);
                    //System.out.println(move);
                    //currentAB.printboard();
                }
            }
        }
        return nextBoards;
    }

    public static void doMove(Move move, AlphaBetaBoard currentAB) {
        int currentPlayer = currentAB.getCurrentPlayer();
        Square currentSq = move.getSq1();
        Square destination = move.getSq2();
        if (move.getType().equals("slide")) // if the move is slide then-
            currentAB.board[currentSq.getRow()][currentSq.getCol()] = 0; // empty last sq
        else // add 1 to the score of growth and drop
            if (currentPlayer == 1)
                currentAB.pl1squares++;
            else
                currentAB.pl2squares++;
        currentAB.turnOverEnemiesAround(move.getNeighbours()); // turn over enemies if they exist and change scores
        currentAB.board[destination.getRow()][destination.getCol()] = currentPlayer; // place in new
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

    public ArrayList<Move> getMovesForSquare(Square sq, boolean canDrop) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        // check if it is not an enemy square:
        if (board[sq.getRow()][sq.getCol()] == currentPlayer*(-1))
            return possibleMoves;

        // if the square is empty -> check if legal for drop or growth
        if (isEmptySquare(sq)) {
            if (canDrop && isLegalMoveForDrop(sq)) { // if a drop is allowed (if there are no growth / slides)
                Move move = new Move(sq, sq, new ArrayList<Square>(), "drop", 0, 0, 0);
                possibleMoves.add(move); // add move to the list
            } else { // growth-
                ArrayList<Square> neighbours = sq.getNeighboursNoDiagonals(); // all squares orthogonal and diagonal to the square that wants to be placed
                int friendly = 0;
                int enemy = 0;
                int empty = 0;
                for (Square neighbour : neighbours) {
                    int val = board[neighbour.getRow()][neighbour.getCol()];
                    if (val == currentPlayer)
                        friendly++;
                    else if (val == currentPlayer*(-1))
                        enemy++;
                    else
                        empty++;
                }
                // if there is a friendly square around
                if (friendly != 0) {
                    Move move = new Move(sq, sq, neighbours, "growth", empty, enemy, friendly);
                    possibleMoves.add(move); // add move to the list
                }
            }
        }
        // if the square is occupied
        else if (!canDrop) { // check slides only if a drop is not possible
            // for each square on the board
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    Square sq2 = new Square(i, j); // 2nd location
                    // if the move is legal for slide
                    if (isLegalMoveForSlide(sq, sq2)) {
                        // getting neighbours of the new location
                        ArrayList<Square> neighbours = sq2.getNeighboursWithDiagonals();
                        int friendly = 0;
                        int enemy = 0;
                        int empty = 0;
                        for (Square neighbour : neighbours) {
                            int val = board[neighbour.getRow()][neighbour.getCol()];
                            if (val == currentPlayer)
                                friendly++;
                            else if (val == currentPlayer*(-1))
                                enemy++;
                            else
                                empty++;
                        }
                        Move move = new Move(sq, sq2, neighbours, "slide",  empty, enemy, friendly);
                        possibleMoves.add(move); // add the move to the list
                    }

                }
            }
        }
        return possibleMoves;
    }

    public void printAllMovesForBoard() {

        int count = 0;
        boolean canDrop = !this.checkANYPossibleSlideORGrowthOnBoard(); // can drop only if there is no slide or growth
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                Square sq = new Square(i,j);
                System.out.println("[" + i + ", " + j + "] square has: "+ board[i][j]);
                ArrayList<Move> movesForSq = getMovesForSquare(sq, canDrop);
                if (movesForSq.isEmpty()) System.out.println(" NO MOVES --------------");
                for (Move move : movesForSq) {
                    System.out.println(count + " " + move.toString());
                    count++;
                }
            }
        }
    }

    public int evaluateBoard() {
        if (currentPlayer == 1)
            return pl1squares-pl2squares;
        return pl2squares-pl1squares;
    }
    // returns 1 if the current player has more squares and -1 if the other player
    // has more. 0 if tie.
    // the function is called after the move was done!!
    public int evaluateBoard2() {
        String moveType = this.move.getType();
        if (moveType.equals("drop")) {
            return -100; // if we got to a drop it is very bad
        }
        else { // slide or growth
            int num_of_neighbours = move.getNeighbours_count();
            int friendly = move.getFriendly_neighbours_count();
            int enemy = move.getEnemy_neighbours_count();
            int empty = move.getEmpty_neighbours_count();

            if (moveType.equals("growth")) {
                if (friendly == num_of_neighbours) {
                    return 49; // if there are only friendly around = perfect move!!
                }
                if (friendly + enemy == 0)
                    return -100; //dead move
                // the more friendly+enemies the bigger
                return (int) ((pl1squares + pl2squares) * ((double)(friendly+enemy)/num_of_neighbours)); // != 0
            }
            else { // the move is slide
                if (enemy == num_of_neighbours) {
                    return 49; // if there are only enemies around = perfect move!!
                }
                // if there are NO enemies its a stupid move
                if (empty + friendly == 0) {
                    //System.out.println("--------------------------------------BAD MOVE");
                    return -100; // dead move does nothing (has no neighbours when done sliding)
                }
                else {
                    // the more enemies there are the bigger
                    return (int)(((pl1squares + pl2squares)) * ((double)enemy/num_of_neighbours));
                   /*if (this.currentPlayer == 1)
                        return pl1squares - pl2squares;
                    else
                        return pl2squares - pl1squares;*/
                }
            }
        }
    }
}
