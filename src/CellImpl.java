import java.util.*;
import java.util.List;

public class CellImpl implements Cell{
  private final int ID;
  private final Location cellLocation;
  private final Map<Direction, Integer> walls;
  private boolean hasGoldCoin;
  private boolean hasThief;

  public CellImpl(int ID, int numOfCols) {
    this.ID = ID;
    this.cellLocation = MazeHelper.iDToLocation(ID, numOfCols);
    this.walls = new HashMap<>();
    initializeWalls();
    this.hasGoldCoin = false;
    this.hasThief = false;
  }


  private void initializeWalls() {
    for(Direction d: Direction.values()) {
      this.walls.put(d, 1); // Initially the walls at all direction are set to 1.
    }
  }


  @Override
  public void tearDownWall(Direction d) {
    this.walls.replace(d, 0); // After tearing down the wall, set the wall at `d` to 0.
  }


  @Override
  public void setHasGoldCoin(boolean b) {
    this.hasGoldCoin = b;
  }

  @Override
  public void setHasThief(boolean b) {
    this.hasThief = b;
  }

  @Override
  public List<Direction> findNextPossibleMove() {
    List<Direction> dList = new ArrayList<>();
    for(Direction d: Direction.values()) {
      if(this.walls.get(d) == 0) {
        dList.add(d);
      }
    }
    return dList;
  }

  @Override
  public boolean getHasGoldCoin() {
    return this.hasGoldCoin;
  }

  @Override
  public boolean getHasThief() {
    return this.hasThief;
  }

  @Override
  public Location getCellLocation() {
    return this.cellLocation;
  }

  @Override
  public Map<Direction, Integer> getWalls() {
    return this.walls;
  }

  @Override
  public String toString() {
    return "ID: " + ID + "\nLocation: " + cellLocation + "\nWalls status: " + walls + "\nHas gold coin: " + hasGoldCoin + "\nHas Thief: " + hasThief + "\n";
  }
}
