/* ------------------------------------------------
  * 8 TIle Puzzle Game
  *
  * Class: CS 342, Fall 2016
  * System: Windows 10, Intellij
  *
  * -------------------------------------------------
  */

//package this file belongs to
package CoreMechanics;

//import statements
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

import static java.lang.System.exit;


//board class
public class Board {

    //instance variables
    private int[][] board;
    private static int numMoves;
    private int boardHeuristic;
    private static SearchGraph boardPaths;

    /*
    *   Function: Default Constructor, random board
    *   Parameters: none;
    *   Return: none;
    */
    public Board() {
        //set num moves to 0
        numMoves = 1;

        //create new 2d array
        board = new int[Constants.rowSize][Constants.colSize];

        //create generator for random values seeded with time
        Random generator = new Random(System.currentTimeMillis());

        //create array of seen random values and set to 0
        int[] randomGenerator = new int[Constants.boardSize];
        for(int i = 0; i < Constants.boardSize; i++)
            randomGenerator[i] = 0;

        //generate board
        for(int i = 0; i < Constants.rowSize; i++) {
            for(int j = 0; j < Constants.colSize; j++) {
                int rand = Math.abs(generator.nextInt()) % 9;
                while(randomGenerator[rand] == 1)
                    rand = Math.abs(generator.nextInt()) % 9;
                board[i][j] = rand;
                randomGenerator[rand] = 1;
            }
        }

        boardPaths = new SearchGraph(this);
        boardHeuristic = boardPaths.calculateHeuristic(this.board);
    }

    /*
    *   Function: custom board constructor
    *   Parameters: String for initial board
    *   Return: none
    */
    public Board(String initialState) {
        //set number of moves to 0
        numMoves = 1;

        //create new 2d array
        board = new int[Constants.rowSize][Constants.colSize];

        //init array based on user input
        for(int i = 0, k = 0; i < Constants.rowSize; i++)
            for(int j = 0; j < Constants.colSize; j++, k++)
                board[i][j] = Character.getNumericValue(initialState.charAt(k));

        boardPaths = new SearchGraph(this);
        boardHeuristic = boardPaths.calculateHeuristic(board);

    }

    /*
    *   Function: Copy constructor
    *   Parameters: board
    *   Return: none
    */
    public Board(Board board) {
        //copy the board
        this.board = board.boardCopy();
        //get the board heuristic
        boardHeuristic = getBoardHeuristic();
    }

    /*
    *   Function: Check if tile is movable
    *   Parameters: tile number to move
    *   Return: true if movable; else false
    */
    private boolean isMovable(int tileNumber) {
        if(checkVictoryCondition() || checkMovesLeft()) {
            return false;
        }
        //get row and col of tile
        int tileRow = computeTileRow(tileNumber);
        int tileCol = computeTileCol(tileNumber);

        //get row and col of space
        int spaceRow = computeTileRow(Constants.boardSpace);
        int spaceCol = computeTileCol(Constants.boardSpace);

        //compute distances
        int rowDistance = Math.abs(tileRow-spaceRow);
        int colDistance = Math.abs(tileCol-spaceCol);

        //check if they are next to eachother
        if(rowDistance == 0 && colDistance == 1)
            return true;
        else if(rowDistance == 1 && colDistance == 0)
            return true;

        //otherwise return false
        return false;
    }

    /*
    *   Function: move tile
    *   Parameters: tile number to move
    *   Return: true if successful move; else false
    */
    public boolean moveTile(int tileNumber) {
        //check if tile is movable
        if(!isMovable(tileNumber))
            return false;

        //get tile row and col
        int tileRow = computeTileRow(tileNumber);
        int tileCol = computeTileCol(tileNumber);

        //get space row and col
        int spaceRow = computeTileRow(Constants.boardSpace);
        int spaceCol = computeTileCol(Constants.boardSpace);

        //flip the values
        board[tileRow][tileCol] = Constants.boardSpace;
        board[spaceRow][spaceCol] = tileNumber;


        //board state doesn't exist
        //increase number of moves
        numMoves++;


        //calculate current board heuristic
        boardHeuristic = boardPaths.calculateHeuristic(board);

        //return true
        return true;

    }

    /*
    *   Function: move child
    *   Parameters: tile number
    *   Return: true on successful move; else false
    */
    private boolean moveTileChild(int tileNumber) {

        //check if movable
        if(!isMovable(tileNumber))
            return false;


        //get tile row and col
        int tileRow = computeTileRow(tileNumber);
        int tileCol = computeTileCol(tileNumber);

        //get col row and col
        int spaceRow = computeTileRow(Constants.boardSpace);
        int spaceCol = computeTileCol(Constants.boardSpace);

        //flip the values
        board[tileRow][tileCol] = Constants.boardSpace;
        board[spaceRow][spaceCol] = tileNumber;

        //compute heuristic
        boardHeuristic = boardPaths.calculateHeuristic(board);

        //return true
        return true;

    }

