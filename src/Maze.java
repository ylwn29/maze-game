// There is one goal location cell
//
//20% of locations (at random) have gold coins in them that the player can pick up
//
//10% of locations (at random) have a thief that takes some of the player's gold coins
//
//The maze can produce the player's location and gold count
//
//The maze can produce the possible moves of the player (North, South, East or West) from their current location
//
//The player can make a move by specifying a direction
//
//The player should be able to collect gold such that:
//1. a player "collects" gold by entering a room that has gold which is then removed from the room
//2. a player "loses" 10% of their gold by entering a room with a thief

import java.util.List;

public interface Maze {

    // There is one goal location cell
    Location getGoalLocation();

    // TODO: do we have to print a list of Location where the gold coin or thief stands
    // 20% of locations (at random) have gold coins in them that the player can pick up
    //10% of locations (at random) have a thief that takes some of the player's gold coins

    Location getStartLocation();

    // The maze can produce the player's location and gold count
    Location getPlayerLocation();
    int getPlayerGoldCount();
    String printPlayerStatus(); // player current location and total gold coin

    // The maze can produce the possible moves of the player (North, South, East or West) from their current location
    List<Direction> produceNextPossibleMove(Location l);
    String printNextPossibleMove(Location l);


    void trigger(Direction playerDirectionInput);

//    // When a player collects the gold coin at location `l`, then remove the coin from this location
//    void removeGoldCoin(Location l);

    boolean isGameOver();

    List<List<Cell>> getGrid();


}
