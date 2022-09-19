import java.awt.event.KeyListener;
import java.util.List;

public interface View {
	void paint(boolean isGameOver, List<List<Cell>> grid, Location playerLocation, Location startLocation,
	           Location goalLocation, String nextMove, String playerStatus);
	void addKeyListener(KeyListener keyListener);
}
