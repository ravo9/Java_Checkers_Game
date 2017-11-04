import java.util.Scanner;

public class Movement {
	
	static void storeMove() {
		int[][]stateCopy = new int[8][8];
		for (int i = 0; i < 8; i++) 
			System.arraycopy(Game.boardState[i], 0, stateCopy[i], 0, 8);
		Game.movesStorage.add(stateCopy);
	}

	
	static int[] transformDesignation(String pos) {
		int xy[] = new int[2];
			
		for (int i=1; i<9; i++) 
			if (pos.contains(Integer.toString(i))) xy[0] = i - 1;
			
		String letters[] = {"A", "B", "C", "D", "E", "F", "G", "H"};
		int acc = 0;
		for (String el: letters) {
			if (pos.contains(el) || pos.contains(el.toLowerCase())) xy[1] = acc;
			acc++;
		}
		return xy;
	}
	
	
	static int checkMoveValidity(char player, int xyA[], int xyB[]){
		// Flag that indicates wrong move.
		// 0 means a correct move, 1 means a wrong move, 3 means an attack.
		int flagM = 0;
		
		// The right pawn hasn't been chosen.
		if (player == 'a' && (Game.boardState[xyA[0]][xyA[1]] != -1 && Game.boardState[xyA[0]][xyA[1]] != -2)) {
			System.out.println("You need to choose your pawn first!");
			return flagM = 1;
		}
		else if (player == 'b' && (Game.boardState[xyA[0]][xyA[1]] != 1 && Game.boardState[xyA[0]][xyA[1]] != 2)) {
			System.out.println("You need to choose your pawn first!");
			return flagM = 1;
		}
		
		// Movement along X or Y axis.
		if (xyA[0] == xyB[0] || xyA[1] == xyB[1]) {
			System.out.println("Wrong move (along X or Y axis)!");
			return flagM = 1;
		}
		
		// Movement onto an occupied place.
		if (Game.boardState[xyB[0]][xyB[1]] != 0) {
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
		if (Game.boardState[xyA[0]][xyA[1]] != 2 && Game.boardState[xyA[0]][xyA[1]] != -2) {
			if (player == 'a' && xyA[0] <= xyB[0]) {
				System.out.println("The pawn has to move forward!");
				return flagM = 1;
			}
			else if (player == 'b' && xyA[0] >= xyB[0]) {
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
				if (player == 'a' && Game.boardState[(xyB[0]+xyA[0])/2][(xyB[1]+xyA[1])/2] == 1) 
					return flagM = 3;
				
				else if (player == 'b' && Game.boardState[(xyB[0]+xyA[0])/2][(xyB[1]+xyA[1])/2] == -1) 
					return flagM = 3;
			}
			
		System.out.println("Wrong move (too far and it's not and attack!");
		return flagM = 1;
		}
		
		return flagM;
	}
	
	
	static void playerAMove() {
		System.out.println("\nPlayer A, your turn!\n");
		Board.printBoard();
		Scanner scan= new Scanner(System.in);
		String origin = "";
		
		if (Game.multipleJumpFlag == 0) {
			System.out.println("\nWhich pawn do you want to move?");
			origin = scan.nextLine();
			if (origin.equals("undo")) {
				undo();
				return;
			}
		}
		else if (Game.multipleJumpFlag == 1) {
			origin += Game.multipleJumpPawnBuffor[0];
			origin += Game.multipleJumpPawnBuffor[1];
			Game.multipleJumpFlag = 0;
			Game.multipleJumpPawnBuffor = null;
		}
		
		System.out.println("\nWhich position would you like to jump on?");
		String dest = scan.nextLine();
		if (dest.equals("undo")) {
			undo();
			return;
		}
		System.out.println("\n-------------------------");
		
		int xyA[] = new int[2];
		int xyB[] = new int[2];
		xyA = transformDesignation(origin);
		xyB = transformDesignation(dest);
		
		// A validity check.
		int rightMove = checkMoveValidity('a', xyA, xyB);
		
		// A movement.
		// 0 indicates a correct move, 3 indicates a correct jump.
		if (rightMove == 0 || rightMove == 3) {
			
			Game.boardState[xyB[0]][xyB[1]] = Game.boardState[xyA[0]][xyA[1]];
			Game.boardState[xyA[0]][xyA[1]] = 0;
			
			// If it's a jump, then remove the jumped pawn.
			if (rightMove == 3) {
				Game.boardState[(xyB[0]+xyA[0])/2][(xyB[1]+xyA[1])/2] = 0;
				System.out.println("Player A achieved a point!");
				System.out.println("-------------------------");
			}
			
			// If it's the last row, then a pawn (man) becomes a king.
			if (xyB[0] == 0)
				Game.boardState[xyB[0]][xyB[1]] = -2;
			
			// The move is sent to the movesStorage.
			storeMove();
			
			// If it was a jump (an attack), then ask the player if he wants to continue jumping.
			if (rightMove == 3) {
				
				System.out.println("\nWould you like to continue jumping (a multiple jump)?");
				System.out.println("Please answer 'yes' or 'not':");
				String multipleJump = scan.nextLine();
				
				if (multipleJump.equals("yes")) {
					
					Game.multipleJumpFlag = 1;
					Game.multipleJumpPawnBuffor[0] = xyB[0];
					Game.multipleJumpPawnBuffor[1] = xyB[1];
			
					Game.movesStorage.pop();
					playerAMove();
				}
			}			
		}
		else 
			playerAMove();
	}
	
	
	static void playerBMove() {
		System.out.println("\nPlayer B, your turn!\n");
		Board.printBoard();
		Scanner scan= new Scanner(System.in);
		String origin = "";
		
		if (Game.multipleJumpFlag == 0) {
			System.out.println("\nWhich pawn do you want to move?");
			origin = scan.nextLine();
			if (origin.equals("undo")) {
				undo();
				return;
			}
		}
		else if (Game.multipleJumpFlag == 1) {
			origin += Game.multipleJumpPawnBuffor[0];
			origin += Game.multipleJumpPawnBuffor[1];
			Game.multipleJumpFlag = 0;
			Game.multipleJumpPawnBuffor = null;
		}
		
		System.out.println("\nWhich position would you like to jump on?");
		String dest = scan.nextLine();
		if (dest.equals("undo")) {
			undo();
			return;
		}
		System.out.println("\n-------------------------");
		
		int xyA[] = new int[2];
		int xyB[] = new int[2];
		xyA = transformDesignation(origin);
		xyB = transformDesignation(dest);
		
		// A validity check.
		int rightMove = checkMoveValidity('b', xyA, xyB);
		
		// A movement.
		// 0 indicates a correct move, 3 indicates a correct jump.
		if (rightMove == 0 || rightMove == 3) {
			Game.boardState[xyB[0]][xyB[1]] = Game.boardState[xyA[0]][xyA[1]];
			Game.boardState[xyA[0]][xyA[1]] = 0;
			
			// If it's a jump, then remove the jumped pawn.
			if (rightMove == 3) {
				Game.boardState[(xyB[0]+xyA[0])/2][(xyB[1]+xyA[1])/2] = 0;
				System.out.println("Player B achieved a point!");
				System.out.println("-------------------------");
			}
			
			// If it's the last row, then a pawn (man) becomes a king.
			if (xyB[0] == 7)
				Game.boardState[xyB[0]][xyB[1]] = 2;
			
			// The move is sent to the movesStorage.
			storeMove();
			
			// If it was a jump (an attack), then ask the player if he wants to continue jumping.
			if (rightMove == 3) {
				
				System.out.println("\nWould you like to continue jumping (a multiple jump)?");
				System.out.println("Please answer 'yes' or 'not':");
				String multipleJump = scan.nextLine();
				
				if (multipleJump.equals("yes")) {
					
					Game.multipleJumpFlag = 1;
					Game.multipleJumpPawnBuffor[0] = xyB[0];
					Game.multipleJumpPawnBuffor[1] = xyB[1];
					
					Game.movesStorage.removeLast();
					playerBMove();
				}
			}
		}
		else 
			playerBMove();
	}
	
	
	static void playerComputerMove() {
		ComputerMove c = new ComputerMove();
		
		int startPositionX = c.xyA[0];
		int startPositionY = c.xyA[1];
		int finalPositionX = -1;
		int finalPositionY = -1;
		
		// If it is a jump/ multiple jump.
		if (c.highestPotentialValue > 1) {
			finalPositionX = c.attackPath.getFirst()[0];
			finalPositionY = c.attackPath.getFirst()[1];
			Game.boardState[finalPositionX][finalPositionY] = Game.boardState[startPositionX][startPositionY];
			Game.boardState[startPositionX][startPositionY] = 0;
			
			// Remove jumped pawns.
			int jumpsNumber = c.attackPath.size()-1;
			for (int i=0; i<jumpsNumber; i++) {
				int jumpedPawnX = (c.attackPath.get(i)[0] + c.attackPath.get(i+1)[0])/2;
				int jumpedPawnY = (c.attackPath.get(i)[1] + c.attackPath.get(i+1)[1])/2;
				Game.boardState[jumpedPawnX][jumpedPawnY] = 0;
				System.out.println("Computer Player achieved a point!");
				System.out.println("-------------------------");
			}
		}
		// If it is a single move.
		else if (c.highestPotentialValue == 1) {
			finalPositionX = startPositionX+1;
			if (c.leftMovePossible == 1)
				finalPositionY = startPositionY-1;
			else if (c.rightMovePossible == 1)
				finalPositionY = startPositionY+1;
			Game.boardState[finalPositionX][finalPositionY] = Game.boardState[startPositionX][startPositionY];
			Game.boardState[startPositionX][startPositionY] = 0;
		}
		
		// If it's the last row, then a pawn (man) becomes a king.
		if (finalPositionX == 7)
			Game.boardState[finalPositionX][finalPositionY] = 2;
	}
	

	static void undo() {
		Game.movesStorage.removeLast();
	    Game.boardState = Game.movesStorage.getLast();
	    if (Game.gameMode == "human-computer")
	    	playerAMove();
	}
	
}
