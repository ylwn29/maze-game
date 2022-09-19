public class PlayerImpl implements Player{
  private final Location playerLocation;
  private int gold;
  
  public PlayerImpl(Location playerLocation) {
    this.playerLocation = playerLocation;
    this.gold = 0;
  }

    //The player can make a move by specifying a direction
    // Direction d must be contained in the nextPossibleMove
    // update the playerLocation, goldCount
  @Override
  public void nextMove(Direction d){
    switch (d) {
      case NORTH -> updatePlayerLocationY(Parameters.CELL_SIZE * (-1));
      case SOUTH -> updatePlayerLocationY(Parameters.CELL_SIZE);
      case EAST -> updatePlayerLocationX(Parameters.CELL_SIZE);
      case WEST -> updatePlayerLocationX(Parameters.CELL_SIZE * (-1));
    }
  }

  private void updatePlayerLocationX(int delta) {
    this.playerLocation.setX(this.playerLocation.getX() + delta);
  }

  private void updatePlayerLocationY(int delta) {
    this.playerLocation.setY(this.playerLocation.getY() + delta);
  }

  @Override
  public void collectGold() {
    this.gold += 1;
  }


  @Override
  public void loseGold() {
    this.gold -= MazeHelper.getXPercentageInInt(this.getGold(), Parameters.THIEF_PERCENT);

    if (this.gold < 0) {
      this.gold = 0;
    }
  }

  @Override
  public int getGold() {
    return this.gold;
  }

  @Override
  public Location getCurrentLocation() {
    return this.playerLocation;
  }

  @Override
  public String toString() {
    return "Currently player is at " + playerLocation + " Collecting: " + gold + " gold coins.\n";
  }
}