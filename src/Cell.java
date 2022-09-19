import java.util.*;

public interface Cell {
  // Tear down the wall of this cell at direction d.
  void tearDownWall(Direction d);
  void setHasGoldCoin(boolean b);
  void setHasThief(boolean b);
  // Return a list of Direction which the player is able to move to.
  List<Direction> findNextPossibleMove();

  boolean getHasGoldCoin();
  boolean getHasThief();

  Location getCellLocation();

  Map<Direction, Integer> getWalls();

}
