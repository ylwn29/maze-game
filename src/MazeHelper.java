public class MazeHelper {
	static int idxToID(int i, int j, int numOfCols) {
		return i * numOfCols + j;
	}

	static int iDToIdxI(int ID, int numOfCols) {
		return ID / numOfCols;
	}

	static int iDToIdxJ(int ID, int numOfCols) {
		return ID % numOfCols;
	}

	static int locationToIdxI(Location l) {
		return (l.getY() - Parameters.TOP_LEFT_Y) / Parameters.CELL_SIZE;
	}

	static int locationToIdxJ(Location l) {
		return (l.getX() - Parameters.TOP_LEFT_X) / Parameters.CELL_SIZE;
	}

	static int idxJToLocationX(int j) {
		return j * Parameters.CELL_SIZE + Parameters.TOP_LEFT_X;
	}

	static int idxIToLocationY(int i) {
		return i * Parameters.CELL_SIZE + Parameters.TOP_LEFT_Y;
	}


	static int getXPercentageInInt(int totalNum, double percentage) {
		return (int) Math.ceil(totalNum * (percentage / 100.0));
	}

	static Location idxToLocation(int i, int j) {
		int x = idxJToLocationX(j);
		int y = idxIToLocationY(i);
		return new Location(Math.abs(x), Math.abs(y));
	}

	static Location iDToLocation(int ID, int numOfCols) {
		int i = iDToIdxI(ID, numOfCols);
		int j = iDToIdxJ(ID, numOfCols);
		return idxToLocation(i, j);
	}
}
