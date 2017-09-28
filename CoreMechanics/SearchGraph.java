/* ------------------------------------------------
  * 8 TIle Puzzle Game
  *
  * Class: CS 342, Fall 2016
  * System: Windows 10, Intellij
  *
  * -------------------------------------------------
  */

//package file is in
package CoreMechanics;


//import statements
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;


//searchgraph class
public class SearchGraph {
    //instance variables
    private Node root;
    private Node currentNode;
    private PriorityQueue<Node> children;
    private HashMap<Board, Node> seenChildren;
    private HashMap<Board, Node> touchedStates;

    /*
    *   Function: constructor
    *   Parameters: board
    *   Return: none
    */
    public SearchGraph(Board board) {
        //get heuristic value
        int heuristic = calculateHeuristic(board.getBoard());

        //create root node
        root = new Node(board, null, heuristic);
        //set current node equal to root
        currentNode = root;

        //create new hashmap for boards we've already been to
        touchedStates = new HashMap<>();
        //create hashmap for children that already exist
        seenChildren = new HashMap<>();
        //add current board to touchedStates
        touchedStates.put(board, root);

        //create new priorityqueue for children with custom comparator
        children = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node v1, Node v2) {
                return v1.getHeuristicValue() - v2.getHeuristicValue();
            }
        });
    }

    /*
    *   Function: Calculate the heuristic value
    *   Parameters: 2d array to calculate
    *   Return: the heuristic value
    */
    public int calculateHeuristic(int[][] board) {
        int heuristic = 0;
        for(int i = 0; i < Constants.rowSize; i++)
            for(int j = 0; j < Constants.colSize; j++)
                switch(board[i][j]) {
                    case 0:
                        heuristic += Math.abs(i - Constants.boardLocationSpaceRow) + Math.abs(j - Constants.boardLocationSpaceCol);
                        break;
                    case 1:
                        heuristic += Math.abs(i - Constants.boardLocation1Row) + Math.abs(j - Constants.boardLocation1Col);
                        break;
                    case 2:
                        heuristic += Math.abs(i - Constants.boardLocation2Row) + Math.abs(j - Constants.boardLocation2Col);
                        break;
                    case 3:
                        heuristic += Math.abs(i - Constants.boardLocation3Row) + Math.abs(j - Constants.boardLocation3Col);
                        break;
                    case 4:
                        heuristic += Math.abs(i - Constants.boardLocation4Row) + Math.abs(j - Constants.boardLocation4Col);
                        break;
                    case 5:
                        heuristic += Math.abs(i - Constants.boardLocation5Row) + Math.abs(j - Constants.boardLocation5Col);
                        break;
                    case 6:
                        heuristic += Math.abs(i - Constants.boardLocation6Row) + Math.abs(j - Constants.boardLocation6Col);
                        break;
                    case 7:
                        heuristic += Math.abs(i - Constants.boardLocation7Row) + Math.abs(j - Constants.boardLocation7Col);
                        break;
                    case 8:
                        heuristic += Math.abs(i - Constants.boardLocation8Row) + Math.abs(j - Constants.boardLocation8Col);
                        break;
                    default: break;
                }

        return heuristic;
    }

    /*
    *   Function: Solve function to solve board
    *   Parameters: an array of size 2 to return stats
    *   Return: a priority queue with a path
    */
    public void solve(int[] retArray, ArrayList<Node> path) {

        //create priority queue to store solution path
        PriorityQueue<Node> retPq = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.getHeuristicValue() - o2.getHeuristicValue();
            }
        });

        //set number of moves to 0
        int numMoves = 1;

        //set currentNode parent to null
        currentNode.setParent(null);
        //while the queue isn't empty and not solved
        while(!children.isEmpty() && currentNode.getHeuristicValue() != 0) {
            //get lowest node in children queue
            Node lowest = children.poll();
            currentNode = lowest;

            //add to path
            retPq.add(currentNode);
            currentNode.getBoardState().computeHeuristicValue();
            //add to touched states
            touchedStates.put(currentNode.getBoardState(), currentNode);
            //compute the children
            currentNode.getBoardState().computeChildren();
            //increase number of moves
            numMoves++;
        }

        //if queue became empty then set nonsolvable
        if(children.isEmpty() && currentNode.getHeuristicValue() != 0) {
            retArray[0] = 0;
            retArray[1] = numMoves;
            path.add(retPq.poll());
        }
        //else set solvable
        else {
            retArray[0] = 1;
            retArray[1] = printPath(retPq, path);
        }
    }

    /*
    *   Function: Recursively print path
    *   Parameters: start Node, and number of moves
    *   Return: none; print to console
    */
    private void _printPath(Node start, ArrayList<Node> path) {
        if(start == null)
            return;
        _printPath(start.getParent(), path);
        path.add(start);
    }

    /*
    *   Function: return the number of moves
    *   Parameters: starting node and number of moves
    *   Return: number of moves
    */
    private int _countNumMoves(Node start, int moves) {
        if(start == null)
            return moves;
        return _countNumMoves(start.getParent(), moves+1);
    }

    /*
    *   Function: function to print path
    *   Parameters: priorityqueue with path and number of moves
    *   Return: none; print to console
    */
    private int printPath(PriorityQueue<Node> p, ArrayList<Node> path) {
        Node start = p.poll();
        Node startCopy = start;
        int numMoves = _countNumMoves(startCopy, 0);
        _printPath(start, path);
        return numMoves;
    }

    /*
    *   Function: Check if board has been seen
    *   Parameters: board
    *   Return: true if seen; else false
    */
    public boolean checkTouched(Board board) {
        if(touchedStates.get(board) != null)
            return true;
        return false;
    }

    /*
    *   Function: add children nodes
    *   Parameters: board
    *   Return: none
    */
    public void addChildren(Board board) {
        //check if child was seen already
        Node exist = touchedStates.get(board);
        if(exist != null)
            return;
        exist = seenChildren.get(board);
        if(exist != null)
            return;

        //if not add the child
        int heuristic = board.getBoardHeuristic();
        Node child = new Node(board, currentNode, heuristic);

        currentNode.addChild(child);
        children.add(child);
        seenChildren.put(board, child);
    }

    /*
    *   Function: move the current node to a child node
    *   Parameters: a board
    *   Return: none; throw if issue
    */
    public void moveCurrentNode(Board board) {
        //if child doesn't exist then throw error
        this.currentNode = currentNode.findChild(board);
        if(currentNode == null) {
            throw new NullPointerException();
        }
        //else remove from queue and hashmap of children seen and add to touched States
        children.remove(currentNode);
        seenChildren.remove(board);
        touchedStates.put(currentNode.getBoardState(), currentNode);
    }
}
