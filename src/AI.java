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
	
	// Multiple jumps!?
	// It has to be taken into account.
	static void bestAttackPower(Board b) {
		
		
		int biggestPower = -1;
		int bestPawnRow = -1;
		int bestPawnCol = -1;
		
		//LinkedList<int[]> allPawnPowers = new LinkedList<int[]>();
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
						if (b.boardState[i-1][k-1] == 1 && b.boardState[i-2][k-2] == 0)
							pawnPower[4] = 2;
					}
					catch (Exception e) {
							pawnPower[4] = 0;
					}
					
					try {
						if (b.boardState[i-1][k+1] == 1 && b.boardState[i-2][k+2] == 0)
							pawnPower[5] = 2;
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
		
		
		// Now I should get the coordinates of the first pawn with the value equals
		// to the biggest attack power.
		System.out.println(bestPawnRow + ", " + bestPawnCol + ":  power: " + biggestPower);
		
		
	}
	
	
}
