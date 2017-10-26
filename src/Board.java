import java.util.LinkedList;

public class Board{
	
	int[][] boardState;
	int turn;
	
	public LinkedList<int[]> computerAttackPath = new LinkedList<int[]>();
	static LinkedList<int[][]> movesStorage = new LinkedList<int[][]>();
	
	Board(){
		boardState = new int[8][8];
		
		// 5 means that it is the A player move (-1 in the boardstate)
		turn = 5;
		
		// Fill the empty board by zeros.
		for (int i=0; i<8; i++) {
			for (int k=0; k<8; k++) 
				boardState[i][k] = 0;
		}
		
		// Put the pawns on the board.
		// -1 indicates White, A player, 1 indicates Black, B player, 0 indicates an empty field,
		// -2 indicates White A King, 2 indicates Black B King
		int odd[] = {0, 2, 4, 6};
		int even[] = {1, 3, 5, 7};
		
		for (int el: odd) {
			boardState[0][el] = 1;
			boardState[2][el] = 1;
		}
		for (int el: even) 
			boardState[1][el] = 1;
		for (int el: odd) 
			boardState[6][el] = -1;
		for (int el: even) {
			boardState[5][el] = -1;
			boardState[7][el] = -1;
		}
		
		// Store empty board as the first state.
		Move.storeMove(this);
	}
	
	
	void printBoard(){
		for (int i=0; i<8; i++) {
			String row = "";
			for (int k=0; k<8; k++) {
				if (boardState[i][k] == 0)
					row += "[ ]";
				else if (boardState[i][k] == 1)
					row += "[b]";
				else if (boardState[i][k] == -1)
					row += "[a]";
				else if (boardState[i][k] == -2)
					row += "[A]";
				else if (boardState[i][k] == 2)
					row += "[B]";
			}
			row += " " + (i+1);
			System.out.println(row);
		}
		System.out.println(" A  B  C  D  E  F  G  H");
	}
}