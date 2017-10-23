public class Game {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int isWinner = 0;
		Board b = new Board();
		while(isWinner == 0) {
			Move.move(b);
		}
	}

}
