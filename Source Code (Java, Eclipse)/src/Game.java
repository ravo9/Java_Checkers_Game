import java.util.LinkedList;
import java.util.Scanner;

public class Game {

	static int[][] boardState;
	
	// This flag indicates when the move is a second or further stage of multiple jump.
	static int multipleJumpFlag = 0;
	static int[] multipleJumpPawnBuffor = new int[2];
	
	// Two possible game modes: "human-computer" and "human-human".
	static String gameMode = "human-computer";
	
	static LinkedList<int[][]> movesStorage = new LinkedList<int[][]>();
	static LinkedList<int[][]> revokedMovesStorage = new LinkedList<int[][]>();
	
	// Levels: 'easy', 'medium', 'hard'.
	static String level = "";
	
	// Modes: '1' indicates human-human game, '2' indicates human-computer game.
	static int mode = 0;
	
	// Variable indicating if the game has been finished.
	static String isWinner = "noWinner";
	
	
	public static void gameMenu() {
		
		Scanner scan0 = new Scanner(System.in);
		String input = "";
		System.out.println("Welcome to 'Checkers V2 3D 4K'!");
		System.out.println("Before we'll start a game, please choose a game mode:");
		
		while (mode == 0) {
			System.out.println("Press '1' to play against another human player,");
			System.out.println(" or press '2' to play against a computer player.");
			input = scan0.nextLine();
			if (input.equals("1"))
				mode = 1;
			else if (input.equals("2"))
				mode = 2;
			else
				System.out.println("Input error. Please try again to choose the game mode again.");
		}
		
		if (mode == 2) {
		
			System.out.println("Please choose a difficulty level:");
			
			while (level == "") {
				System.out.println("Please type 'easy', 'medium' or 'hard'.");
				input = scan0.nextLine();
				if (input.equals("easy"))
					level = "easy";
				else if (input.equals("medium"))
					level = "medium";
				else if (input.equals("hard"))
					level = "hard";
				else
					System.out.println("Input error. Please try again to choose the difficulty level.");
			}
		}
		
		System.out.println("Well done! Let's start the game!!!\n");
		System.out.println("-------------------------\n");
		
		Board.newBoard();
		
		
		// Human-human game
		if (mode == 1) {
			while(true) {
				
				isWinner = Board.checkIfEnd();
				if (isWinner == "noWinner")
					Movement.playerAMove();
				else
					break;
				
				isWinner = Board.checkIfEnd();
				if (isWinner == "noWinner")
					Movement.playerBMove();
				else
					break;
			}
			
			if (isWinner == "winnerA")
				System.out.println("PLAYER A WON! CONGRATULATIONS!!!");
			else if (isWinner == "winnerB")
				System.out.println("PLAYER B WON! CONGRATULATIONS!!!");
			
			System.out.println("Thank you for the game! ;)");	
		}
		
		
		// Human-computer game
		if (mode == 2) {
			while(true) {
				
				isWinner = Board.checkIfEnd();
				if (isWinner == "noWinner")
					Movement.playerAMove();
				else
					break;
				
				isWinner = Board.checkIfEnd();
				if (isWinner == "noWinner")
					Movement.playerComputerMove();
				else
					break;
			}
			
			if (isWinner == "winnerA")
				System.out.println("PLAYER A WON! CONGRATULATIONS!!!");
			else if (isWinner == "winnerB")
				System.out.println("PLAYER B WON! CONGRATULATIONS!!!");
			
			System.out.println("Thank you for the game! ;)");
		}		
	}
	
	
	// A function that replays recorde game.
	public static void replay() {
		Scanner scan0 = new Scanner(System.in);
		for (int i=0; i<Game.movesStorage.size(); i++) {
			Game.boardState = Game.movesStorage.get(i);
			Board.printBoard();
			System.out.println("\nPress ENTER to proceed.\n");
			scan0.nextLine();
		}
		System.out.println("-------------------------\n");
		System.out.println("Please continue the game.\n");
	}
	
	
	public static void main(String[] args) {
		
		gameMenu();

	}
}
