import java.util.LinkedList;

public class AI {

	
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
			
			// If it is the final position of potential multijump - check if the pawn can be attacked by opponent.
			if (acc == 0) {
					
				// Left side.
				try {
					if (b.boardState[x-1][y-1] == 1 && b.boardState[x+1][y+1] == 0)
						acc -= 2;
				}
				catch (Exception e) {}
					
				// Right side.
				try {
					if (b.boardState[x-1][y+1] == 1 && b.boardState[x+1][y-1] == 0)
						acc -= 2;
				}
				catch (Exception e) {}
				}	
				
			return acc;
		}
	
}
