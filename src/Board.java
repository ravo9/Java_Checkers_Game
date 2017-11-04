

public class Board{
	
	static void newBoard(){
		
		Game.boardState = new int[8][8];
		putPawns();
		Movement.storeMove();
	}
	
	
	static void putPawns() {

		// Fill the empty board by zeros.
		for (int i=0; i<8; i++) {
			for (int k=0; k<8; k++) 
				Game.boardState[i][k] = 0;
		}
		
		// Put the pawns on the board.
		// -1 indicates White, A player, 1 indicates Black, B player, 0 indicates an empty field,
		// -2 indicates White A King, 2 indicates Black B King
		int odd[] = {0, 2, 4, 6};
		int even[] = {1, 3, 5, 7};
		
		for (int el: odd) {
			Game.boardState[0][el] = 1;
			Game.boardState[2][el] = 1;
		}
		for (int el: even) 
			Game.boardState[1][el] = 1;
		for (int el: odd) 
			Game.boardState[6][el] = -1;
		for (int el: even) {
			Game.boardState[5][el] = -1;
			Game.boardState[7][el] = -1;
		}	
	}
	
	
	static void printBoard(){
		for (int i=0; i<8; i++) {
			String row = "";
			for (int k=0; k<8; k++) {
				if (Game.boardState[i][k] == 0)
					row += "[ ]";
				else if (Game.boardState[i][k] == 1)
					row += "[b]";
				else if (Game.boardState[i][k] == -1)
					row += "[a]";
				else if (Game.boardState[i][k] == -2)
					row += "[A]";
				else if (Game.boardState[i][k] == 2)
					row += "[B]";
			}
			row += " " + (i+1);
			System.out.println(row);
		}
		System.out.println(" A  B  C  D  E  F  G  H");
	}
}