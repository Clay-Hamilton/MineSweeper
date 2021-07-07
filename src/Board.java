import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;

public class Board extends JFrame {
	private int xlength;
	private int ylength;
	private int numMines;
	Cell[][] realBoardArray;
	char[][] playerBoardArray;
	//	int[][] minesNearBoardArray;
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
				System.out.print(getMinesNear(i,j) + " ");
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

	}

	public int getMinesNear(int x, int y) {
		int count = 0;

		//check for edge of board:
		//upper left:
		if (isLegalMove(x-1, y-1)) {
			if (realBoardArray[x-1][y-1].isMine()) count++;
		}
		//upper middle:
		if (isLegalMove(x, y-1)) {
			if (realBoardArray[x][y-1].isMine()) count++;
		}
		//you get the point:
		if (isLegalMove(x+1, y-1)) {
			if (realBoardArray[x+1][y-1].isMine()) count++;
		}
		if (isLegalMove(x-1, y)) {
			if (realBoardArray[x-1][y].isMine()) count++;
		}
		if (isLegalMove(x+1, y)) {
			if (realBoardArray[x+1][y].isMine()) count++;
		}
		if (isLegalMove(x-1, y+1)) {
			if (realBoardArray[x-1][y+1].isMine()) count++;
		}
		if (isLegalMove(x, y+1)) {
			if (realBoardArray[x][y+1].isMine()) count++;
		}
		if (isLegalMove(x+1, y+1)) {
			if (realBoardArray[x+1][y+1].isMine()) count++;
		}
		return count;
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

	public boolean isLegalMove(int x, int y) {
		if (x < 0 || y < 0 || x >= xlength || y >= ylength) return false;

		return true;
	}


	public void Play (Board board) {
		int xresponse = 0;
		int yresponse = 0;
		int squaresleft = xlength * ylength - numMines;
		String response = "";
		String[] responses = new String[2];
		boolean gameWon = false;
		boolean firstMove = true;
		/*
		System.out.println("Enter Dimensions:");
		int x = scan.nextInt();
		System.out.println("Enter Number of Mines");
		int mines = scan.nextInt();
		 */
		while(!gameWon) {
			board.printPlayerBoard();

			//scan for player's entered move:
			System.out.println("Select an X and Y coordinate, surrounded by a space.");
			response = scan.nextLine();
			responses = response.split(" ");
			xresponse = Integer.parseInt(responses[0]);
			yresponse = Integer.parseInt(responses[1]);

			//check if move is legal:
			if (!isLegalMove(xresponse, yresponse)) {
				System.out.println("Illegal Move: Keep your answer between 0 and " + xlength + " for the first number and 0 and " + ylength + " for the second!");
				continue;
			}

			//check if the selected square was already picked:
			if(board.playerBoardArray[xresponse][yresponse] != 'X') {
				System.out.println("You already picked that square!");
				continue;
			}


			//if the player hits a mine, game over
			if (board.realBoardArray[xresponse][yresponse].isMine()) {
				//if it's the first move, move the mine so they don't die immediately:
				if (squaresleft == xlength * ylength - numMines) {
					board.realBoardArray[xresponse][yresponse].setMine(false);
					genMines(1);
				}
				else { //otherwise they die:
					board.playerBoardArray[xresponse][yresponse] = '*';
					board.printPlayerBoard();
					System.exit(0);
				}
			}
			board.playerBoardArray[xresponse][yresponse] = Character.forDigit(getMinesNear(xresponse, yresponse), 10);

			//check for winning condition:
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
				System.exit(0);
			}

		}


	}
}


