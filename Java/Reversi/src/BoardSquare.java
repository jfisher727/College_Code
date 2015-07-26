

public class BoardSquare {
		BoardSquare [][] board = new BoardSquare [8][8];
	
		boolean white;
		boolean black;
		boolean blank;
		int x;
		int y;
		int value = Integer.MIN_VALUE;
		
		public BoardSquare () {
			white = false;
			black = false;
			blank = true;
		}
		
		public void initializeBoard () {
			for(int i = 0; i < board.length; i++) {
				int value = 0;
				for(int x = 0; x < board[0].length; x++) {
					board [i][x] = new BoardSquare();
					board [i][x].x = i;
					board [i][x].y = x;
					if((i == 0 || i == board.length - 1) && (x == 0 || x == board[0].length - 1)) {
						value = 5;
					}
					else if ((i == 0 || i == board.length - 1) || (x == 0 || x == board[0].length - 1)) {
						value = 3;
					}
					else if ((i == 1 || i == board.length - 2) || (x == 1 || x == board[0].length - 2)) {
						value = -3;
					}
					else {
						value = 1;
					}
					board [i][x].value = value;
					if(i == 3 && x == 3) {
						board [i][x].blank = false;
						board [i][x].white = true;
					}
					else if (i == 3 && x == 4) {
						board [i][x].blank = false;
						board [i][x].black = true;
					}
					else if(i == 4 && x == 4) {
						board [i][x].blank = false;
						board [i][x].white = true;
					}
					else if (i == 4 && x == 3) {
						board [i][x].blank = false;
						board [i][x].black = true;
					}
				}
			}
		}
		
		public void printBoard () {
			String rowDivider = "";
			for(int i = 0; i < board.length; i++) {
				String currentRow = "|";
				rowDivider = "-";
				for(int x = 0; x < board[i].length; x++) {
					BoardSquare currentSquare = board [i][x];
					rowDivider += "----";
					if(currentSquare.black) {
						currentRow += " b |";
						//currentRow += currentSquare.value +	") |";
					}
					else if (currentSquare.white) {
						currentRow += " w |";
						//currentRow += currentSquare.value + ") |";
					}
					else {
						currentRow += "   |";
						//currentRow += currentSquare.value +	") |";
					}
				}
				System.out.println(rowDivider);
				System.out.println(currentRow);
			}
			System.out.println(rowDivider);
		}
		
		public void determineWinner () {
			int whitePieces = 0;
			int blackPieces = 0;
			for(int x = 0; x < board.length; x++) {
				for(int y = 0; y < board[0].length; y++) {
					if(board[x][y].white && !board[x][y].blank) {
						whitePieces++;
					}
					else if(board[x][y].black && !board[x][y].blank) {
						blackPieces++;
					}
				}
			}
			if(whitePieces > blackPieces) {
				System.out.println("User wins!");
			}
			else if(blackPieces > whitePieces) {
				System.out.println("AI wins!");
			}
			else {
				System.out.println("Tie!");
			}
		}
		
	}
