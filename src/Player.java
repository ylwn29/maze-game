public interface Player{
  // The player can make a move by specifying a direction
  void nextMove(Direction d);

  void collectGold();

  void loseGold();

//  // The player should be able to collect gold such that
//  // a player "collects" gold by entering a room that has gold which is then removed from the room
//  // a player "loses" 10% of their gold by entering a room with a thief
  int getGold();

  Location getCurrentLocation();
}