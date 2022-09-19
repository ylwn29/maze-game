import java.util.*;

public class PerfectMaze implements Maze {
  private final List<List<Cell>> grid;
  private final Player player;
  private final Location goalLocation;
  private final Location startLocation;
  private boolean isGameOver;


  private PerfectMaze(List<List<Cell>> grid, Player player, Location startLocation, Location goalLocation) {
    this.grid = grid;
    this.player = player;
    this.startLocation = startLocation;
    this.goalLocation = goalLocation;
    this.isGameOver = false;
  }

  public static PerfectMaze createPerfectMaze(int numOfRows,
                                           int numOfCols,
                                           Location startLocation,
                                           Location goalLocation) {
    if (numOfRows < 0 || numOfCols < 0) {
      throw new IllegalArgumentException("Rows and Cols must be positive!");
    }

    if (startAndGoalNotInsideGrid(startLocation, numOfRows, numOfCols)) {
      throw new IllegalArgumentException("Start location is not inside the maze grid.");
    }

    if (startAndGoalNotInsideGrid(goalLocation, numOfRows, numOfCols)) {
      throw new IllegalArgumentException("Goal location is not inside the maze grid.");
    }

    // Create grid of maze.
    int numOfCells = numOfRows * numOfCols;
    List<List<Cell>> grid = generateGrid(numOfRows, numOfCols, numOfCells);
    // Create a player.
    Location playerLocation = new Location(startLocation.getX(), startLocation.getY());
    Player player = new PlayerImpl(playerLocation);
    updatePlayerGoldCollection(player, grid);


    return new PerfectMaze(grid, player, startLocation, goalLocation);
  }

  private static boolean startAndGoalNotInsideGrid(Location l, int numOfRows, int numOfCols) {
    int i = MazeHelper.locationToIdxI(l);
    int j = MazeHelper.locationToIdxJ(l);

    return i < 0 || i > (numOfRows - 1) || j < 0 || j > (numOfCols - 1);
  }

  // Using Kruskal Algorithm to generate grid.
  private static List<List<Cell>> generateGrid(int numOfRows, int numOfCols, int numOfCells) {
    // Initialize grid, each cell is isolated
    List<List<Cell>> grid = initializeGrid(numOfRows, numOfCols);
    int[] parent = makeSet(numOfCells);
    int[][] adjacentMatrix = generateAdjacentMatrix(numOfRows, numOfCols, numOfCells);
    List<Edge> edgeList = generateEdgeList(numOfCells, adjacentMatrix);
    // Gradually remove walls and union connected Cell.
    int numOfTotalWalls = calculateTotalWalls(numOfRows, numOfCols);
    int numOfRemainingWalls = calculateMaxRemainingWalls(numOfTotalWalls, numOfRows, numOfCols);
    int count = numOfTotalWalls;
    Random randNum = new Random();

    while (count != numOfRemainingWalls) {
      int randEdgeIdx = randNum.nextInt(edgeList.size());
      int sourceID = edgeList.get(randEdgeIdx).getSource();
      int destID = edgeList.get(randEdgeIdx).getDest();
      // If tear down the wall, decrement the count.
      if(tearDownWall(sourceID, destID, numOfCells, parent, grid)) {
        count--;
      }
      // Once the edge is processed, it can be either tore down or saved, do not need to process again.
      edgeList.remove(randEdgeIdx);
    }

    Set<Integer> itemsSet = new HashSet<>();
    // Randomly select Cell to place gold coin
    int numOfGoldCoin = MazeHelper.getXPercentageInInt(numOfCells, Parameters.GOLD_COIN_PERCENT);
    placeItems(TypeOfItem.GOLD_COIN, numOfGoldCoin, itemsSet, numOfCells, numOfCols, grid);

    // Randomly select Cell to place thief
    int numOfThief = MazeHelper.getXPercentageInInt(numOfCells, Parameters.THIEF_PERCENT);
    placeItems(TypeOfItem.THIEF, numOfThief, itemsSet, numOfCells, numOfCols, grid);

    return grid;
  }

  private static List<List<Cell>> initializeGrid(int numOfRows, int numOfCols) {
    List<List<Cell>> grid = new ArrayList<>();
    for(int i = 0; i < numOfRows; i++){
      List<Cell> gridInRow = new ArrayList<>();
      for(int j = 0; j < numOfCols; j++){
        // ID = i * numOfCols + j, range from 0 to numOfCells - 1; i = ID / numOfCols; j = ID % numOfCols
        int ID = MazeHelper.idxToID(i, j, numOfCols);
        Cell c = new CellImpl(ID, numOfCols);
        gridInRow.add(c);
      }
      grid.add(gridInRow);
    }
    return grid;
  }

