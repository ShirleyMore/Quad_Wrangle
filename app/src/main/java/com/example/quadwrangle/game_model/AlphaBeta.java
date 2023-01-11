package com.example.quadwrangle.game_model;

import java.util.List;

public class AlphaBeta {
    static final int MAX_PLAYER = -1; // the number of the MAX player // aka the white player

    // initial values of alpha and beta
    final static int MAX = Integer.MAX_VALUE;
    static final int MIN = Integer.MIN_VALUE;

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
        //int winner = abBoard.winBoard(); // checks if there is a winner and if the game is over
        //if (winner != 0) {
         //   return Integer.MAX_VALUE * winner;
        //}
        if (abBoard.getPlaced(1) + abBoard.getPlaced(-1) == 49) {
            abBoard.setVal(0);
            return 0; // todo: fix
        }
        if (depth == 0) {
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
/*
    static int minimax(AlphaBetaBoard abBoard, int depth, int alpha, int beta) {
        // Terminating condition. i.e
        // leaf node is reached
        if (depth == 0) {
            return abBoard.getVal(); // return base value
        }
        if (abBoard.pl1squares + abBoard.pl2squares == 49) {
            abBoard.setVal(0);
            return 0; //todo: fix the val because 0 can happen otherwise - > use the is game over func
        }
        // getting the next best boards for the current player (min or max)
        List<AlphaBetaBoard> nextBoards = abBoard.IterateNextBoards();
        if (abBoard.getCurrentPlayer() == MAX_PLAYER) {
            // Recur for left and right children
            for (AlphaBetaBoard child : nextBoards) {
                int val = minimax(child, depth - 1, alpha, beta);
                alpha = Math.max(alpha, val);
                // Alpha Beta Pruning
                if (beta <= alpha)
                    break;
            }

        } else {
            // Recur for left and right children
            for (AlphaBetaBoard child : nextBoards) {
                int val = minimax(child, depth - 1, alpha, beta);
                beta = Math.min(beta, val);
                // Alpha Beta Pruning
                if (beta <= alpha)
                    break;
            }
        }
        return beta;
    }*/
}
