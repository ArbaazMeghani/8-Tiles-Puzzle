/* ------------------------------------------------
  * 8 TIle Puzzle Game
  *
  * Class: CS 342, Fall 2016
  * System: Windows 10, Intellij
  *
  * Description: This is the 8 tiles puzzle gui.
  *     It is made using javafx.
  * -------------------------------------------------
  */

//import statements
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import CoreMechanics.*;

import java.util.ArrayList;

import static java.lang.System.exit;

//Driver class inherits application for javafx
public class TilesDriver extends Application {

    //keep track of board, all the tiles, and the primary stage.
    Board board;
    ArrayList<Tile> tiles;
    Stage primaryStage;

    /*
    * Function: This is the start function that runs the application, it is like main.
    * Parameters: The stage to display the application on
    * Return: none; void
    */
    @Override
    public void start(Stage primaryStage) {
        //print headers
        System.out.println("Net Id: amegha3\n" +
                "Class CS342, Fall2016\n" +
                "Program #3: 8 Tile Puzzle Game\n\n");

        //set the stage
        this.primaryStage = primaryStage;

        //set title and make sure resizable is false
        primaryStage.setTitle("8 Tiles Puzzle");
        primaryStage.setResizable(false);

        //get the starting scene and put it on the stage and show it
        Scene startScene = startScreen();
        primaryStage.setScene(startScene);
        primaryStage.show();
    }


