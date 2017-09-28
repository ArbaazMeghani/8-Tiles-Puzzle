/* ------------------------------------------------
  * 8 TIle Puzzle Game
  *
  * Class: CS 342, Fall 2016
  * System: Windows 10, Intellij
  *
  * Description: This is a tile class that creates the tile on the screen.
  * -------------------------------------------------
  */

//import statements
import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import CoreMechanics.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;

//tile class to hold tiles
public class Tile {

    //instance variables to hold each tile info
    private int xPosition;
    private int yPosition;
    private int tileNumber;
    private StackPane container;
    private Rectangle rectTile;
    private static Tile spaceTile;
    private static ArrayList<Tile> allTiles;

    /*
    * Function: Constructor
    * Parameters: tile position, board, boardContainer, label
    * Return: none
    */
    public Tile(int xPosition, int yPosition, Board board, Pane boardContainer, Text labels) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.tileNumber = -1;
        initializeRectangle(board, boardContainer, labels);
    }

    /*
    * Function: init rectangle
    * Parameters: board, boardContainer, label
    * Return: none
    */
    private void initializeRectangle(Board board, Pane boardContainer, Text label) {
        //create rectangle
        rectTile = new Rectangle(Constants.tileWidth*xPosition,
                Constants.tileHeight*yPosition,
                Constants.tileWidth, Constants.tileHeight);
        rectTile.setFill(Paint.valueOf("WHITE"));
        rectTile.setStroke(Paint.valueOf("BLACK"));
        rectTile.setStrokeWidth(1);

        //add mouse functionality
        addMouseEventClick(board, boardContainer, label);
        addMouseEventEnter();
        addMouseEventExit();
    }

    /*
    * Function: add event that user clicks rect
    * Parameters: board, boardContainer, label
    * Return: none
    */
    private void addMouseEventClick(Board board, Pane boardContainer, Text label) {
        rectTile.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //if game over show alert
                if(board.getNumMoves() == -1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Game Over");
                    alert.setHeaderText(null);
                    alert.setContentText("Game has Ended");
                    alert.getButtonTypes().clear();
                    alert.getButtonTypes().add(ButtonType.CLOSE);

                    alert.showAndWait();
                    return;
                }
                //try to move tile
                else if (board.moveTile(tileNumber)) {
                    //create animation
                    SequentialTransition sequentialTransition = new SequentialTransition();
                    sequentialTransition.getChildren().clear();
                    sequentialTransition.getChildren().add(moveToSpaceTile(boardContainer));
                    sequentialTransition.play();

                    //update labels
                    updateLabels(label, board);
                }
                //check if game ending conditions, if true show alert and set game over
                if(board.checkVictoryCondition()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Game Over");
                    alert.setHeaderText(null);
                    alert.setContentText("You Win! Congratulations.");
                    alert.getButtonTypes().clear();
                    alert.getButtonTypes().add(ButtonType.CLOSE);

                    alert.showAndWait();
                    board.setNumMoves(-1);
                }
                else if(board.checkMovesLeft()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Game Over");
                    alert.setHeaderText(null);
                    alert.setContentText("No More Moves Left");
                    alert.getButtonTypes().clear();
                    alert.getButtonTypes().add(ButtonType.CLOSE);

                    alert.showAndWait();
                    board.setNumMoves(-1);
                }
            }
        });
    }

    /*
    * Function: Mouse enter rect event
    * Parameters: none
    * Return: none
    */
    private void addMouseEventEnter() {
        rectTile.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(tileNumber != 0)
                    rectTile.setFill(Paint.valueOf("CYAN"));
            }
        });
    }

    /*
    * Function: Mouse exit rect event
    * Parameters: none
    * Return: none
    */
    private void addMouseEventExit() {
        rectTile.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(tileNumber != 0)
                    rectTile.setFill(Paint.valueOf("WHITE"));
            }
        });
    }

    /*
    * Function: change position of rectanlge
    * Parameters: none
    * Return: none
    */
    private void changeRectPositions() {
        double xPos = spaceTile.getRectTile().getX();
        double yPos = spaceTile.getRectTile().getY();

        spaceTile.getRectTile().setX(rectTile.getX());
        spaceTile.getRectTile().setY(rectTile.getY());

        rectTile.setX(xPos);
        rectTile.setY(yPos);

        //System.out.println(rectTile.getX() + " " + rectTile.getY());
    }

    /*
    * Function: change coordinates of tile
    * Parameters: none
    * Return: none
    */
    private void changePositions() {
        int xPos = spaceTile.getxPosition();
        int yPos = spaceTile.getyPosition();
        spaceTile.setxPosition(xPosition);
        spaceTile.setyPosition(yPosition);

        xPosition = xPos;
        yPosition = yPos;

        changeRectPositions();
    }

    /*
    * Function: Move the tile
    * Parameters: boardContainer
    * Return: the transition
    */
    public PauseTransition moveToSpaceTile(Pane boardContainer) {
        //change positions
        changePositions();

        //create transition
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));

        //get list of stackpanes
        ObservableList<javafx.scene.Node> stackPanes = boardContainer.getChildren();

        //loop through find space and the tile to move
        for(javafx.scene.Node n: stackPanes) {
            StackPane s = (StackPane)n;
            if(s.getChildren().contains(spaceTile.getRectTile())) {
                //update location for space
                s.setLayoutX(spaceTile.getRectTile().getX());
                s.setLayoutY(spaceTile.getRectTile().getY());
            }
            else if(s.getChildren().contains(rectTile)) {

                //create transition for numbered tile
                TranslateTransition transition = new TranslateTransition(Duration.millis(300));
                transition.setByX(rectTile.getX() - spaceTile.getRectTile().getX());
                transition.setByY(rectTile.getY() - spaceTile.getRectTile().getY());
                transition.setNode(s);
                pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        transition.play();
                    }
                });
                //sequentialTransition.getChildren().add(transition);
                //s.setLayoutX(rectTile.getX());
                //s.setLayoutY(rectTile.getY());
                //System.out.println(rectTile.getX() + " " + rectTile.getY());
            }
        }

        //return transition
        return pauseTransition;
    }

    /*
    * Function: Solve function
    * Parameters: board, boardContainer, solved path
    * Return: none
    */
    private void solve(Board board, Pane boardContainer, ArrayList<Node> path) {

        //get current state and next state
        Node curr = path.get(0);
        Node nxt = path.get(1);

        //get position of space in current place
        int yPosOfSpace = curr.getBoardState().computeTileCol(Constants.boardSpace);
        int xPosOfSpace = curr.getBoardState().computeTileRow(Constants.boardSpace);

        //in next board state find what is in that space location from current board
        int tileNumberToMove = nxt.getBoardState().getBoard()[xPosOfSpace][yPosOfSpace];
        ArrayList<Tile> tiles = new Tile(0, 0, null, null, null).getAllTiles();

        //move that tile.
        for (Tile t : tiles) {
            if (t.getTileNumber() == tileNumberToMove)
                t.moveToSpaceTile(boardContainer).play();
        }
    }

    /*
    * Function: solve animation
    * Parameters: board, boardContainer, path, labels
    * Return: none
    */
    public void solveAnimation(Board board, Pane boardContainer, ArrayList<Node> path, Text label) {
        //create timeline to show solved animation, new state every 300ms
        Timeline solver = new Timeline(new KeyFrame(Duration.millis(300), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                solve(board, boardContainer, path);
                updateLabels(label, path.get(1).getBoardState());
                path.remove(0);
            }
        }));

        //after finished set game to finished
        solver.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                board.setNumMoves(-1);
            }
        });
        solver.setCycleCount(board.getNumMoves()-1);
        solver.play();
    }

    /*
    * Function: update labels
    * Parameters: label, board
    * Return: none
    */
    public void updateLabels(Text label, Board board) {
        label.setText("HValue: " + board.getBoardHeuristic()
        + " numMoves: " + board.getNumMoves());
    }


    //from this point on everything is setters and getters for instance variables
    public ArrayList<Tile> getAllTiles() { return allTiles; }

    public void setAllTiles(ArrayList<Tile> allTiles) { this.allTiles = allTiles; }

    public StackPane getContainer() { return container; }

    public void setContainer(StackPane container) { this.container = container; }

    public void setSpaceTile(Tile spaceTile) { this.spaceTile = spaceTile; }

    public Tile getSpaceTile() { return spaceTile; }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getTileNumber() {
        return tileNumber;
    }

    public void setTileNumber(int tileNumber) {
        this.tileNumber = tileNumber;
    }

    public Rectangle getRectTile() {
        return rectTile;
    }

    public void setRectTile(Rectangle rectTile) {
        this.rectTile = rectTile;
    }
}
