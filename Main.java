/*
 * Minimax Algorithm
 * Alpha-beta prunning
 * 
 * Tic tac toe
 */
import java.util.Scanner;

public class Main {
	static Board game = new Board();
	static boolean isRunning = true;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (isRunning) {
			game.drawBoard();

			if (game.checkWinner(game.board) != null) {
				System.out.println(game.checkWinner(game.board));
				isRunning = false;
			}

			if (isRunning) {
				if (game.currentPlayer == 0) {
					try {
						game.sortPosition(scanner.next());
					} catch (ArrayIndexOutOfBoundsException aioobe) {
						game.sortPosition(scanner.next());
					} catch (Exception ex) {
						System.out.println("EXIT");
						isRunning = !isRunning;
					}
				} else {
					game.playBot();
				}
			}
		}

		scanner.close();
	}
}
