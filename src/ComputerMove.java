import java.util.LinkedList;
import java.util.Random;

public class ComputerMove {
	
	// Start and final positions.
	public int xyA[] = new int[2];
	
	// A potential multiple jump path.
	public LinkedList<int[]> attackPath = new LinkedList<int[]>();
	
	int highestPotentialValue = 0;
	
	// Flags indicating move possibilities.
	int rightMovePossible = 0;
	int leftMovePossible = 0;
	
	ComputerMove(){	
	
		for (int i=0; i<8; i++) {
			for (int k=0; k<8; k++) {
				if (Game.boardState[i][k] == 1) {
					
					int thisPawnValue = 0;
					int thisRightMovePossible = 0;
					int thisLeftMovePossible = 0;
					LinkedList<int[]> thisPawnAttackPath = new LinkedList<int[]>();
					
					// Is there any jump opportunity?
					// Only for medium and hard level.
					if (Game.level == "medium" || Game.level == "hard")
						thisPawnAttackPath = checkAttackPotential(i, k);
					
					// Is there any single move opportunity?
					try {
						if (Game.boardState[i+1][k-1] == 0)
							thisLeftMovePossible = 1;
					}
					catch (Exception e) {}
					
					try {
						if (Game.boardState[i+1][k+1] == 0)
							thisRightMovePossible = 1;
					}
					catch (Exception e) {}
				
					// What is the final value of this pawn? 3 points per each attack, 1 point per single move.
					if (thisPawnAttackPath.size() > 1)
						thisPawnValue = thisPawnAttackPath.size() * 3 - 3;
						
					else if (thisLeftMovePossible == 1 || thisRightMovePossible == 1)
						thisPawnValue = 1;
					
					// Consider if the pawn is going to move onto position in front of the enemy pawn.
					// Only for hard level.
					// The computer will prefer a risky attack over the single move, but among different attacks will prefer
					// the safest one.
					// There is a rule that the player has to attack, if the attack is possible.
					if (Game.level == "hard") {
						int lastX = thisPawnAttackPath.getFirst()[0];
						int lastY = thisPawnAttackPath.getFirst()[1];
						int dangerousPosition = 0;
						
						try {
							if (Game.boardState[lastX+1][lastY-1] == -1)
								dangerousPosition = 1;
						}
						catch (Exception e) {}
						try {
							if (Game.boardState[lastX+1][lastY+1] == -1)
								dangerousPosition = 1;
						}
						catch (Exception e) {}
						
						if (dangerousPosition == 1)
							thisPawnValue -= 0.9;
					}
					
					// If this pawn is better than the previous best one, set it as the best one pawn.
					if (thisPawnValue > highestPotentialValue) {
						highestPotentialValue = thisPawnValue;
						xyA[0] = i;
						xyA[1] = k;
						if (highestPotentialValue > 1)
							attackPath = thisPawnAttackPath;
						else {
							rightMovePossible = thisRightMovePossible;
							leftMovePossible = thisLeftMovePossible;
						}
					}	
					// If this pawn is equally good as the previous best one, use a random number generator
					// to decide if it should be set as the best one pawn or not.
					// It allows computer to choose a random pawn (among equally good pawns) instead
					// of taking always the first one.
					else if (thisPawnValue == highestPotentialValue) {
						Random generator = new Random(); 
						int decision = generator.nextInt(2);
						if (decision == 1) {
							highestPotentialValue = thisPawnValue;
							xyA[0] = i;
							xyA[1] = k;
							if (highestPotentialValue > 1)
								attackPath = thisPawnAttackPath;
							else {
								rightMovePossible = thisRightMovePossible;
								leftMovePossible = thisLeftMovePossible;
							}
						}
					}	
				}
			}
		}
	}
	
	
	static LinkedList<int[]> checkAttackPotential(int x, int y) {
		
		int currentPosition[] = new int[2];
		currentPosition[0] = x;
		currentPosition[1] = y;
		
		LinkedList<int[]> tempLeftAttackPath = new LinkedList<int[]>();
		LinkedList<int[]> tempRightAttackPath = new LinkedList<int[]>();
		
		try {
			if (Game.boardState[x+1][y-1] == -1 && Game.boardState[x+2][y-2] == 0) {
				tempLeftAttackPath = checkAttackPotential(x+2, y-2);
				tempLeftAttackPath.add(currentPosition);
			}
			else
				tempLeftAttackPath.add(currentPosition);
		}
		catch (Exception e) {
			tempLeftAttackPath.add(currentPosition);
		}
		
		try {
			if (Game.boardState[x+1][y+1] == -1 && Game.boardState[x+2][y+2] == 0) {
				tempRightAttackPath = checkAttackPotential(x+2, y+2);
				tempRightAttackPath.add(currentPosition);
			}
			else
				tempRightAttackPath.add(currentPosition);
		}
		catch (Exception e) {
			tempRightAttackPath.add(currentPosition);
		}		
				
		// Proceed only longer list (left or right). Longer list means more jumps.
		if (tempLeftAttackPath.size() >= tempRightAttackPath.size()) 
			return tempLeftAttackPath;
		else 
			return tempRightAttackPath;		
	}
}
