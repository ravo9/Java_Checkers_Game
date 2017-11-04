import java.util.LinkedList;

public class Game {

	static int[][] boardState;
	static LinkedList<int[][]> movesStorage = new LinkedList<int[][]>();
	
	
	public static void main(String[] args) {
		
		Board.newBoard();
		int isWinner = 0;
		
		while(isWinner == 0) {
			Movement.playerAMove();
			Movement.playerComputerMove();
		}
	}
}
