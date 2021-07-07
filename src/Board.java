import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;

public class Board extends JFrame {
	private int xlength;
	private int ylength;
	private int numMines;
	Cell[][] realBoardArray;
	char[][] playerBoardArray;
	int[][] minesNearBoardArray;
	Scanner scan = new Scanner(System.in);
	Random gen = new Random();

	public void printRealBoard() {
		for (int i = 0; i < xlength; i++) {
			for (int j = 0; j < ylength; j++) {
				if(realBoardArray[i][j].isMine()) {
					System.out.print(1 + " ");
				}
				else {
					System.out.print(0 + " ");
				}
			}
			System.out.println();
		}
	}
	public void printPlayerBoard() {
		for (int i = 0; i < xlength; i++) {
			for (int j = 0; j < ylength; j++) {
				System.out.print(playerBoardArray[i][j] + " ");
			}
			System.out.println();
		}
	}
	public void printMinesNear() {
		for (int i = 0; i < xlength; i++) {
			for (int j = 0; j < ylength; j++) {
				System.out.print(realBoardArray[i][j].getMinesNear() + " ");
			}
			System.out.println();
		}
	}


	public Board(int x, int y, int mines) {
		xlength = x;
		ylength = y;
		numMines = mines;
		realBoardArray = new Cell[x][y];
		playerBoardArray = new char[x][y];
		minesNearBoardArray = new int[x][y];
		//generate board places for mines:
		for (int i = 0; i < realBoardArray.length; i++) {
			for (int j = 0; j < realBoardArray.length; j++) {
				realBoardArray[i][j] = new Cell(false);
			}
		}
		genMines(mines);
		//hardcode mines for now:
//		realBoardArray[0][0].setMine(true);
//		realBoardArray[1][1].setMine(true);
//		realBoardArray[2][2].setMine(true);
//		realBoardArray[3][3].setMine(true);
//		realBoardArray[4][4].setMine(true);

		//generate board that player sees:
		for (int i = 0; i < playerBoardArray.length; i++) {
			for (int j = 0; j < playerBoardArray.length; j++) {
				playerBoardArray[i][j] = 'X';
			}
		}

		//generate minesNear for each Cell:
		for (int i = 0; i < xlength; i++) {
			for (int j = 0; j < ylength; j++) {
				int count = 0;
				
				//check for edge of board:
				//upper left:
				if (i-1 >= 0 && j-1 >= 0) {
					if (realBoardArray[i-1][j-1].isMine()) count++;
				}
				//upper middle:
				if (j-1 >= 0) {
					if (realBoardArray[i][j-1].isMine()) count++;
				}
				//you get the point:
				if (i+1 < xlength && j-1 >= 0) {
					if (realBoardArray[i+1][j-1].isMine()) count++;
				}
				if (i-1 >= 0) {
					if (realBoardArray[i-1][j].isMine()) count++;
				}
				if (i+1 < xlength) {
					if (realBoardArray[i+1][j].isMine()) count++;
				}
				if (i-1 >= 0 && j+1 < ylength) {
					if (realBoardArray[i-1][j+1].isMine()) count++;
				}
				if (j+1 < ylength) {
					if (realBoardArray[i][j+1].isMine()) count++;
				}
				if (i+1 < xlength && j+1 < ylength) {
					if (realBoardArray[i+1][j+1].isMine()) count++;
				}
				realBoardArray[i][j].setMinesNear(count);
			}
		}

	}
	public void genMines(int numMines) {
		int randomx = 0;
		int randomy = 0;
		for (int i = numMines; i > 0; i--) {
			randomx = gen.nextInt(xlength);
			randomy = gen.nextInt(ylength);
			if(!realBoardArray[randomx][randomy].isMine()) {
				realBoardArray[randomx][randomy].setMine(true);
			}
			else i++;
		}
	}
	
	public boolean isLegalMove(int num, boolean isX) {
		int length;
		if (isX) length = xlength;
		else length = ylength;
		
		if (num < 0 || num >= length) return false;
		
		return true;
	}


	public void Play (Board board) {
		int xresponse = 0;
		int yresponse = 0;
		int squaresleft = xlength * ylength - numMines;
		boolean gameWon = false;
		/*
		System.out.println("Enter Dimensions:");
		int x = scan.nextInt();
		System.out.println("Enter Number of Mines");
		int mines = scan.nextInt();
		 */
		while(!gameWon) {
			board.printPlayerBoard();
			System.out.println("Select an X Coordinate (starting at 0, of course :) ):");
			xresponse = scan.nextInt();
			if (!isLegalMove(xresponse, true)) {
				System.out.println("Illegal Move: Keep your answer within bounds!");
				continue;
			}
			System.out.println("Select a Y Coordinate:");
			yresponse = scan.nextInt();
			if (!isLegalMove(yresponse, false)) {
				System.out.println("Illegal Move: Keep your answer within bounds!");
				continue;
			}
			//check if the selected square was already picked:
			if(board.playerBoardArray[xresponse][yresponse] != 'X') {
				System.out.println("You already picked that square!");
				continue;
			}

			if (board.realBoardArray[xresponse][yresponse].isMine()) {
				board.playerBoardArray[xresponse][yresponse] = '*';
				board.printPlayerBoard();
				System.exit(0);
			}
			else {
				System.out.println(board.realBoardArray[xresponse][yresponse].getMinesNear());
				board.playerBoardArray[xresponse][yresponse] = Character.forDigit(board.realBoardArray[xresponse][yresponse].getMinesNear(), 10);
			}
			squaresleft--;
			if (squaresleft == 0) {
				gameWon = true;
				
				//reveal all mines:
				for (int i = 0; i < xlength; i++) {
					for (int j = 0; j < ylength; j++) {
						if(board.playerBoardArray[i][j] == 'X') board.playerBoardArray[i][j] = '*';
					}
				}
				
				board.printPlayerBoard();
				System.out.println("You Win!");
			}
			
		}


	}
}


