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

//Constants final class
public final class Constants {
    //private default constructor
    private Constants() {

    }

    //board size
    public static final int boardSize = 9;

    //row and column sizes
    public static final int rowSize = 3;
    public static final int colSize = 3;


    //victory board
    public static final int[][] victoryBoard = { {1, 2, 3},
                                                    {4, 5, 6},
                                                        {7, 8, 0}};

    //define what a space is
    public static final int boardSpace = 0;

    //location of each value for victory
    public static final int boardLocation1Row = 0;
    public static final int boardLocation1Col = 0;

    public static final int boardLocation2Row = 0;
    public static final int boardLocation2Col = 1;

    public static final int boardLocation3Row = 0;
    public static final int boardLocation3Col = 2;

    public static final int boardLocation4Row = 1;
    public static final int boardLocation4Col = 0;

    public static final int boardLocation5Row = 1;
    public static final int boardLocation5Col = 1;

    public static final int boardLocation6Row = 1;
    public static final int boardLocation6Col = 2;

    public static final int boardLocation7Row = 2;
    public static final int boardLocation7Col = 0;

    public static final int boardLocation8Row = 2;
    public static final int boardLocation8Col = 1;

    public static final int boardLocationSpaceRow = 2;
    public static final int boardLocationSpaceCol = 2;

    public static final int tileWidth = 145;
    public static final int tileHeight = 175;

}