    /*
    *   Function: check if board has reached victory state
    *   Parameters: none
    *   Return: true on victory; else false
    */
    public boolean checkVictoryCondition() {
        for(int i = 0; i < Constants.rowSize; i++)
            for(int j = 0; j < Constants.colSize; j++)
                if(board[i][j] != Constants.victoryBoard[i][j])
                    return false;
        return true;
    }

    public void computeHeuristicValue() {
        boardHeuristic = boardPaths.calculateHeuristic(getBoard());
    }

    public boolean checkMovesLeft() {
        return numMoves >= 181440;
    }

    public void setNumMoves(int numMoves) { this.numMoves = numMoves; }

    //return board
    public int[][] getBoard() {
        return board;
    }

    //return number of moves
    public int getNumMoves() {
        return numMoves;
    }

    //return heuristic value
    public int getBoardHeuristic() {
        return boardHeuristic;
    }

    /*
    *   Function: Automatically Solve The Board
    *   Parameters: none
    *   Return: none; output to console
    */
    public int solve(ArrayList<Node> path) {
        boardPaths = new SearchGraph(this);
        computeChildren();

        //array to return solve stats
        int[] solveStats = new int[2];
        //priority queue to store solved path
        boardPaths.solve(solveStats, path);

        return solveStats[1];
//        //if solvable print done and return
//        if(solveStats[0] == 1) {
//            System.out.println("Done");
//            return;
//        }
//
//        //otherwise not solvable, print move tries and best board
//        System.out.println("\nAll " + solveStats[1] +" moves have been tried. \n" +
//                "That puzzle is impossible to solve.  Best board found was: ");
//        Node current = solvePath.poll();
//
//        System.out.println(current.getBoardState());
//        System.out.println("Heuristic Value: " + current.getHeuristicValue());
    }

    /*
    *   Function: Compute the children of a board
    *   Parameters: none
    *   Return: none
    */
    public void computeChildren() {
        //create a copy of board
        Board copy = new Board(this);
        //try each move and if it succeeds add it as a child and make new copy
        for(int i = 1; i < Constants.boardSize; i++) {
            if(copy.moveTileChild(i)) {
                boardPaths.addChildren(copy);
                copy = new Board(this);
            }
        }
    }

    /*
    *   Function: create a copy of a board
    *   Parameters: none
    *   Return: 2d array
    */
    public int[][] boardCopy() {
        //declare new 2d array
        int[][] copy = new int[Constants.rowSize][Constants.colSize];
        //copy values
        for(int i = 0; i < Constants.rowSize; i++)
            for(int j = 0; j < Constants.colSize; j++)
                copy[i][j] = board[i][j];
        //return array
        return copy;
    }

    /*
    *   Function: Find the row of a tile
    *   Parameters: the tile number
    *   Return: the row of the tile
    */
    public int computeTileRow(int tileNumber) {
        for(int i = 0; i < Constants.rowSize; i++)
            for(int j = 0; j < Constants.colSize; j++)
                if(board[i][j] == tileNumber)
                    return i;
        return -1;
    }

    /*
    *   Function: Find the col of a tile
    *   Parameters: the tile number
    *   Return: the col of the tile
    */
    public int computeTileCol(int tileNumber) {
        for(int i = 0; i < Constants.rowSize; i++)
            for(int j = 0; j < Constants.colSize; j++)
                if(board[i][j] == tileNumber)
                    return j;
        return -1;
    }

    /*
    *   Function: get hashcode for hashmap
    *   Parameters: none
    *   Return: hash value
    */
    @Override
    public int hashCode() {
        return java.util.Arrays.deepHashCode(board);
    }

    /*
    *   Function: equals functions, check if boards are equal
    *   Parameters: an object
    *   Return: true if equal; else false
    */
    @Override
    public boolean equals(Object o) {
        Board boardObj = (Board)o;
        int[][] boardTiles = boardObj.getBoard();
        for(int i = 0; i < Constants.rowSize; i++)
            for(int j = 0; j < Constants.colSize; j++)
                if(this.board[i][j] != boardTiles[i][j])
                    return false;
        return true;
    }

    /*
    *   Function: board to string
    *   Parameters: none
    *   Return: board as a string
    */
    @Override
    public String toString() {
        String boardAsString = "  ";
        for (int i = 0; i < Constants.rowSize; i++) {
            for (int j = 0; j < Constants.colSize; j++) {
                if (board[i][j] == 0)
                    boardAsString += "   ";
                else
                    boardAsString += " " + board[i][j] + " ";
            }
            boardAsString += "\n  ";
        }

        return boardAsString;
    }
}
