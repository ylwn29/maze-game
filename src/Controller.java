import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import static java.awt.event.KeyEvent.*;
import static java.awt.event.KeyEvent.VK_RIGHT;


public class Controller implements KeyListener {
	private final Maze maze;
	private final View view;

	public Controller(Maze maze, View view) {
		this.maze = maze;
		this.view = view;
		this.view.addKeyListener(this);
	}

	public void startGame() {
		paintMaze();
	}

	private void paintMaze() {
		// get the latest game status
		boolean isGameOver = maze.isGameOver();
		List<List<Cell>> grid = maze.getGrid();
		Location startLocation = maze.getStartLocation();
		Location goalLocation = maze.getGoalLocation();
		Location playerLocation = maze.getPlayerLocation();
		String nextMove = maze.printNextPossibleMove(playerLocation);
		String playerStatus = maze.printPlayerStatus();

		// ask the view to visualize the game status
		view.paint(isGameOver, grid, playerLocation, startLocation, goalLocation, nextMove, playerStatus);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		if (key == VK_UP) {
			maze.trigger(Direction.NORTH);
		} else if (key == VK_DOWN) {
			maze.trigger(Direction.SOUTH);
		} else if (key == VK_RIGHT) {
			maze.trigger(Direction.EAST);
		} else if (key == VK_LEFT) {
			maze.trigger(Direction.WEST);
		} else {
			// ignore
		}
		paintMaze();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