    /*
    * Function: start scene, basically the main menu
    * Parameters: none
    * Return: the scene
    */
    public Scene startScreen() {
        //create border pane to organize screen
        BorderPane startBorderPane = new BorderPane();
        //create border pane to hold title
        BorderPane title = new BorderPane();

        //set the title text
        title.setPrefSize(100,100);
        title.setCenter(new Text("Welcome To\n 8 Tiles Puzzle"));

        //create a verticle box to hold buttons
        VBox startButtons = new VBox();

        //create play button that creates random board
        Button playBtn = new Button("Play");
        playBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                board = new Board();
                Scene mainGameScene = setupGame();
                primaryStage.setScene(mainGameScene);
            }
        });
        Button setBoardBtn = new Button("Set Board");

        //create button that lets user set a custom board
        setBoardBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Scene setBoardScene = setupBoard();
                primaryStage.setScene(setBoardScene);
            }
        });

        //add the buttons to the vertical box
        startButtons.getChildren().add(playBtn);
        startButtons.getChildren().add(setBoardBtn);
        startButtons.getChildren().add(exitBtn());

        //add the title and the buttons to screen
        startBorderPane.setTop(title);
        startBorderPane.setCenter(startButtons);

        //center the buttons
        startButtons.setTranslateX(Constants.tileWidth*Constants.rowSize/2 - 25);

        //create scene with the main border pane
        Scene startScene = new Scene(startBorderPane,
                Constants.tileWidth*Constants.rowSize,
                Constants.tileHeight*Constants.colSize + 25);

        //return the scene
        return startScene;
    }

    /*
    * Function: Create an exit button
    * Parameters: none
    * Return: return an exit button
    */
    public Button exitBtn() {
        Button btnExit = new Button("Exit");
        btnExit.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                exit(1);
            }
        });

        return btnExit;
    }

    /*
    * Function: setup custom board
    * Parameters: none
    * Return: Scene with custom board
    */
    public Scene setupBoard() {

        //create panes to organize screen
        Pane boardPane = new Pane();
        BorderPane screenPane = new BorderPane();
        Pane boardContainer = new Pane();

        //set preferred size for the board container
        boardContainer.setPrefSize(Constants.tileWidth*Constants.rowSize,
                Constants.tileHeight*Constants.colSize);

        //add board container to boardpane
        boardPane.getChildren().add(boardContainer);
        //add it to screen
        screenPane.setCenter(boardPane);

        //create horizontal box to hold buttons at top
        HBox topPane = new HBox();

        //create button to go back to the main menu
        Button mainMenu = new Button("Main Menu");
        mainMenu.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Scene startScreen = startScreen();
                primaryStage.setScene(startScreen);
            }
        });

        //add buttons and also an exit button
        topPane.getChildren().add(mainMenu);
        topPane.getChildren().add(exitBtn());

        //add it to screen
        screenPane.setTop(topPane);

        //array to make sure no repeats and check if everything is set
        final char[][] customBoard = new char[Constants.rowSize][Constants.colSize];
        //array to increment number
        final int[] tileNum = new int[1];
        tileNum[0] = 0;

        //init array
        for(int i = 0; i < Constants.rowSize; i++)
            for(int j = 0; j < Constants.colSize; j++)
                customBoard[i][j] = 's';

        //setup the tiles
        setupCustomBoardTiles(customBoard, tileNum, boardContainer);

        //create the scene
        Scene scene = new Scene(screenPane,
                Constants.tileWidth*Constants.rowSize,
                Constants.tileHeight*Constants.colSize + 25);

        //return the scene
        return scene;
    }

    /*
    * Function: Setup the board tiles for a custom board
    * Parameters: final array for error checking and validation, array to keep track of number, and the pane to add tiles to.
    * Return: none; updates to screen
    */
    public void setupCustomBoardTiles(final char[][] customBoard, final int[] tileNum, Pane boardContainer) {

        //create 9 rectangles and add event handling to them
        for(int i = 0; i < Constants.rowSize; i++) {
            for (int j = 0; j < Constants.colSize; j++) {
                //use stackpane for adding text
                StackPane s = new StackPane();
                //create rectangle
                Rectangle r = new Rectangle(Constants.tileWidth * i,
                        Constants.tileHeight * j,
                        Constants.tileWidth, Constants.tileHeight);

                //set rectangle coloring
                r.setFill(Paint.valueOf("WHITE"));
                r.setStroke(Paint.valueOf("BLACK"));
                r.setStrokeWidth(1);

                //add mouse input handling
                r.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        //get the pos clicked
                        int xPos = (int) event.getX() / Constants.tileWidth;
                        int yPos = (int) event.getY() / Constants.tileHeight;

                        //if pos has value already, return
                        if (customBoard[xPos][yPos] != 's')
                            return;

                        //otherwise set the value
                        customBoard[xPos][yPos] = String.valueOf(tileNum[0]).charAt(0);

                        //get the rectangle
                        Rectangle thisRect = (Rectangle) event.getSource();

                        //find the rectangle in the list, once found add text to it
                        ObservableList<javafx.scene.Node> stackPanes = boardContainer.getChildren();
                        for (javafx.scene.Node n : stackPanes) {
                            StackPane s = (StackPane) n;
                            if (s.getChildren().contains(thisRect))
                                s.getChildren().add(new Text(String.valueOf(customBoard[xPos][yPos])));
                        }
                        //increment tile number
                        tileNum[0]++;

                        //create string to convert array to string for custom board
                        String boardString = new String();

                        //convert array to string, if we find an 's' return since not all place initalized
                        for (int i = 0; i < Constants.rowSize; i++) {
                            for (int j = 0; j < Constants.colSize; j++) {
                                if (customBoard[i][j] == 's')
                                    return;
                                boardString += customBoard[j][i];
                            }
                        }

                        //create board with string
                        board = new Board(boardString);

                        //setup the game
                        Scene mainGame = setupGame();

                        //set the new scene
                        primaryStage.setScene(mainGame);
                    }
                });

                //add rectangle to stackpane
                s.getChildren().add(r);
                //set stackpane location
                s.setLayoutX(Constants.tileWidth * i);
                s.setLayoutY(Constants.tileHeight * j);

                //add to boardcontainer
                boardContainer.getChildren().add(s);
            }
        }
    }

    /*
    * Function: Setup the game
    * Parameters: none
    * Return: Scene
    */
    public Scene setupGame() {

        //create panes to hold main game
        Pane boardPane = new Pane();
        BorderPane screenPane = new BorderPane();
        Pane boardContainer = new Pane();

        //create array list to hold all tiles
        tiles = new ArrayList<>();


        //init container
        boardContainer.setPrefSize(Constants.tileWidth*Constants.rowSize,
                Constants.tileHeight*Constants.colSize);
        boardContainer.setBackground(new Background(new BackgroundFill(Paint.valueOf("WHITE"), null, null)));
        boardPane.getChildren().add(boardContainer);

        //add container
        screenPane.setCenter(boardPane);

        //create top pane with buttons and labels
        BorderPane topPane = new BorderPane();
        Rectangle labelRect= new Rectangle(0, 0, Constants.tileWidth*2, 25);
        labelRect.setFill(Paint.valueOf("WHITE"));
        StackPane labelHolder = new StackPane();
        Text label = new Text("HValue: " + String.valueOf(board.getBoardHeuristic())
        + " numMoves: " + String.valueOf(board.getNumMoves()));
        labelHolder.getChildren().addAll(labelRect, label);
        HBox hBox = createHBox(boardContainer, label);
        topPane.setLeft(hBox);
        topPane.setRight(labelHolder);

        //add top pane
        screenPane.setTop(topPane);

        //init board and tiles
        initializeBoard(boardContainer, label);
        initializeTilePane(boardContainer);

        //System.out.println(board);

        //set the array list
        tiles.get(0).setAllTiles(tiles);

        //create scene
        Scene scene = new Scene(screenPane,
                Constants.tileWidth*Constants.rowSize,
                Constants.tileHeight*Constants.colSize + 25,
                Color.WHITE);

        //return scene
        return scene;
    }

    /*
    * Function: Create hbox for buttons and text
    * Parameters: pane to add to and a text label
    * Return: hbox
    */
    public HBox createHBox(Pane boardContainer, Text label) {
        //create new hbox
        HBox hbox = new HBox();

        //create buttons
        Button solve = new Button("Solve");
        Button mainMenu = new Button("Main Menu");

        //add solve button functionality
        solve.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Tile t  = new Tile(0,0,null,null, null);

                //check if game ended
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
                //other wise solve it
                ArrayList<Node> path = new ArrayList<>();
                int numMoves = board.solve(path);
                board.setNumMoves(numMoves);

                //if unsolvable, show board and return
                if(numMoves >= 181440) {
                    t.updateLabels(label, path.get(0).getBoardState());
                    board.setNumMoves(-1);
                    ObservableList<javafx.scene.Node> stackPanes = boardContainer.getChildren();
                    for(javafx.scene.Node n: stackPanes) {
                        StackPane s = (StackPane)n;
                        if(s.getChildren().size() > 1) {
                            Text tileNum = (Text) s.getChildren().get(1);
                            int tileNumberToMove = Integer.parseInt(tileNum.getText());

                            int tileNumYPos = path.get(0).getBoardState().computeTileRow(tileNumberToMove);
                            int tileNumXPos = path.get(0).getBoardState().computeTileCol(tileNumberToMove);

                            s.setLayoutX(tileNumXPos * Constants.tileWidth);
                            s.setLayoutY(tileNumYPos * Constants.tileHeight);
                        }
                    }
                    return;
                }

                //otherwise it is solvable so play animation
                t.solveAnimation(board, boardContainer, path, label);
            }
        });
        //add main menu functionality
        mainMenu.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Scene startScene = startScreen();
                primaryStage.setScene(startScene);
            }
        });

        //add bittons
        hbox.getChildren().add(solve);
        hbox.getChildren().add(mainMenu);
        hbox.getChildren().add(exitBtn());

        //return hbox
        return hbox;
    }

    /*
    * Function: Create tiles and add to boardContainer
    * Parameters: boardContainer
    * Return: none
    */
    public void initializeTilePane(Pane boardContainer) {
        //get board
        int[][] board2D = board.getBoard();
        //make arraylist of stackpanes
        ArrayList<StackPane> stackpanes = new ArrayList<>();
        //loop and create tiles, add zero first so it is in the back,
        //if not zero add to arraylist
        for(int i = 0, k = 0; i < Constants.rowSize; i++) {
            for (int j = 0; j < Constants.colSize; j++, k++) {
                tiles.get(k).setTileNumber(board2D[j][i]);
                StackPane s = setupStackPane(tiles.get(k));
                tiles.get(k).setContainer(s);
                if (board2D[j][i] == 0) {
                    tiles.get(k).setSpaceTile(tiles.get(k));
                    boardContainer.getChildren().add(s);
                } else {
                    stackpanes.add(s);
                }
            }
        }

        //add the rest of tiles to container so they can be in front of space tile
        for(StackPane s: stackpanes)
            boardContainer.getChildren().add(s);
    }

    /*
    * Function: Setup stackpane
    * Parameters: tile
    * Return: StackPane
    */
    public StackPane setupStackPane(Tile tile) {
        //create new stackpane
        StackPane stackPane = new StackPane();

        //if not zero add text and rect
        if(tile.getTileNumber() != 0)
            stackPane.getChildren().addAll(tile.getRectTile(), new Text(String.valueOf(tile.getTileNumber())));
        //if zero add rect and set border to white
        else {
            stackPane.getChildren().addAll(tile.getRectTile());
            tile.getRectTile().setStroke(Paint.valueOf("WHITE"));
        }

        //set location
        stackPane.setLayoutX(tile.getRectTile().getX());
        stackPane.setLayoutY(tile.getRectTile().getY());

        //return stackpane
        return stackPane;
    }

    /*
    * Function: create tiles
    * Parameters: board container, and text label
    * Return: none
    */
    public void initializeBoard(Pane boardContainer, Text labels) {
        for(int i = 0, k = 0; i < Constants.rowSize; i++)
            for(int j = 0; j < Constants.colSize; j++, k++) {
                Tile tile = new Tile(i, j, board, boardContainer, labels);
                tiles.add(tile);
            }
    }

    //main doesn't do anything.
    public static void main(String[] args) {
        launch(args);
    }
}
