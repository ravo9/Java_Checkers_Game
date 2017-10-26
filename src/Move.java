import java.util.*;
import java.util.LinkedList;

public class Move {
	
	static void storeMove(Board b) {
		int[][]stateCopy = new int[8][8];
		for (int i = 0; i < 8; i++) 
			System.arraycopy(b.boardState[i], 0, stateCopy[i], 0, 8);
		b.movesStorage.add(stateCopy);
	}

	
	// The function which takes a user's designation of a position (e.g. "A1" or "2B")
	// and transforms it into arrays coords.
	static int[] transformDesignation(String pos) {
		int xy[] = new int[2];
			
		for (int i=0; i<8; i++) 
			if (pos.contains(Integer.toString(i))) xy[0] = i - 1;
			
		String letters[] = {"A", "B", "C", "D", "E", "F", "G", "H"};
		int acc = 0;
		for (String el: letters) {
			if (pos.contains(el) || pos.contains(el.toLowerCase())) xy[1] = acc;
			acc++;
		}
		return xy;
	}
	
	
	static int checkMoveValidity(Board b, int xyA[], int xyB[]){
		// Flag that indicates wrong move.
		// 0 means a correct move, 1 means a wrong move, 3 means an attack.
		int flagM = 0;
		
		// The right pawn hasn't been chosen.
		if (b.turn == 5 && (b.boardState[xyA[0]][xyA[1]] != -1 && b.boardState[xyA[0]][xyA[1]] != -2)) {
			System.out.println("You need to choose your pawna first!");
			return flagM = 1;
		}
		else if (b.turn == -5 && (b.boardState[xyA[0]][xyA[1]] != 1 && b.boardState[xyA[0]][xyA[1]] != 2)) {
			System.out.println("You need to choose your pawn first!");
			return flagM = 1;
		}
		
		// Movement along X or Y axis.
		if (xyA[0] == xyB[0] || xyA[1] == xyB[1]) {
			System.out.println("Wrong move (movement along X or Y axis)!");
			return flagM = 1;
		}
		
		// Movement onto an occupied place.
		if (b.boardState[xyB[0]][xyB[1]] != 0) {
			System.out.println("Wrong move (occupied place)!");
			return flagM = 1;
		}
			
		// The dest. position is out of the board.
		if (((xyB[0] > 7) || (xyB[0] < 0)) || ((xyA[0] > 7) || (xyA[0] < 0))) {
			System.out.println("Wrong move (out of the board)!");
			return flagM = 1;
		}
		
		// The movement isn't performed forward.
			// Firstly check if it is not a king selected.
		if (b.boardState[xyA[0]][xyA[1]] != 2 && b.boardState[xyA[0]][xyA[1]] != -2) {
			if (b.turn == 5 && xyA[0] <= xyB[0]) {
				System.out.println("The pawn has to move forward!");
				return flagM = 1;
			}
			else if (b.turn == -5 && xyA[0] >= xyB[0]) {
				System.out.println("The pawn has to move forward!");
				return flagM = 1;
			}	
		}
		
		// The dest. position is more than 1 step far from the origin.
		// Is it a jump (an attack)?
		// 1. Is it more than 1 step far from the origin?
		// Now it has to be the last test.
		if (Math.abs(xyB[0]-xyA[0]) > 1 || Math.abs(xyB[1]-xyA[1]) > 1) {
			
			// 2. Is it 2-fields long movement?
			if (Math.abs(xyB[0]-xyA[0]) == 2 && Math.abs(xyB[1]-xyA[1]) == 2) {
				
				// 3. Is there an opponent pawn on the middle position?
				if (b.turn == 5 && b.boardState[(xyB[0]+xyA[0])/2][(xyB[1]+xyA[1])/2] == 1) 
					return flagM = 3;
				
				else if (b.turn == -5 && b.boardState[(xyB[0]+xyA[0])/2][(xyB[1]+xyA[1])/2] == -1) 
					return flagM = 3;
			}
			
		System.out.println("Wrong move (too far and it's not and attack!");
		return flagM = 1;
		}
		
		return flagM;
	}
	
	
	// Human - human game
	static void playHuman(Board b) {
		// '5' indicates White player's turn, '-5' - the Black one.
		b.printBoard();
		
		if (b.turn == 5)
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
		
		if (dest.equals("undo")) {
			undo(b);
			return;
		}
		
		int xyA[] = new int[2];
		int xyB[] = new int[2];
		xyA = transformDesignation(origin);
		xyB = transformDesignation(dest);
		
		int rightMove = checkMoveValidity(b, xyA, xyB);
		
		// A movement.
		if (rightMove == 0 || rightMove == 3) {
		
				b.boardState[xyB[0]][xyB[1]] = b.boardState[xyA[0]][xyA[1]];
				b.boardState[xyA[0]][xyA[1]] = 0;
				// If it's a jump, then remove the jumped pawn.
				if (rightMove == 3) {
					b.boardState[(xyB[0]+xyA[0])/2][(xyB[1]+xyA[1])/2] = 0;
					if (b.turn == 5)
						System.out.println("Point for A player!");
					else
						System.out.println("Point for B player!");
				}
				// If it's the last row, then a pawn (man) becomes a king.
				if (b.turn == 5 && xyB[0] == 0)
					b.boardState[xyB[0]][xyB[1]] = -2;
				if (b.turn == -5 && xyB[0] == 7)
					b.boardState[xyB[0]][xyB[1]] = 2;
			
			// A movement is sent to the movesStorage.
			storeMove(b);
		}
		else 
			wrongMove(b);
		
		// If it was a jump (an attack), then ask the player if he wants to continue jumping.
		if (rightMove == 3) {
			
			System.out.println("\nWould you like to continue jumping (a multiple jump)?");
			System.out.println("Please answer 'yes' or 'not':");
			String multipleJump = scan.nextLine();
			
			// Undo function for this situation doesn't work yet.
			if (multipleJump.equals("yes")) {
				b.printBoard();
				b.turn *= -1;
			}
		}
		
		b.turn *= -1;
	}
	
	
	// Computer - human game.
	static void playComputer(Board b) {
		
		System.out.println("\n ----------------------------------------------- \n");
		
		// '5' indicates White player's turn, '-5' - the Black one.
		b.printBoard();
		
		if (b.turn == -5) {
			
			System.out.println("\nPlayer B, your turn!");
			Scanner scan= new Scanner(System.in);
			
			System.out.println("\nWhich pawn do you want to move?");
			String origin = scan.nextLine();
			
			if (origin.equals("undo")) {
				undo(b);
				return;
			}
		
			System.out.println("\nWhich position would you like to jump on?");
			String dest = scan.nextLine();
			
			if (dest.equals("undo")) {
				undo(b);
				return;
			}
			
			int xyA[] = new int[2];
			int xyB[] = new int[2];
			xyA = transformDesignation(origin);
			xyB = transformDesignation(dest);
			
			int rightMove = checkMoveValidity(b, xyA, xyB);
			
			// A movement.
			if (rightMove == 0 || rightMove == 3) {
			
					b.boardState[xyB[0]][xyB[1]] = b.boardState[xyA[0]][xyA[1]];
					b.boardState[xyA[0]][xyA[1]] = 0;
					// If it's a jump, then remove the jumped pawn.
					if (rightMove == 3) {
						b.boardState[(xyB[0]+xyA[0])/2][(xyB[1]+xyA[1])/2] = 0;
						if (b.turn == 5)
							System.out.println("Point for A player!");
						else
							System.out.println("Point for B player!");
					}
					// If it's the last row, then a pawn (man) becomes a king.
					if (xyB[0] == 7)
						b.boardState[xyB[0]][xyB[1]] = 2;
				
				// A movement is sent to the movesStorage.
				storeMove(b);
			}
			else 
				wrongMove(b);
			
			// If it was a jump (an attack), then ask the player if he wants to continue jumping.
			if (rightMove == 3) {
				
				System.out.println("\nWould you like to continue jumping (a multiple jump)?");
				System.out.println("Please answer 'yes' or 'not':");
				String multipleJump = scan.nextLine();
				
				// Undo function for this situation doesn't work yet.
				if (multipleJump.equals("yes")) {
					b.printBoard();
					b.turn *= -1;
				}
			}
		}
		
		// A computer move.
		else
		{
			int[] bestMove;
			bestMove = AI.bestAttackPower(b);
			
			int xyA[] = new int[2];
			int xyB[] = new int[2];
			
			xyA[0] = bestMove[0];
			xyA[1] = bestMove[1];
			
			
			xyB[0] = b.computerAttackPath.getFirst()[0];
			xyB[1] = b.computerAttackPath.getFirst()[1];
			
			// Debug
			System.out.println("A starting position: "+ xyA[0]+", "+ xyA[1]);
			System.out.println("A final position: "+ xyB[0]+", "+ xyB[1]);
			System.out.println("A number of fields in the path: "+ b.computerAttackPath.size());
			
			// A movement.		
			b.boardState[xyB[0]][xyB[1]] = b.boardState[xyA[0]][xyA[1]];
			b.boardState[xyA[0]][xyA[1]] = 0;
		
			
			// Remove all jumped pawns.
				// If there was any attack.
			if (b.computerAttackPath.size() > 1) {
				
				int attacksNumber = b.computerAttackPath.size()-1;
				
				for (int i=0; i<attacksNumber; i++) {
					int jumpedPawnX = (b.computerAttackPath.get(i)[0] + b.computerAttackPath.get(i+1)[0])/2;
					int jumpedPawnY = (b.computerAttackPath.get(i)[1] + b.computerAttackPath.get(i+1)[1])/2;
					b.boardState[jumpedPawnX][jumpedPawnY] = 0;
					System.out.println("\n ----------------------------------------------- \n");
					System.out.println("Point for the Computer Player!");
				}
			}
			
			// A movement is sent to the movesStorage.
				storeMove(b);
				b.computerAttackPath.clear();
		}
		

		b.turn *= -1;
	}
	
	
	static void undo(Board b) {
		b.turn *= -1;
		b.movesStorage.removeLast();
	    b.boardState = b.movesStorage.getLast();
	}
	
	
	static void wrongMove(Board b) {
		b.turn *= -1;
	}
	

	
	
}
