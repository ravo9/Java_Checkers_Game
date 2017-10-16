// Undo doesn't work properly.
import java.util.*;

public class Move {
	
	static int turn = 5;
	static LinkedList<int[][]> movesStorage = new LinkedList<int[][]>();

	static void storeMove(int[][] boardState) {
		movesStorage.add(boardState);
	}
	
	static void move(Board b) {
		// '5' indicates White player's turn, '-5' - the Black one.
		b.printBoard();
		
		if (turn == 5)
			System.out.println("\nPlayer A, your turn!");
		else
			System.out.println("\nPlayer B, your turn!");
		
		Scanner scan= new Scanner(System.in);
		
		System.out.println("\nWhich pawn do you want to move?");
		String origin = scan.nextLine();
		
		if (origin.equals("undo")) {
			undo(b);
			return;
		}
	
		// English correctly?
		System.out.println("\nWhich position would you like to jump on?");
		String dest = scan.nextLine();
		
		if (origin == "undo") {
			undo(b);
			return;
		}
		
		b.movement(origin,  dest, turn);
		movesStorage.add(b.boardState);
		
		turn *= -1;
	}
	
	static void undo(Board b) {
		turn *= -1;
		movesStorage.removeLast();
		b.boardState = movesStorage.getLast();
	}
	
	static void wrongMove(Board b) {
		turn *= -1;
		movesStorage.removeLast();
	}
}