  private static int[][] generateAdjacentMatrix(int numOfRows, int numOfCols, int numOfCells) {
    int[][] adjacentMatrix = new int[numOfCells][numOfCells];
    for(int i = 0; i < numOfRows; i++) {
      for(int j = 0; j < numOfCols; j++) {
        int ID = MazeHelper.idxToID(i, j, numOfCols);
        if(j != numOfCols - 1) {
          int IDEast = MazeHelper.idxToID(i, j + 1, numOfCols);
          adjacentMatrix[ID][IDEast] = 1;
          adjacentMatrix[IDEast][ID] = 1;
        }
        if(i != numOfRows - 1) {
          int IDSouth = MazeHelper.idxToID(i + 1, j, numOfCols);
          adjacentMatrix[ID][IDSouth] = 1;
          adjacentMatrix[IDSouth][ID] = 1;
        }
      }
    }
    return adjacentMatrix;
  }

  static class Edge {
    private final int source;
    private final int dest;

    public Edge(int source, int dest) {
      this.source = source;
      this.dest = dest;
    }

    public int getSource() {
      return this.source;
    }

    public int getDest() {
      return this.dest;
    }
  }

  private static List<Edge> generateEdgeList(int numOfCells, int[][] adjacentMatrix) {
    List<Edge> edgeList = new ArrayList<>();

    // i, j is Cell ID
    for(int i = 0; i < numOfCells - 1; i++) {
      for(int j = i + 1; j < numOfCells; j++) {
        if(adjacentMatrix[i][j] == 1) {
          Edge e = new Edge(i, j);
          edgeList.add(e);
        }
      }
    }

    return edgeList;
  }

  private static boolean tearDownWall(int sourceID, int destID, int numOfCells, int[] parent, List<List<Cell>> grid) {
    boolean success = false;

    // If sourceID and destID are in different set, then the wall can be torn down, and union these two sets.
    // Otherwise, the two cell has already been
    if(findSet(sourceID, parent) != findSet(destID, parent)) {
      success = true;
      unionSet(sourceID, destID, parent, numOfCells);
      // tear down the wall between sourceID and destID
      updateGrid(sourceID, destID, grid);
    }

    return success;
  }

  private static int[] makeSet(int numOfCells) {
    int[] parent = new int[numOfCells];
    for(int k = 0; k < numOfCells; k++) {
      parent[k] = -1;
    }
    return parent;
  }

  private static int findSet(int ID, int[] parent) {
    while(parent[ID] >= 0) {
      ID = parent[ID];
    }

    return ID;
  }

  private static void unionSet(int sourceID, int destID, int[] parent, int numOfCells) {

    // case 1: both roots,
    if (parent[sourceID] < 0 && parent[destID] < 0) {
      if (parent[sourceID] <= parent[destID]) {
        updateParent(sourceID, destID, parent);
      } else {
        updateParent(destID, sourceID, parent);
      }
    } else if (parent[sourceID] < 0 && parent[destID] >= 0) { // sourceID is root, merge sourceID to destID's parent
      updateParent(parent[destID], sourceID, parent);
    } else if (parent[sourceID] >= 0 && parent[destID] < 0) { // destID is root, merge destID to sourceID's parent
      updateParent(parent[sourceID], destID, parent);
    } else { // both not root
      if (parent[parent[sourceID]] <= parent[parent[destID]]) {
        updateParent(parent[sourceID], parent[destID], parent);
      } else {
        updateParent(parent[destID], parent[sourceID], parent);
      }

    }

//    // used for test
//    System.out.printf("Union ID %d and ID %d. parent --> %n", sourceID, destID);
//    StringBuilder message = new StringBuilder();
//    for (int k = 0; k < numOfCells; k++) {
//      message.append(" ").append(parent[k]);
//    }
//    System.out.println(message + "\n");

  }

  private static void updateParent(int root, int child, int[] parent) {
    parent[root] += parent[child];
    // Set the parent of child and its child to root.
    parent[child] = root;
    for (int k = 0; k < parent.length; k++) {
      if(parent[k] == child) {
        parent[k] = root;
      }
    }
  }

  private static void updateGrid(int sourceID, int destID, List<List<Cell>> grid){
    int numOfCols = grid.get(0).size();
    int sourceI = MazeHelper.iDToIdxI(sourceID, numOfCols);
    int sourceJ = MazeHelper.iDToIdxJ(sourceID, numOfCols);
    int destI = MazeHelper.iDToIdxI(destID, numOfCols);
    int destJ = MazeHelper.iDToIdxJ(destID, numOfCols);
    if((destID - sourceID) == 1) {
      grid.get(sourceI).get(sourceJ).tearDownWall(Direction.EAST);
      grid.get(destI).get(destJ).tearDownWall(Direction.WEST);
    } else {
      grid.get(sourceI).get(sourceJ).tearDownWall(Direction.SOUTH);
      grid.get(destI).get(destJ).tearDownWall(Direction.NORTH);
    }
  }


