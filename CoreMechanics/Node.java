/* ------------------------------------------------
  * 8 TIle Puzzle Game
  *
  * Class: CS 342, Fall 2016
  * System: Windows 10, Intellij
  *
  * -------------------------------------------------
  */


//package this file is in
package CoreMechanics;

//import statement
import java.util.ArrayList;


//node class
public class Node {
    //instance variables
    private Board boardState;
    private Node parent;
    private int heuristicValue;
    private ArrayList<Node> childStates;

    /*
    *   Function: Constructor
    *   Parameters: board, parent node, heuristic value
    *   Return:
    */
    public Node(Board board, Node parent, int heuristicValue) {
        //set instance variables and create copy of board and new array list for children
        this.parent = parent;
        this.heuristicValue = heuristicValue;
        boardState = new Board(board);
        childStates = new ArrayList<>();

    }

    /*
    *   Function: add a child
    *   Parameters: child to be added
    *   Return: none
    */
    public void addChild(Node child) {
        childStates.add(child);
    }

    //return board
    public Board getBoardState() {
        return boardState;
    }

    //return parent
    public Node getParent() { return parent; }

    //set parent
    public void setParent(Node parent) { this.parent = parent; }

    //return heuristic value
    public int getHeuristicValue() {
        return heuristicValue;
    }

    /*
    *   Function: find a specific child
    *   Parameters: board to compare with
    *   Return: the node of specific child
    */
    public Node findChild(Board board) {
        for(Node c: childStates)
            if(c.equals(board.getBoard()))
                return c;
        return null;
    }

    /*
    *   Function: find the child with minimum heuristic value
    *   Parameters: none
    *   Return: the node with min or null if no children
    */
    public Node findMinChild() {
        if(childStates.size() == 0)
            return null;
        Node minV = childStates.get(0);
        for(Node v: childStates)
            if(v.getHeuristicValue() < minV.getHeuristicValue())
                minV = v;
        return minV;
    }

    /*
    *   Function: check if node has specific board
    *   Parameters: the board
    *   Return: true if equal; else false
    */
    public boolean equals(int[][] board) {
        int[][] currentBoard = boardState.getBoard();
        for(int i = 0; i < Constants.rowSize; i++)
            for(int j = 0; j < Constants.colSize; j++)
                if(currentBoard[i][j] != board[i][j])
                    return false;
        return true;
    }


    /*
    *   Function: equals function
    *   Parameters: object
    *   Return: true if nodes are equal; else false
    */
    @Override
    public boolean equals(Object o) {
        Node v = (Node)o;
        return this.boardState.equals(v.boardState);
    }
}
