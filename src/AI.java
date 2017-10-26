import java.util.LinkedList;

public class AI {

	static void stateValue(Board b) {
		
		int valueA = 0;
		int valueB = 0;
		
		// Count the value - a pawn equals 1 point, a king - 2 points.
		for (int i=0; i<8; i++) {
			for (int k=0; k<8; k++) {
				if (b.boardState[i][k] == -1)
					valueA += 1;
				else if (b.boardState[i][k] == -2)
					valueA += 2;
				else if (b.boardState[i][k] == 1) 
					valueB += 1;
				else if (b.boardState[i][k] == 2)
					valueB += 2;
			}
		}
		
		System.out.println("\nA state: "+valueA);
		System.out.println("B state: "+valueB+"\n");
	}

	
	// A function that returns an attack best path (position by position).
	static LinkedList<int[]> attackPathFinder(Board b, int x, int y) {
		
		LinkedList<int[]> pathLeftFront = new LinkedList<int[]>();
		LinkedList<int[]> pathRightFront = new LinkedList<int[]>();

		int thisPos[] = new int[2];
		thisPos[0] = x;
		thisPos[1] = y;		
		
		try {
			// There is an attack opportunity on the left front-side.
			if (b.boardState[x-1][y-1] == 1 && b.boardState[x-2][y-2] == 0) { 
				pathLeftFront = attackPathFinder(b, x-2, y-2);
				pathLeftFront.add(thisPos);
			}
			else
				pathLeftFront.add(thisPos);
		}
		catch (Exception e) {
				pathLeftFront.add(thisPos);
		}
		
		
		// Right side check.
		try {
			// There is an attack opportunity on the right front-side.
			if (b.boardState[x-1][y+1] == 1 && b.boardState[x-2][y+2] == 0) { 
				pathRightFront = attackPathFinder(b, x-2, y+2);
				pathRightFront.add(thisPos);
			}
			else
				pathRightFront.add(thisPos);
		}
		catch (Exception e) {
				pathRightFront.add(thisPos);
		}
		
		// Proceed only the longer list (left or right). Longer list means more jumps.
		if (pathLeftFront.size() >= pathRightFront.size()) 
			return pathLeftFront;
		else 
			return pathRightFront;
	}
	
	
	// A function that returns a number and coordinates of the pawn, which has the biggest attack potential.
	// This function also assigns a path of the attack to the board's array 'computerAttackPath'.
	static int[] bestAttackPower(Board b) {
		
		int biggestPower = -1;
		int bestPawnRow = -1;
		int bestPawnCol = -1;
		
		int[] results = new int[3];
		int[] pawnPower = new int[6];
		
		
		for (int i=0; i<8; i++) {
			for (int k=0; k<8; k++) {
				// A player's pawn.
				if (b.boardState[i][k] == -1) {
				
					pawnPower[0] = i;
					pawnPower[1] = k;
					
					try {
						if (b.boardState[i-1][k-1] == 0)
							pawnPower[2] = 1;
					}
					catch (Exception e) {
							pawnPower[2] = 0;
					}
							
					try {
						if (b.boardState[i-1][k+1] == 0)
							pawnPower[3] = 1;
					}
					catch (Exception e) {
							pawnPower[3] = 0;
					}
					
					try {
						// There is an attack opportunity on the left front-side.
						if (b.boardState[i-1][k-1] == 1 && b.boardState[i-2][k-2] == 0) {
							pawnPower[4] = 2;
							// Check if it is a multijump.
							pawnPower[4] += checkAttackPotential(b, i-2, k-2);
						}	
					}
					catch (Exception e) {
							pawnPower[4] = 0;
					}
					
					try {
						// There is an attack opportunity on the right front-side.
						if (b.boardState[i-1][k+1] == 1 && b.boardState[i-2][k+2] == 0) {
							pawnPower[5] = 2;
							// Check if it is a multi jump.
							pawnPower[5] += checkAttackPotential(b, i-2, k+2);
						}
					}
					catch (Exception e) {
							pawnPower[5] = 0;
					}
					
					
					// Now I have to find out if does this pawn have a higher attack power then 
					// the previous one.
					
					for (int l=2; l<6; l++) {
						if (pawnPower[l] > biggestPower) {
							biggestPower = pawnPower[l];
							bestPawnRow = pawnPower[0];
							bestPawnCol = pawnPower[1];
						}
					}
					
				}
			}
		}
		
		// If the attack is possible.
		if (biggestPower > 1) {
			b.computerAttackPath = attackPathFinder(b, bestPawnRow, bestPawnCol);
		}
		// If the attack is not possible, but the move into left side is.
		else if (bestPawnCol != 0 && b.boardState[bestPawnRow - 1][bestPawnCol - 1] == 0) {
			int[] nextMove = new int[2];
			nextMove[0] = bestPawnRow - 1;
			nextMove[1] = bestPawnCol - 1;
			b.computerAttackPath.addFirst(nextMove);	
		}
		// If the attack and move into left side are not possible, but the move into right side is.
		else if (bestPawnCol != 7 && b.boardState[bestPawnRow - 1][bestPawnCol + 1] == 0) {
			int[] nextMove = new int[2];
			nextMove[0] = bestPawnRow - 1;
			nextMove[1] = bestPawnCol + 1;
			b.computerAttackPath.addFirst(nextMove);	
		}
		
		results[0] = bestPawnRow;	
		results[1] = bestPawnCol;
		results[2] = biggestPower;
		
		return results;
		
	}
	
	
	// A function that returns a value of the attack potential.
	static int checkAttackPotential(Board b, int x, int y) {
	
	int acc = 0;
	
		// Only A player is taken into account now.
			
			try {
				if (b.boardState[x-1][y-1] == 1 && b.boardState[x-2][y-2] == 0) {
					acc += 2;
					acc += checkAttackPotential(b, x-2, y-2);
				}
			}
			catch (Exception e) {}
			
			try {
			    if (b.boardState[x-1][y+1] == 1 && b.boardState[x-2][y+2] == 0) {
					acc += 2;
					acc += checkAttackPotential(b, x-2, y+2);
				}
			}
			catch (Exception e) {}
		
		return acc;
	}
	
	// A function that returns a value of the attack potential and also takes an opponent's potential attack into account.
		static int checkAttackPotentialCarefully(Board b, int x, int y) {
		
		int acc = 0;
		
			// Only A player is taken into account now.
				
				try {
					if (b.boardState[x-1][y-1] == 1 && b.boardState[x-2][y-2] == 0) {
						acc += 2;
						acc += checkAttackPotentialCarefully(b, x-2, y-2);
					}
				}
				catch (Exception e) {}
				
				try {
				    if (b.boardState[x-1][y+1] == 1 && b.boardState[x-2][y+2] == 0) {
						acc += 2;
						acc += checkAttackPotentialCarefully(b, x-2, y+2);
					}
				}
				catch (Exception e) {}
			
			return acc;
		}
	
}