  private boolean checkRemainingWalls(int numOfRows, int numOfCols, int numOfRemainingWalls) {
    int numOfTotalWalls = calculateTotalWalls(numOfRows, numOfCols);
    if (numOfRemainingWalls < 0 ||
            numOfRemainingWalls > calculateMaxRemainingWalls(numOfTotalWalls, numOfRows, numOfCols)) {
      throw new IllegalArgumentException("NumOfRemainingWalls out of range!");
    }
    return true;
  }

  private static int calculateTotalWalls(int numOfRows, int numOfCols) {
    return 2 * numOfRows * numOfCols + numOfRows + numOfCols;   // numOfRows: R
  }

  private static int calculateMaxRemainingWalls(int numOfTotalWalls, int numOfRows, int numOfCols) {
    // numOfCols: C
    // numOfRemainingWalls: RW
    // numOfTotalWalls(Border + walls): TW = 2R*C + R + C
    // According to the Maze Document, it says perfect Maze may has maximum remaining walls
    // max(RW) = TW - R*C + 1 
    // RW = R*C + R + C + 1
    return numOfTotalWalls - numOfRows * numOfCols + 1;
  }


  private static void placeItems(TypeOfItem type, int numOfItems, Set<Integer> itemsSet,
                                 int numOfCells, int numOfCols, List<List<Cell>> grid) {
    Random randNum = new Random();
    int count = 0;

    while(count != numOfItems) {
      int ID = randNum.nextInt(numOfCells);
      if (!itemsSet.contains(ID)) {
        int i = MazeHelper.iDToIdxI(ID, numOfCols);
        int j = MazeHelper.iDToIdxJ(ID, numOfCols);
        if (type == TypeOfItem.GOLD_COIN) {
          grid.get(i).get(j).setHasGoldCoin(true);
        } else if (type == TypeOfItem.THIEF) {
          grid.get(i).get(j).setHasThief(true);
        }
        itemsSet.add(ID);
        count++;
      }
    }
  }

  private static void updatePlayerGoldCollection(Player player, List<List<Cell>> grid) {
    Location l = player.getCurrentLocation();
    int i = MazeHelper.locationToIdxI(l);
    int j = MazeHelper.locationToIdxJ(l);

    // a player "collects" gold by entering a room that has gold which is then removed from the room
    if (grid.get(i).get(j).getHasGoldCoin()){
      player.collectGold();
      removeGoldCoin(l, grid);
    } else if (grid.get(i).get(j).getHasThief()) { // a player "loses" 10% of their gold by entering a room with a thief
      player.loseGold();
    }
  }

  private static void removeGoldCoin(Location l, List<List<Cell>> grid) {
    int i= MazeHelper.locationToIdxI(l);
    int j = MazeHelper.locationToIdxJ(l);

    grid.get(i).get(j).setHasGoldCoin(false);
  }

  // There is one goal location cell.
  @Override
  public Location getGoalLocation(){
    return this.goalLocation;
  }

  @Override
  public Location getStartLocation() {
    return this.startLocation;
  }

  // The maze can produce the possible moves of the player (North, South, East or West) from their current location
  @Override
  public List<Direction> produceNextPossibleMove(Location l){
    List<Direction> dirList = new ArrayList<>();
    int i = MazeHelper.locationToIdxI(l);
    int j = MazeHelper.locationToIdxJ(l);

    dirList = grid.get(i).get(j).findNextPossibleMove();

    return dirList;
  }

  // Print out the next possible moves as a String
  @Override
  public String printNextPossibleMove(Location l){
    List<Direction> dirList = produceNextPossibleMove(l);

    return "Player's next possible move is: " + dirList;
  }

  // The maze move player to next cell.
  @Override
  public void trigger(Direction playerDirectionInput) {
    if (!produceNextPossibleMove(player.getCurrentLocation()).contains(playerDirectionInput)) {
      throw new IllegalArgumentException("Player can not move to this direction " + playerDirectionInput + ". A wall stands at this direction.");
    }
    player.nextMove(playerDirectionInput);
    updatePlayerGoldCollection(player, grid);

    if (player.getCurrentLocation().equals(goalLocation)) {
      this.isGameOver = true;
    }
  }

  // The maze can produce the player's location and gold count
  @Override
  public Location getPlayerLocation() {
    return player.getCurrentLocation();
  }

  @Override
  public int getPlayerGoldCount() {
    return player.getGold();
  }

  @Override
  public String printPlayerStatus() {
    return player.toString();
  }

  @Override
  public boolean isGameOver() {
    return this.isGameOver;
  }



  @Override
  public List<List<Cell>> getGrid() {
    return this.grid;
  }


  // TODO: only used for test
  @Override
  public String toString() {
    String mazeGrid = "";
    for(int i = 0; i < grid.size(); i++) {
      for(int j = 0; j < grid.get(i).size(); j++) {
        mazeGrid += grid.get(i).get(j).toString();
      }
    }
    return mazeGrid + "\n" + player;
  }
}
