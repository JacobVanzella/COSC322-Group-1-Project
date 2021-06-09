package amazonsChess;

import java.util.ArrayList;
import java.util.List;

// TODO Consider creating a Move class to simplify code
// Constructors
// public class Move(){}
// public class Move(row, col){}
// public class Move(queenCurrRow, queenCurrCol, queenNextRow, queenNextCol)
// public class Move(queenCurrRow, queenCurrCol, queenNextRow, queenNextCol, spearRow, spearCol){}

public class RecursiveAI extends Board {

	RecursiveAI parent = null;

	public RecursiveAI() {
		super(); // calls Board() to create a board for the AI
	}

	public RecursiveAI(int[][] board) {
		super(board);
		// this.board = board;
	}

	public RecursiveAI(RecursiveAI parent) {
		super();
		this.parent = parent;
	}

	public int scoreMove(int[][] board, int player) {

		RecursiveAI testMoveAI = new RecursiveAI(board); // create copy of board

		int opponent = (player == 1) ? 2 : 1;
		int playerMoves = getMoves(testMoveAI, player).size();
		int opponentMoves = getMoves(testMoveAI, opponent).size();
		int score = playerMoves - opponentMoves;

		return score;
	}

	public List<int[]> getMoves(RecursiveAI currBoard, int player) {
		// The nested List has form (prevRow, prevCol, nextRow, nextCol) for available
		// moves
		List<int[]> moves = new ArrayList<int[]>(); // will store array of moves in a list

		// Queens are implemented as a list since the opponent can delete queens when
		// they play invalid moves (will break array)
		List<List<Integer>> queenPos = new ArrayList<List<Integer>>();

		// System.out.println(currBoard.toString()); // for debugging

		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if (currBoard.getTile(row, col) == player) {
					// System.out.println("Row: " + row + " Col: " + col); // for debugging
					ArrayList<Integer> queenN = new ArrayList<Integer>();
					queenN.add(row);
					queenN.add(col);
					queenPos.add(queenN);
				}
			}
		}
		// Find possible moves for each queen
		for (List<Integer> queenCordinates : queenPos) {
			int currRow = queenCordinates.get(0);
			int currCol = queenCordinates.get(1);
			int nextRow = currRow;
			int nextCol = currCol;
			int dist = 0;
			boolean canMove = true;
			boolean spearCanMove = true;
			int spearRow = -1;
			int spearCol = -1;
			int spearDist = 0;

			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					// .println(currBoard.toString());
					canMove = true;
					dist = 1;
					try {
						while (canMove) {
							nextRow = currRow + i * dist;
							nextCol = currCol + j * dist;

							if (currBoard.getTile(nextRow, nextCol) == EMPTY) {
								// System.out.println(currRow + " " + currCol + " " + nextRow + " " + nextCol);

								RecursiveAI testboard = new RecursiveAI(currBoard.getCloneBoard());
								testboard.moveQueen(currRow, currCol, nextRow, nextCol, player);

								for (int k = -1; k < 2; k++) {
									for (int r = -1; r < 2; r++) {
										spearCanMove = true;
										spearDist = 1;
										// spearRow = nextRow;
										// spearCol = nextCol;
										try {
											while (spearCanMove) {
												spearRow = nextRow + k * spearDist;
												spearCol = nextCol + r * spearDist;
												// System.out.println(currRow + " " + currCol + " " + nextRow + " " +
												// nextCol);
												// System.out.println(spearRow + " " + spearCol);
												if (testboard.getTile(spearRow, spearCol) == EMPTY) {
													// System.out.println(testboard.toString());
													int[] move = new int[] { currRow, currCol, nextRow, nextCol,
															spearRow, spearCol };
													moves.add(move);
													spearDist++;
												} else {
													spearCanMove = false;
												}
											}
										} catch (Exception e) {
											spearCanMove = false;
										}
									}
								}
								dist++;
							} else {
								canMove = false;
							}
						}
					} catch (Exception e) {
						canMove = false;
					}
				}
			}
		}
		return moves;
	}

	public boolean wasValidMove(RecursiveAI currBoard, int opponent, int[] move) {
		List<int[]> validMoves = currBoard.getMoves(currBoard, opponent);

		int prevRowAdjusted = move[0] - 1;
		int prevColAdjusted = move[1] - 1;
		int nextRowAdjusted = move[2] - 1;
		int nextColAdjusted = move[3] - 1;
		int spearRowAdjusted = move[4] - 1;
		int spearColAdjusted = move[5] - 1;
		int[] adjMove = { prevRowAdjusted, prevColAdjusted, nextRowAdjusted, nextColAdjusted, spearRowAdjusted,
				spearColAdjusted };
		for (int[] moveN : validMoves) {
			boolean contains = true;
			for (int i = 0; i < move.length; i++) {
				if (moveN[i] != adjMove[i]) {
					contains = false;
				}
				if (i == move.length - 1 && contains == true) {
					return true;
				}
			}
		}
		return false;
	}

	public int depth = 0;
	public int desiredDepth = 1;
	Long startTime;
	Long currentTime;
	Long runTime = (long) 27;

	public int[] iterativeDeepeningSearch(int player, int opponent) {
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		this.startTime = System.currentTimeMillis();
		this.currentTime = System.currentTimeMillis();

		boolean toContinue = true;
		int[] bestMove = new int[7];
		int[] currentMove = new int[7];
		while (toContinue == true) {
			currentMove = alphaBetaJared(this, alpha, beta, player, opponent);

			System.out.println("CURRENT DEPTH: " + this.desiredDepth);
			
			int invalidCounter = 0;
			for (int i : currentMove) {
				if (i == Integer.MIN_VALUE) {
					invalidCounter++;
				}
			}
			if (invalidCounter > 1) {
				toContinue = false;
			} else {
				for (int i = 0; i < currentMove.length; i++) {
					bestMove[i] = currentMove[i];
				}
			}

			// currentTime = System.currentTimeMillis();
			System.out.println("TOTAL RUNTIME: " + (currentTime - startTime) / 1000);
			System.out.print("CURRENT BEST MOVE: ");
			for (int i = 0; i < bestMove.length; i++) {
				System.out.print(bestMove[i] + " ");
			}
			System.out.println();

			desiredDepth++;

			if (toContinue == false)
				System.out.println("\nSEARCH ENDED AT DEPTH: " + (desiredDepth - 1));
			System.out.println();
		}
		return bestMove;
	}

	public int[] alphaBetaJared(RecursiveAI boardState, int alpha, int beta, int player, int opponent) {

		// System.out.println(boardState.toString());
		int[] currentMove = new int[7];

		this.currentTime = System.currentTimeMillis();
		Long timeElapsed = (this.currentTime - this.startTime) / 1000;

		if (timeElapsed > this.runTime) {
			for (int i = 0; i < currentMove.length; i++) {
				currentMove[i] = Integer.MIN_VALUE;
			}
			return currentMove;
		}

		if (depth >= desiredDepth) {
			int score = boardState.scoreMove(boardState.getCloneBoard(), player);
			int[] returnVal = new int[7];
			returnVal[0] = score;
			return returnVal;
		}

		if (depth % 2 == 0) { // Max turn
			int[] max = new int[7];
			max[0] = Integer.MIN_VALUE;
			List<int[]> playerMoveList = boardState.getMoves(boardState, player);
			if (playerMoveList.isEmpty() == false) {
				
				depth++;
				
				for (int[] childMove : playerMoveList) {

					RecursiveAI childBoard = new RecursiveAI(boardState.getCloneBoard());
					childBoard.moveQueen(childMove[0], childMove[1], childMove[2], childMove[3], player);
					childBoard.throwSpear(childMove[4], childMove[5]);

					currentMove = alphaBetaJared(childBoard, alpha, beta, player, opponent);

					if (currentMove[0] == Integer.MIN_VALUE) {
						for (int i = 0; i < currentMove.length; i++) {
							currentMove[i] = Integer.MIN_VALUE;
						}
						return currentMove;
					}

					// max = Math.max(max.score, currentMove.score);
					if (currentMove[0] > max[0]) {
						max[0] = currentMove[0];
						for (int i = 0; i < 6; i++) {
							max[i + 1] = childMove[i];
						}
					}

					// alpha = Math.max(max.score, alpha);
					if (max[0] > alpha) {
						alpha = max[0];
					}

					// if beta <= alpha can prune branch
					if (beta <= alpha) {
						break;
					}
				}
			} else {
				currentMove[0] = - this.depth * 10000;
				depth --;
				return currentMove;
			}

			depth--;

			// return max - which is the move array
			return max;

		} else { // Min turn
			int[] min = new int[7];
			min[0] = Integer.MAX_VALUE;
			List<int[]> opponentMoveList = boardState.getMoves(boardState, opponent);
			if (opponentMoveList.isEmpty() == false) {
				
				depth++;
				
				for (int[] childMove : opponentMoveList) {

					RecursiveAI childBoard = new RecursiveAI(boardState.getCloneBoard());
					childBoard.moveQueen(childMove[0], childMove[1], childMove[2], childMove[3], opponent);
					childBoard.throwSpear(childMove[4], childMove[5]);

					currentMove = alphaBetaJared(childBoard, alpha, beta, player, opponent);

					if (currentMove[0] == Integer.MIN_VALUE) {
						for (int i = 0; i < currentMove.length; i++) {
							currentMove[i] = Integer.MIN_VALUE;
						}
						return currentMove;
					}

					// min = Math.min( min.score, currentMove.score);
					if (currentMove[0] < min[0]) {
						min[0] = currentMove[0];
						for (int i = 0; i < 6; i++) {
							min[i + 1] = childMove[i];
						}
					}

					// beta = Math.min( min, beta)
					if (min[0] < beta) {
						beta = min[0];
					}

					// if beta <= alpha can prune branch
					if (beta <= alpha) {
						break;
					}

				}
			} else {
				currentMove[0] = this.depth * 10000;
				depth --;
				return currentMove;
			}
			depth--;

			// return min - which is the move array
			return min;
		}
	}
}