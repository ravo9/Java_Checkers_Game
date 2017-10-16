public class Game {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int isWinner = 0;
		Board b = new Board();
		while(isWinner == 0) {
			Move.move(b);
		}
		//b.movement("D6", "E5", 5);
		//b.movement("3C", "D4", -5);
		//b.movement("E5", "C3", 5);
	}

}
