public class Board{
	
	int[][] boardState;
	
	Board(){
		boardState = new int[8][8];
		
		// Fill the empty board by zeros.
		for (int i=0; i<8; i++) {
			for (int k=0; i<8; i++) {
				boardState[i][k] = 0;
			}
		}
		
		// Put the pawns on the board.
		int odd[] = {0, 2, 4, 6};
		int even[] = {1, 3, 5, 7};
		
		for (int el: odd) {
			boardState[0][el] = 1;
			boardState[2][el] = 1;
		}
		for (int el: even) {
			boardState[1][el] = 1;
		}
		for (int el: odd) {
			boardState[6][el] = -1;
		}
		for (int el: even) {
			boardState[5][el] = -1;
			boardState[7][el] = -1;
		}
	}
	
	void printBoard(){
		for (int i=0; i<8; i++) {
			String row = "";
			for (int k=0; k<8; k++) {
				if (boardState[i][k] == 0)
					row += "[ ]";
				else if (boardState[i][k] == 1)
					row += "[B]";
				else
					row += "[A]";
			}
			row += " " + (i+1);
			System.out.println(row);
		}
		System.out.println(" A  B  C  D  E  F  G  H");
	}
	
	// The function which takes a user's designation of a position (e.g. "A1" or "2B")
	// and transforms it into arrays coords.
	int[] transformDesignation(String pos) {
		int xy[] = new int[2];
		
		for (int i=0; i<8; i++) {
			if (pos.contains(Integer.toString(i))) xy[0] = i - 1;
		}
		
		String letters[] = {"A", "B", "C", "D", "E", "F", "G", "H"};
		int acc = 0;
		for (String el: letters) {
			if (pos.contains(el) || pos.contains(el.toLowerCase())) xy[1] = acc;
			acc++;
		}
		
		return xy;
	}
	
	void checkMoveValidity(int xyA[], int xyB[], int turn){
		// White player (A) turn.
		if (turn == 5) {
			// Movement along X or Y axis.
			if (xyA[0] == xyB[0] || xyA[1] == xyB[1])
				System.out.println("Wrong move (movement along X or Y axis)!");
			// Movement onto an occupied place.
			if (boardState[xyB[0]][xyB[1]] != 0)
				System.out.println("Wrong move (occupied place)!");
			// The dest. position is out of the board.
			if (((xyB[0] > 7) || (xyB[0] < 0)) || ((xyA[0] > 7) || (xyA[0] < 0)))
				System.out.println("Wrong move (out of the board)!");
		}
	}
	
	void movement(String positionA, String positionB, int turn) {
		int xyA[] = new int[2];
		int xyB[] = new int[2];
		
		xyA = transformDesignation(positionA);
		xyB = transformDesignation(positionB);
		
		checkMoveValidity(xyA, xyB, turn);
		
		if (turn == 5) {
			boardState[xyA[0]][xyA[1]] = 0;
			boardState[xyB[0]][xyB[1]] = -1;
		}
		else {
			boardState[xyA[0]][xyA[1]] = 0;
			boardState[xyB[0]][xyB[1]] = 1;
		}
		
		printBoard();
	}
	
}