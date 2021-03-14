import java.util.Map;
import java.util.Scanner;

public class Board {
	String[][] board = new String[3][3];
	String[] players = { "X", "O" };
	int currentPlayer;
	boolean[] available = new boolean[9];
	Map<String, Integer> scores = Map.of("X", -1, "O", 1, "Velha", 0);

	public Board() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = "";
				available[j + i * 3] = true;
			}
		}

		currentPlayer = 0;
	}

	public void drawBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.format("%d%d|_%s_|\t", j + 1, i + 1, board[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	public void nextTurn() {
		currentPlayer = (currentPlayer + 1) % players.length;
	}

	public void sortPosition(String position) {
		int x = Integer.parseInt((position.substring(0, 1))) - 1;
		int y = Integer.parseInt((position.substring(1, 2))) - 1;
		if (board[y][x].contentEquals("")) {
			board[y][x] = players[currentPlayer];
			available[y + x * 3] = false;
			nextTurn();
		} else {
			sortPosition(new Scanner(System.in).next());
		}
	}

	public void playBot() {
		int bestScore = Integer.MIN_VALUE;
		int bestMove[] = new int[2];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j].contentEquals("")) {
					board[i][j] = players[1];
					int score = miniMax(board, 10, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
					board[i][j] = "";
					if (score > bestScore) {
						bestScore = score;
						bestMove[0] = i;
						bestMove[1] = j;
					}
				}
			}
		}

		board[bestMove[0]][bestMove[1]] = players[currentPlayer];
		available[bestMove[0] + bestMove[1] * 3] = false;
		nextTurn();
	}

	private int miniMax(String[][] board, int depth, int alpha, int beta, boolean isMaximizing) {
		if (checkWinner(board) != null || depth == 0)
			return scores.get(checkWinner(board));

		if (isMaximizing) {
			// procura o maior valor possivel
			int bestScore = Integer.MIN_VALUE;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					// o local está disponível?
					if (board[i][j].contentEquals("")) {
						board[i][j] = players[1];
						int score = miniMax(board, depth - 1, alpha, beta, false) * depth;
						board[i][j] = "";
						bestScore = Math.max(score, bestScore);
						alpha = Math.max(alpha, score);
						if(beta <= alpha)
							break;
					}
				}
			}
			return bestScore;
		} else {
			// procura o menor valor possivel
			int bestScore = Integer.MAX_VALUE;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					// o local está disponível?
					if (board[i][j].contentEquals("")) {
						board[i][j] = players[0];
						int score = miniMax(board, depth - 1, alpha, beta, true) * depth;
						board[i][j] = "";
						bestScore = Math.min(score, bestScore);
						beta = Math.min(beta, score);
						if(beta <= alpha)
							break;
					}
				}
			}
			return bestScore;
		}
	}

	public boolean equals3(String a, String b, String c) {
		return (a == b && b == c && a != "");
	}

	public String checkWinner(String[][] board) {
		String winner = null;

		// Horizontais
		for (int i = 0; i < 3; i++) {
			if (equals3(board[i][0], board[i][1], board[i][2])) {
				winner = board[i][0];
			}
		}

		// Verticais
		for (int i = 0; i < 3; i++) {
			if (equals3(board[0][i], board[1][i], board[2][i])) {
				winner = board[0][i];
			}
		}

		// Diagonais
		if (equals3(board[0][0], board[1][1], board[2][2]))
			winner = board[0][0];

		if (equals3(board[2][0], board[1][1], board[0][2]))
			winner = board[2][0];

		int countAval = 0;
		for (int i = 0; i < available.length; i++) {
			if (!available[i])
				countAval++;
		}
		if (winner == null && countAval == 9) {
			return "Velha";
		} else {
			return winner;
		}
	}

}
