import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.List;

public class SwingView implements View{
	private final SwingPanel panel;

	public SwingView() {
		JFrame frame = new JFrame("My Maze Game");
		panel = new SwingPanel();
		panel.setPreferredSize(new Dimension(Parameters.WIDTH, Parameters.LENGTH));
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void addKeyListener(KeyListener keyListener) {
		panel.addKeyListener(keyListener);
	}

	@Override
	public void paint(boolean isGameOver, List<List<Cell>> grid, Location playerLocation, Location startLocation,
	                  Location goalLocation, String nextMove, String playerStatus) {
		panel.paint(isGameOver, grid, playerLocation, startLocation, goalLocation, nextMove, playerStatus);
	}
}
