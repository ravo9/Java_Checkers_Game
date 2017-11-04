import java.util.LinkedList;

public class Game {

	static int[][] boardState;
	
	// This flag indicates when the move is a second or further stage of multiple jump.
	static int multipleJumpFlag = 0;
	static int[] multipleJumpPawnBuffor = new int[2];
	
	// Two possible game modes: "human-computer" and "human-human".
	static String gameMode = "human-computer";
	static LinkedList<int[][]> movesStorage = new LinkedList<int[][]>();
	
	
	public static void main(String[] args) {
		
		Board.newBoard();
		int isWinner = 0;
		
		// Human-computer game
		
		while(isWinner == 0) {
			Movement.playerAMove();
			Movement.playerComputerMove();
		}
		
		// Human-human game
		/*while(isWinner == 0) {
			Movement.playerAMove();
			Movement.playerBMove();
		}		*/
	}
}
