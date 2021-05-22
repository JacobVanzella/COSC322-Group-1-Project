package amazonsChess;

import java.util.ArrayList;
import java.util.List;

public class Board {

	protected int[][] board; // can change to 2D array based on preference
	public static final int EMPTY = 0;
	public static final int W = 1;
	public static final int B = 2;
	public static final int SPEAR = 3;

	public Board() {
		board = new int[][] {
			{ 0, 0, 0, B, 0, 0, B, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ B, 0, 0, 0, 0, 0, 0, 0, 0, B },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ W, 0, 0, 0, 0, 0, 0, 0, 0, W },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, W, 0, 0, W, 0, 0, 0 },
			};
	}

	public Board(Board source) {
		this();
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				board[i][j] = source.board[i][j];
	}

	public int[][] getBoard() {
		return this.board;
	}

	public void updateBoard(int[][] newBoard) {
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				board[i][j] = newBoard[i][j];
	}

	public void clone(Board source) {
		for (int i = 0; i < 100; i++)
			for (int j = 0; j < 10; j++)
				board[i][j] = source.board[i][j];
	}

	public int getTile(int row, int col) {
		return board[row][col];
	}

	public String getTileSymbol(int row, int col) {
		int tile = getTile(row, col);
		if (tile == EMPTY)
			return ".";
		if (tile == W)
			return "W";
		if (tile == B)
			return "B";
		if (tile == SPEAR)
			return "X";
		return "" + (row * 10 + col + 1);
	}

	// Row column position moves
	public void moveQueen(int prevRow, int prevCol, int nextRow, int nextCol, int player) {
		board[prevRow][prevCol] = EMPTY;
		board[nextRow][nextCol] = player;
	}

	public void throwSpear(int row, int col) {
		board[row][col] = SPEAR;
	}

	// Return 1 on white win, 2 on black win, else return 0
	public int hasWinner() {
		int hasWinner = 0;
		boolean blackCanMove = false;
		boolean whiteCanMove = false;
		List<List<Integer>> queens = new ArrayList<List<Integer>>();

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (board[i][j] == 1 || board[i][j] == 2) {
					ArrayList<Integer> queenN = new ArrayList<Integer>();
					queenN.add(i);
					queenN.add(j);
					queens.add(queenN);
				}
			}
		}

		for (List<Integer> queen : queens) {
			int row = queen.get(0);
			int col = queen.get(1);

			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					try {
						if (board[row + i][col + j] == EMPTY)
							if (board[row][col] == 1)
								whiteCanMove = true;
							else
								blackCanMove = true;
					} catch (Exception e) {
						continue; // If out of bounds continue to next loop
					}
				}
			}
		}

		if (blackCanMove == false) {
			hasWinner = 1;
		} else if (whiteCanMove == false) {
			hasWinner = 2;
		}
		
		return hasWinner;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				sb.append(getTileSymbol(row, col));
			}
			if (row != 9) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
