import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SwingPanel extends JPanel {
	private List<List<Cell>> grid;
	private Location startLocation;
	private Location goalLocation;
	private Location playerLocation;
	private Image startLocationImage;
	private Image goalLocationImage;
	private Image playerImage;
	private Image goldImage;
	private Image thiefImage;
	private boolean isGameOver;
	private String nextMove;
	private String playerStatus;
	private boolean initialized = false;

	public SwingPanel() {
		// load images
		try {
			playerImage = ImageIO.read(new File("./img/player.png"));
			goldImage = ImageIO.read(new File("./img/gold.png"));
			thiefImage = ImageIO.read(new File("./img/thief.png"));
			startLocationImage = ImageIO.read(new File("./img/start.png"));
			goalLocationImage = ImageIO.read(new File("./img/goal.png"));
		} catch (IOException e) {
			System.out.println("file doesn't exist");
		}
		this.setFocusable(true);
	}

	public void paint(boolean isGameOver, List<List<Cell>> grid, Location playerLocation, Location startLocation,
	                  Location goalLocation, String nextMove, String playerStatus) {
		this.isGameOver = isGameOver;
		this.grid = grid;
		this.playerLocation = playerLocation;
		this.goalLocation = goalLocation;
		this.startLocation = startLocation;
		this.nextMove = nextMove;
		this.playerStatus = playerStatus;
		this.initialized = true;

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!initialized) {
			return;
		}
		if (isGameOver) {
			// draw game over string
			drawMaze(g);
			g.drawString("Game Over", Parameters.CELL_SIZE, Parameters.LENGTH - Parameters.CELL_SIZE * 4);
			return;
		}

		drawMaze(g);
		g.drawString(nextMove, Parameters.CELL_SIZE, Parameters.LENGTH - Parameters.CELL_SIZE * 4);
		g.drawString(playerStatus, Parameters.CELL_SIZE, Parameters.LENGTH- Parameters.CELL_SIZE * 2);
	}

	private void drawMaze(Graphics g) {
		for(int i = 0; i < grid.size(); i++) {
			for(int j = 0; j < grid.get(0).size(); j++) {
				Cell c = grid.get(i).get(j);
				if (c.getWalls().get(Direction.NORTH) == 1) {
					drawWall(Direction.NORTH, g, c);
				}
				if (c.getWalls().get(Direction.WEST) == 1) {
					drawWall(Direction.WEST, g, c);
				}
				if (i == grid.size() - 1 && c.getWalls().get(Direction.SOUTH) == 1) {
					drawWall(Direction.SOUTH, g, c);
				}
				if (j == grid.get(0).size() - 1 && c.getWalls().get(Direction.EAST) == 1) {
					drawWall(Direction.EAST, g, c);
				}
				if (c.getHasGoldCoin()) {
					g.drawImage(goldImage, c.getCellLocation().getX(), c.getCellLocation().getY(), this);
				}
				if (c.getHasThief()) {
					g.drawImage(thiefImage, c.getCellLocation().getX(), c.getCellLocation().getY(), this);
				}
			}
		}
		g.drawImage(startLocationImage, startLocation.getX(), startLocation.getY(), this);
		g.drawImage(goalLocationImage, goalLocation.getX(), goalLocation.getY(), this);
		g.drawImage(playerImage, playerLocation.getX(), playerLocation.getY(), this);
	}

	private void drawWall(Direction d, Graphics g, Cell c) {
		int x = c.getCellLocation().getX();
		int y = c.getCellLocation().getY();
		int endX = x;
		int endY = y;
		switch (d) {
			case NORTH -> endX += Parameters.CELL_SIZE;
			case WEST -> endY += Parameters.CELL_SIZE;
			case SOUTH -> {
				y += Parameters.CELL_SIZE;
				endX += Parameters.CELL_SIZE;
				endY = y;
			}
			case EAST -> {
				x += Parameters.CELL_SIZE;
				endX = x;
				endY += Parameters.CELL_SIZE;
			}
			default -> throw new IllegalArgumentException("Unsupported Direction!");
		}
		g.drawLine(x, y, endX, endY);
	}
}
