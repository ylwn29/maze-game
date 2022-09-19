class Main {
  public static void main(String[] args) {
////    Location l = new Location(100, 100);
////    Maze m = new PerfectMaze(2, 2, l);
//    Location start = new Location(10, 10);
//    Location goal = new Location(50, 30);
//    Game perfect = Game.createGame(TypeOfMaze.PERFECT, 2, 3, 1, start, goal);
//
//
//    while(!perfect.isGameOver()) {
//      System.out.print(perfect.controlPlayer());
//    }

      // Set start location and goal location.
      int numOfRows = (Parameters.LENGTH - Parameters.CELL_SIZE * 6) / Parameters.CELL_SIZE;
      int numOfCols = (Parameters.WIDTH - Parameters.CELL_SIZE * 6) / Parameters.CELL_SIZE;
      int startI = 0;
      int startJ = 0;
      int startLocationX = MazeHelper.idxJToLocationX(startJ);
      int startLocationY = MazeHelper.idxIToLocationY(startI);
      int goalI = numOfRows - 1;
      int goalJ = numOfCols - 1;
      int goalLocationX = MazeHelper.idxJToLocationX(goalJ);
      int goalLocationY = MazeHelper.idxIToLocationY(goalI);
      Location startLocation = new Location(startLocationX, startLocationY);
      Location goalLocation = new Location(goalLocationX, goalLocationY);
      System.out.println("Rows: " + numOfRows + " Cols: " + numOfCols + "\n");

      Maze maze = PerfectMaze.createPerfectMaze(numOfRows, numOfCols, startLocation, goalLocation);
      View view = new SwingView();
      Controller c = new Controller(maze, view);
      c.startGame();
  }
}