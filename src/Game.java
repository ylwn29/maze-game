/**
 * The {@code Game} is a driver
 */
import java.util.*;

public class Game {
  private final Maze maze;
  private boolean isGameOver;

  /**
   * Create a game.
   *
   * TODO: finish doc.
   * @param type type of maze. Supported maze see {@code TypeOfMaze}.
   * @param numOfRows the row size of the maze.
   * @param numOfCols the column size of the maze.
   * @param numOfRemainingWalls the remaining walls of the maze. For perfect maze, this is unused.
   * @return maze game.
   */
  public static Game createGame(TypeOfMaze type,
                                int numOfRows,
                                int numOfCols,
                                int numOfRemainingWalls,
                                Location startLocation,
                                Location goalLocation) {



    Maze maze = switch (type) {
      case PERFECT -> PerfectMaze.createPerfectMaze(numOfRows, numOfCols, startLocation, goalLocation);
//      case ROOM -> new RoomMaze(numOfRows, numOfCols, numOfRemainingWalls, startLocation, goalLocation);
//      case WRAP_ROOM -> new WrapRoomMaze(numOfRows, numOfCols, numOfRemainingWalls, startLocation, goalLocation);
      default -> throw new IllegalArgumentException("Unsupported type of maze specified.");
    };

    return new Game(maze);
  }

  // TODO: add doc.
  public static Game createGameWithRandom(TypeOfMaze type, int numOfRows, int numOfCols, int numOfRemainingWalls) {
    Location startLocation = generateRandLoc(numOfRows, numOfCols);
    Location goalLocation = generateRandLoc(numOfRows, numOfCols);
    return createGame(type, numOfRows, numOfCols, numOfRemainingWalls, startLocation, goalLocation);
  }

  /**
   * Convenient creation of game with perfect maze.
   *
   * TODO: finish doc.
   * @param numOfRows
   * @param numOfCols
   * @return
   */
  public static Game createPerfectGameWithRandom(int numOfRows, int numOfCols) {
    return createGameWithRandom(TypeOfMaze.PERFECT, numOfRows, numOfCols, -1);
  }

  private Game(Maze maze) {
    this.maze = maze;
    System.out.println(maze);
    this.isGameOver = false;
  }

  private static Location generateRandLoc(int numOfRows, int numOfCols) {
    Random randNum = new Random();
    int i = randNum.nextInt() % numOfRows;
    int j = randNum.nextInt() % numOfCols;
    int y = MazeHelper.idxIToLocationY(i);
    int x = MazeHelper.idxJToLocationX(j);
    return new Location(Math.abs(x), Math.abs(y));
  }


  public String controlPlayer() {
    Random r = new Random();
    List<Direction> dList = maze.produceNextPossibleMove(maze.getPlayerLocation());
    System.out.println("Possible Direction: " + dList);
    int idx = r.nextInt(dList.size());
    maze.trigger(dList.get(idx));
    this.isGameOver = maze.isGameOver();

    return "player moves to: "+ dList.get(idx) + "\n" + maze.printPlayerStatus();
  }

  public boolean isGameOver() {
    return isGameOver;
  }




}
