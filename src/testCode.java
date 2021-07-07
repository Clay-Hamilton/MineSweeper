import java.util.Scanner;

public class testCode {
	Scanner scan = new Scanner(System.in);
	
	
	public static void main(String[] args){
		Board board = new Board(3, 3, 1);
		//board.printRealBoard();
		//board.printPlayerBoard();
		//board.printMinesNear();
		board.Play(board);
	}
	

}


