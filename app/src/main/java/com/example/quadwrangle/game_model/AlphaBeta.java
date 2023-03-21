package com.example.quadwrangle.game_model;

import java.util.List;

public class AlphaBeta {
    static final int MAX_PLAYER = -1; // the number of the MAX player // aka the white player

    public static Move bestMove(AlphaBetaBoard board) {
        Move move = null;
        List<AlphaBetaBoard> children = board.IterateNextBoards(); // list of new children boards of possibilities
        for (AlphaBetaBoard child : children) {
            // set the value of the minmax to each of the children
            child.setVal(minimax(child, child.getDepth(), Integer.MIN_VALUE, Integer.MAX_VALUE));
            //System.out.println("IN BEST MOVE: " + child.getVal());
        }
        // get the best child for max / min
        if (board.getCurrentPlayer() == MAX_PLAYER) {
            // max player is choosing the board with the highest value
            int highestValue = Integer.MIN_VALUE; // starting with the smallest value and going up
            for (AlphaBetaBoard child : children) {
                int val = child.getVal();
                if (val >= highestValue) {
                    highestValue = val;
                    move = child.getMove();
                }
            }
            board.setVal(highestValue);
        }
        else {
            // min player is choosing the board with the lowest value
            int lowestValue = Integer.MAX_VALUE; // starting with the biggest value and going down
            for (AlphaBetaBoard child : children) {
                int val = child.getVal();
                if (val <= lowestValue) {
                    lowestValue = val;
                    move = child.getMove();
                }
            }
            board.setVal(lowestValue);
        }
        board.setMove(move);
        return move;
    }

    public static int minimax(AlphaBetaBoard abBoard, int depth, int alpha, int beta) {
        int val;
        if (depth == 0 || abBoard.isGameOver()) {
            val = abBoard.evaluateBoard();
            abBoard.setVal(val);
            return val;
        }
        List<AlphaBetaBoard> nextBoards = abBoard.getNextBoards();
        if (abBoard.getCurrentPlayer() == MAX_PLAYER) {
            for (AlphaBetaBoard child : nextBoards) {
                alpha = Math.max(alpha, minimax(child, depth -1, alpha, beta));
                if (alpha < beta)
                    break;
            }
            return alpha;
        }
        else { // min player
            for (AlphaBetaBoard child : nextBoards) {
                beta = Math.min(beta, minimax(child, depth -1, alpha, beta));
                if (alpha > beta)
                    break;
            }
            return beta;
        }

    }

}
