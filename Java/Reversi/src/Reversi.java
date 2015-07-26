import java.util.ArrayList;
import java.util.Scanner;

public class Reversi {
	static BoardSquare board [][];
	
	public static boolean gameComplete () {
		for(int i = 0; i < board.length; i++) {
			for(int x = 0; x < board[0].length; x++) {
				if (board [i][x].blank) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static BoardSquare parseMove (final String response) {
		Scanner parser = new Scanner (response);
		parser.useDelimiter(",");
		int x = parser.nextInt() - 1;
		int y = parser.nextInt() - 1;
		parser.close();
		BoardSquare selected = null;
		if((x >= 0 && x < board.length) && (y >= 0 && y < board[0].length)) {
			selected = board [y][x];
		}
		return selected;
	}
	
	public static boolean isCorner(final int x, final int y) {
		return (x == 0 && y == 0) || (x == 0 && y == 7) || (x == 7 && y == 0) || (x == 7 && y == 7);
	}
	
	public static boolean verifyMove (final BoardSquare move) {
	    ArrayList<BoardSquare> legalMoves = new ArrayList<BoardSquare>();
	    
		for (int i = 0; i < board.length; i++){
		    for(int j = 0; j<board[i].length; j++){
		        if(board[i][j].black) {
		        	if(isCorner(i,j)) {
		        		continue;
		        	}		            
		            if((i == 0) || (i == 7) ){
		                 if ((board[i][j-1].blank)  && (board[i][j+1].blank)){
		                     continue;
		                 }
		                 else if (board[i][j-1].blank){		                     
		                     for (int count = j+1; count < 8; count++){
		                         if(board[i][count].blank){
		                             break;
		                         }
		                         else if (board[i][count].white){
		                             legalMoves.add (board[i][j-1]);
		                             break;
		                         }
		                     }
		                 }
		                 else if (board[i][j+1].blank){
		                     for (int count = j-1; count >0; count--){
                                 if(board[i][count].blank){
                                     break;
                                 }
                                 else if (board[i][count].white){
                                     legalMoves.add (board[i][j+1]);
                                     break;
                                 }
                             }
		                 }
		            }
		            else if ((j == 0) || (j == 7)){
		                if ((board[i-1][j].blank)  && (board[i+1][j].blank)){
                            continue;
                        }
                        else if (board[i-1][j].blank){
                            
                            for (int count = i+1; count < 8; count++){
                                if(board[count][j].blank){
                                    break;
                                }
                                else if (board[count][j].white){
                                    legalMoves.add (board[i-1][j]);
                                    break;
                                }
                            }
                        }
                        else if (board[i+1][j].blank){
                            for (int count = i-1; count >0; count--){
                                if(board[count][j].blank){
                                    break;
                                }
                                else if (board[count][j].white){
                                    legalMoves.add (board[i+1][j]);
                                    break;
                                }
                            }
                        }
		            }
		            else{
		                for(int row = i-1; row < i+2; row++){
		                    for(int col = j-1; col < j+2; col++){
		                        if(board[row][col].blank){
		                            if ((row == i-1) && (col == j-1)){
		                                if(board[i+1][j+1].blank){
		                                    continue;
		                                }
		                                else{
		                                    for (int count1 = i+1, count2 = j+1; count1 < 8; count1++, count2++){
		                                        if(board[count1][count2].blank){
		                                            break;
		                                        }
		                                        else if(board[count1][count2].white){
		                                            legalMoves.add (board[i-1][j-1]);
		                                            break;
		                                        }
		                                    }
		                                }
		                            }
		                            else if ((row == i-1) && (col == j)){
                                        if(board[i+1][j].blank){
                                            continue;
                                        }
                                        else{
                                            for (int count1 = i+1, count2 = j; count1 < 8; count1++){
                                                if(board[count1][count2].blank){
                                                    break;
                                                }
                                                else if(board[count1][count2].white){
                                                    legalMoves.add (board[i-1][j]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
		                            else if ((row == i-1) && (col == j+1)){
                                        if(board[i+1][j-1].blank){
                                            continue;
                                        }
                                        else{
                                            for (int count1 = i+1, count2 = j-1; count1 < 8; count1++, count2--){
                                                if(board[count1][count2].blank){
                                                    break;
                                                }
                                                else if(board[count1][count2].white){
                                                    legalMoves.add (board[i-1][j+1]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
		                            else if ((row == i) && (col == j-1)){
                                        if(board[i][j+1].blank){
                                            continue;
                                        }
                                        else{
                                            for (int count1 = i, count2 = j+1; count2 < 8; count2++){
                                                if(board[count1][count2].blank){
                                                    break;
                                                }
                                                else if(board[count1][count2].white){
                                                    legalMoves.add (board[i][j-1]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
		                            else if ((row == i) && (col == j+1)){
                                        if(board[i][j-1].blank){
                                            continue;
                                        }
                                        else{
                                            for (int count1 = i, count2 = j-1; count2 >= 0;  count2--){
                                                if(board[count1][count2].blank){
                                                    break;
                                                }
                                                else if(board[count1][count2].white){
                                                    legalMoves.add (board[i][j+1]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
		                            else if ((row == i+1) && (col == j-1)){
                                        if(board[i-1][j+1].blank){
                                            continue;
                                        }
                                        else{
                                            for (int count1 = i-1, count2 = j+1; count1 >= 0; count1--, count2++){
                                                if(board[count1][count2].blank){
                                                    break;
                                                }
                                                else if(board[count1][count2].white){
                                                    legalMoves.add (board[i+1][j-1]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
		                            else if ((row == i+1) && (col == j)){
                                        if(board[i-1][j].blank){
                                            continue;
                                        }
                                        else{
                                            for (int count1 = i-1, count2 = j; count1 >= 0; count1--){
                                                if(board[count1][count2].blank){
                                                    break;
                                                }
                                                else if(board[count1][count2].white){
                                                    legalMoves.add (board[i+1][j]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
		                            else if ((row == i+1) && (col == j+1)){
                                        if(board[i-1][j-1].blank){
                                            continue;
                                        }
                                        else{
                                            for (int count1 = i-1, count2 = j-1; count1 >=0 ; count1--, count2--){
                                                if(board[count1][count2].blank){
                                                    break;
                                                }
                                                else if(board[count1][count2].white){
                                                    legalMoves.add (board[i+1][j+1]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
		                        }
		                        else {
		                            continue;
		                        }
		                    }
		                }
		            }
		        }
		        else{
		            continue;
		        }
		    }
		}
				
		for (int i = 0 ; i < legalMoves.size (); i++){
		    BoardSquare temp = legalMoves.get (i);
		    if ((move.x == temp.x) && (move.y == temp.y)){
		        return true;
		    }
		}		
		return false;
	}
	
	public static ArrayList<BoardSquare> LegalComputerMove () {
        ArrayList<BoardSquare> legalMoves = new ArrayList<BoardSquare>();
        
        for (int i = 0; i < board.length; i++){
            for(int j = 0; j<board[i].length; j++){
                if(board[i][j].white){
                    if (((board[i][j].x == 0) && (board[i][j].y == 0))  || ((board[i][j].x == 0) && (board[i][j].y == 7))
                            || ((board[i][j].x == 7) && (board[i][j].y == 0)) || ((board[i][j].x == 7) && (board[i][j].y == 7))){
                        continue;
                    }
                    else if((board[i][j].x == 0) || (board[i][j].x == 7) ){
                         if ((board[i][j-1].blank)  && (board[i][j+1].blank)){
                             continue;
                         }
                         else if (board[i][j-1].blank){
                             
                             for (int count = j+1; count < 8; count++){
                                 if(board[i][count].blank){
                                     break;
                                 }
                                 else if (board[i][count].white){
                                     legalMoves.add (board[i][j-1]);
                                     break;
                                 }
                             }
                         }
                         else if (board[i][j+1].blank){
                             for (int count = j-1; count >0; count--){
                                 if(board[i][count].blank){
                                     break;
                                 }
                                 else if (board[i][count].black == true){
                                     legalMoves.add (board[i][j+1]);
                                     break;
                                 }
                             }
                         }
                    }
                    else if ((board[i][j].y == 0) || (board[i][j].y == 7)){
                        if ((board[i-1][j].blank)  && (board[i+1][j].blank)){
                            continue;
                        }
                        else if (board[i-1][j].blank){
                            
                            for (int count = i+1; count < 8; count++){
                                if(board[count][j].blank){
                                    break;
                                }
                                else if (board[count][j].black){
                                    legalMoves.add (board[i-1][j]);
                                    break;
                                }
                            }
                        }
                        else if (board[i+1][j].blank){
                            for (int count = i-1; count >0; count--){
                                if(board[count][j].blank){
                                    break;
                                }
                                else if (board[count][j].black){
                                    legalMoves.add (board[i+1][j]);
                                    break;
                                }
                            }
                        }
                    }
                    else{
                        for(int row = i-1; row < i+2; row++){
                            for(int col = j-1; col < j+2; col++){
                                if(board[row][col].blank){
                                    if ((row == i-1) && (col == j-1)){
                                        if(board[i+1][j+1].blank){
                                            continue;
                                        }
                                        else{
                                            for (int count1 = i+1, count2 = j+1; count1 < 8; count1++, count2++){
                                                if(board[count1][count2].blank){
                                                    break;
                                                }
                                                else if(board[count1][count2].black){
                                                    legalMoves.add (board[i-1][j-1]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    else if ((row == i-1) && (col == j)){
                                        if(board[i+1][j].blank){
                                            continue;
                                        }
                                        else{
                                            for (int count1 = i+1, count2 = j; count1 < 8; count1++){
                                                if(board[count1][count2].blank){
                                                    break;
                                                }
                                                else if(board[count1][count2].black){
                                                    legalMoves.add (board[i-1][j]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    else if ((row == i-1) && (col == j+1)){
                                        if(board[i+1][j-1].blank){
                                            continue;
                                        }
                                        else{
                                            for (int count1 = i+1, count2 = j-1; count1 < 8; count1++, count2--){
                                                if(board[count1][count2].blank){
                                                    break;
                                                }
                                                else if(board[count1][count2].black){
                                                    legalMoves.add (board[i-1][j+1]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    else if ((row == i) && (col == j-1)){
                                        if(board[i][j+1].blank){
                                            continue;
                                        }
                                        else{
                                            for (int count1 = i, count2 = j+1; count2 < 8; count2++){
                                                if(board[count1][count2].blank){
                                                    break;
                                                }
                                                else if(board[count1][count2].black){
                                                    legalMoves.add (board[i][j-1]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    else if ((row == i) && (col == j+1)){
                                        if(board[i][j-1].blank){
                                            continue;
                                        }
                                        else{
                                            for (int count1 = i, count2 = j-1; count2 >= 0;  count2--){
                                                if(board[count1][count2].blank){
                                                    break;
                                                }
                                                else if(board[count1][count2].black){
                                                    legalMoves.add (board[i][j+1]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    else if ((row == i+1) && (col == j-1)){
                                        if(board[i-1][j+1].blank){
                                            continue;
                                        }
                                        else{
                                            for (int count1 = i-1, count2 = j+1; count1 >= 0; count1--, count2++){
                                                if(board[count1][count2].blank){
                                                    break;
                                                }
                                                else if(board[count1][count2].black){
                                                    legalMoves.add (board[i+1][j-1]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    else if ((row == i+1) && (col == j)){
                                        if(board[i-1][j].blank){
                                            continue;
                                        }
                                        else{
                                            for (int count1 = i-1, count2 = j; count1 >= 0; count1--){
                                                if(board[count1][count2].blank){
                                                    break;
                                                }
                                                else if(board[count1][count2].black){
                                                    legalMoves.add (board[i+1][j]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    else if ((row == i+1) && (col == j+1)){
                                        if(board[i-1][j-1].blank){
                                            continue;
                                        }
                                        else{
                                            for (int count1 = i-1, count2 = j-1; count1 >=0 ; count1--, count2--){
                                                if(board[count1][count2].blank){
                                                    break;
                                                }
                                                else if(board[count1][count2].black){
                                                    legalMoves.add (board[i+1][j+1]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                else {
                                    continue;
                                }
                            }
                        }
                    }
                }
                else{
                    continue;
                }
            }
        }
                
        
                return legalMoves;
      
    }
	
	public static void updateSquareUser (final int x, final int y) {
		board[x][y].black = false;
		board[x][y].white = true;
		board[x][y].blank = false;
	}
	
	public static void checkSurroundingsUser (final BoardSquare selectedMove) {
		final int x = selectedMove.x;
		final int y = selectedMove.y;
		
		
		if(x >= 1 ) {
			if(y >= 1) {
				//top left
				int tempY = y - 2;
				int tempX = x - 2;
				ArrayList<BoardSquare> updateThese = new ArrayList<BoardSquare>();
				if(board[x-1][y-1].black) {
					updateThese.add(board[x-1][y-1]);
					while(tempY >= 0 && tempX >= 0 && !board[tempX][tempY].white) {
						if(board[tempX][tempY].black) {
							updateThese.add(board[tempX][tempY]);
						}
						tempY--;
						tempX--;
					}
				}
				for(int i = 0; i < updateThese.size(); i++) {
					BoardSquare current = updateThese.get(i);
					updateSquareUser(current.x, current.y);
				}
			}
			if(y <= board[0].length-1) {
				int tempX = x - 2;
				int tempY = y + 2;
				ArrayList<BoardSquare> updateThese = new ArrayList<BoardSquare>();
				//bottom left
				if(board[x-1][y+1].black) {
					updateThese.add(board[x-1][y+1]);
					while(tempX >= 0 && tempY < board[0].length && !board[tempX][tempY].white) {
						if(board[tempX][tempY].black) {
							updateThese.add(board[tempX][tempY]);
						}
						tempY++;
						tempX--;
					}
					for(int i = 0; i < updateThese.size(); i++) {
						BoardSquare current = updateThese.get(i);
						updateSquareUser(current.x, current.y);
					}
				}
			}
			//left
			int tempX = x - 2;
			int tempY = y;
			ArrayList<BoardSquare> updateThese = new ArrayList<BoardSquare>();
			if(board[x-1][y].black) {
				updateThese.add(board[x-1][y]);
				while(tempX >= 0 && tempY == y && !board[tempX][tempY].white) {
					if(board[tempX][tempY].black) {
						updateThese.add(board[tempX][tempY]);
					}
					tempX--;
				}
				for(int i = 0; i < updateThese.size(); i++) {
					BoardSquare current = updateThese.get(i);
					updateSquareUser(current.x, current.y);
				}
			}
		}
		if(y >= 1) {
			//top
			int tempX = x;
			int tempY = y - 2;
			ArrayList<BoardSquare> updateThese = new ArrayList<BoardSquare> ();
			if(board[x][y-1].black) {
				updateThese.add(board[x][y-1]);
				while(tempX == x && tempY >= 0 && !board[tempX][tempY].white) {
					if(board[tempX][tempY].black) {
						updateThese.add(board[tempX][tempY]);
					}
					tempY--;
				}
				for(int i = 0; i < updateThese.size(); i++) {
					BoardSquare current = updateThese.get(i);
					updateSquareUser(current.x, current.y);
				}
			}
			tempX = x + 2;
			tempY = y - 2;
			updateThese.clear();
			if(x <= board.length-1) {
				//top right
				if(board[x+1][y-1].black) {
					updateThese.add(board[x+1][y-1]);
					while(tempX < board.length && tempY >= 0 && !board[tempX][tempY].white) {
						if(board[tempX][tempY].black) {
							updateThese.add(board[tempX][tempY]);
						}
						tempY--;
						tempX++;
					}
					for(int i = 0; i < updateThese.size(); i++) {
						BoardSquare current = updateThese.get(i);
						updateSquareUser(current.x, current.y);
					}
				}
			}
		}
		
		if(x <= board.length -1) {
			int tempX = x + 2;
			int tempY = y;
			ArrayList <BoardSquare> updateThese = new ArrayList <BoardSquare> ();
			//right
			if(board[x+1][y].black) {
				updateThese.add(board[x+1][y]);
				while(tempX < board.length && tempY == y && !board[tempX][tempY].white) {
					if(board[tempX][tempY].black) {
						updateThese.add(board[tempX][tempY]);
					}
					tempX++;
				}
				for(int i = 0; i < updateThese.size(); i++) {
					BoardSquare current = updateThese.get(i);
					updateSquareUser(current.x, current.y);
				}
			}
			tempX = x + 2;
			tempY = y + 2;
			updateThese.clear();
			//bottom right
			if(board[x+1][y+1].black) {
				updateThese.add(board[x+1][y+1]);
				while(tempX < board.length && tempY < board[0].length && !board[tempX][tempY].white) {
					if(board[tempX][tempY].black) {
						updateThese.add(board[tempX][tempY]);
					}
					tempX++;
					tempY++;
				}
				for(int i = 0; i < updateThese.size(); i++) {
					BoardSquare current = updateThese.get(i);
					updateSquareUser(current.x, current.y);
				}
			}
		}
		
		if(y <= board[x].length - 1) {
			int tempX = x;
			int tempY = y+2;
			ArrayList<BoardSquare> updateThese = new ArrayList<BoardSquare> ();
			//bottom
			if(board[x][y+1].black) {
				updateThese.add(board[x][y+1]);
				while(tempX == x && tempY < board[0].length && !board[tempX][tempY].white) {
					if(board[tempX][tempY].black) {
						updateThese.add(board[tempX][tempY]);
					}
					tempY++;
				}
				for(int i = 0; i < updateThese.size(); i++) {
					BoardSquare current = updateThese.get(i);
					updateSquareUser(current.x, current.y);
				}
			}
		}
	}
	
	public static void updateSquareAI (final int x, final int y) {
		board[x][y].black = true;
		board[x][y].white = false;
		board[x][y].blank = false;
	}
	
	public static void checkSurroundingsAI(final BoardSquare selectedMove) {
		final int x = selectedMove.x;
		final int y = selectedMove.y;
		
		
		if(x >= 1 ) {
			if(y >= 1) {
				//top left
				int tempY = y - 2;
				int tempX = x - 2;
				ArrayList<BoardSquare> updateThese = new ArrayList<BoardSquare>();
				if(board[x-1][y-1].white) {
					updateThese.add(board[x-1][y-1]);
					while(tempY >= 0 && tempX >= 0 && !board[tempX][tempY].black) {
						if(board[tempX][tempY].white) {
							updateThese.add(board[tempX][tempY]);
						}
						tempY--;
						tempX--;
					}
				}
				for(int i = 0; i < updateThese.size(); i++) {
					BoardSquare current = updateThese.get(i);
					updateSquareAI(current.x, current.y);
				}
			}
			if(y <= board[0].length-1) {
				int tempX = x - 2;
				int tempY = y + 2;
				ArrayList<BoardSquare> updateThese = new ArrayList<BoardSquare>();
				//bottom left
				if(board[x-1][y+1].white) {
					updateThese.add(board[x-1][y+1]);
					while(tempX >= 0 && tempY < board[0].length && !board[tempX][tempY].black) {
						if(board[tempX][tempY].white) {
							updateThese.add(board[tempX][tempY]);
						}
						tempY++;
						tempX--;
					}
					for(int i = 0; i < updateThese.size(); i++) {
						BoardSquare current = updateThese.get(i);
						updateSquareAI(current.x, current.y);
					}
				}
			}
			//left
			int tempX = x - 2;
			int tempY = y;
			ArrayList<BoardSquare> updateThese = new ArrayList<BoardSquare>();
			if(board[x-1][y].white) {
				updateThese.add(board[x-1][y]);
				while(tempX >= 0 && tempY == y && !board[tempX][tempY].black) {
					if(board[tempX][tempY].white) {
						updateThese.add(board[tempX][tempY]);
					}
					tempX--;
				}
				for(int i = 0; i < updateThese.size(); i++) {
					BoardSquare current = updateThese.get(i);
					updateSquareAI(current.x, current.y);
				}
			}
		}
		if(y >= 1) {
			//top
			int tempX = x;
			int tempY = y - 2;
			ArrayList<BoardSquare> updateThese = new ArrayList<BoardSquare> ();
			if(board[x][y-1].white) {
				updateThese.add(board[x][y-1]);
				while(tempX == x && tempY >= 0 && !board[tempX][tempY].black) {
					if(board[tempX][tempY].white) {
						updateThese.add(board[tempX][tempY]);
					}
					tempY--;
				}
				for(int i = 0; i < updateThese.size(); i++) {
					BoardSquare current = updateThese.get(i);
					updateSquareAI(current.x, current.y);
				}
			}
			tempX = x + 2;
			tempY = y - 2;
			updateThese.clear();
			if(x <= board.length-1) {
				//top right
				if(board[x+1][y-1].white) {
					updateThese.add(board[x+1][y-1]);
					while(tempX < board.length && tempY >= 0 && !board[tempX][tempY].black) {
						if(board[tempX][tempY].white) {
							updateThese.add(board[tempX][tempY]);
						}
						tempY--;
						tempX++;
					}
					for(int i = 0; i < updateThese.size(); i++) {
						BoardSquare current = updateThese.get(i);
						updateSquareAI(current.x, current.y);
					}
				}
			}
		}
		
		if(x <= board.length -1) {
			int tempX = x + 2;
			int tempY = y;
			ArrayList <BoardSquare> updateThese = new ArrayList <BoardSquare> ();
			//right
			if(board[x+1][y].white) {
				updateThese.add(board[x+1][y]);
				while(tempX < board.length && tempY == y && !board[tempX][tempY].black) {
					if(board[tempX][tempY].white) {
						updateThese.add(board[tempX][tempY]);
					}
					tempX++;
				}
				for(int i = 0; i < updateThese.size(); i++) {
					BoardSquare current = updateThese.get(i);
					updateSquareAI(current.x, current.y);
				}
			}
			tempX = x + 2;
			tempY = y + 2;
			updateThese.clear();
			//bottom right
			if(board[x+1][y+1].white) {
				updateThese.add(board[x+1][y+1]);
				while(tempX < board.length && tempY < board[0].length && !board[tempX][tempY].black) {
					if(board[tempX][tempY].white) {
						updateThese.add(board[tempX][tempY]);
					}
					tempX++;
					tempY++;
				}
				for(int i = 0; i < updateThese.size(); i++) {
					BoardSquare current = updateThese.get(i);
					updateSquareAI(current.x, current.y);
				}
			}
		}
		
		if(y <= board[x].length - 1) {
			int tempX = x;
			int tempY = y+2;
			ArrayList<BoardSquare> updateThese = new ArrayList<BoardSquare> ();
			//bottom
			if(board[x][y+1].white) {
				updateThese.add(board[x][y+1]);
				while(tempX == x && tempY < board[0].length && !board[tempX][tempY].black) {
					if(board[tempX][tempY].white) {
						updateThese.add(board[tempX][tempY]);
					}
					tempY++;
				}
				for(int i = 0; i < updateThese.size(); i++) {
					BoardSquare current = updateThese.get(i);
					updateSquareAI(current.x, current.y);
				}
			}
		}
	}
	
	//to do------------------------------does black need to be false-------------
	public static void executeUserMove (final BoardSquare selectedMove) {
		selectedMove.white = true;
		selectedMove.blank = false;
		selectedMove.value = Integer.MIN_VALUE;
		
		board [selectedMove.x][selectedMove.y] = selectedMove;
		checkSurroundingsUser (selectedMove);
		// need to flip colors when move is played. 
		// b is being added at the top so I need to fix that. 
		// start programming AI.
		
	}
	
//i got this ---------------------------------------------------------------------------------------------------------	
	public static void userMove (Scanner input) {
		boolean validMove = false;
		BoardSquare selectedMove = null;
		while (!validMove) {
			System.out.println("Please enter the coordinates of where you would like to place a piece (x,y): ");
			String response = input.nextLine();
			selectedMove = parseMove (response);
			validMove = verifyMove (selectedMove);
			if(!validMove) {
				System.out.println("Square selected does not result in a valid move, please select again");
			}
		}
		executeUserMove (selectedMove);
	}

	public static BoardSquare selectAI () {
		BoardSquare selected = new BoardSquare ();
		for(int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[0].length; y++) {
				if(board[x][y].value > selected.value) {
					selected = board [x][y];
				}
			}
		}
		return selected;
	}
	
	//to do
	public static void executeAIMove (final BoardSquare selected) {
		selected.black = true;
		selected.blank = false;
		selected.value = Integer.MIN_VALUE;
		board [selected.x][selected.y] = selected;
		checkSurroundingsAI(selected);
	}
	
	public static void aiMove () {
	    BoardSquare ComputerMove;
	    ArrayList<BoardSquare> LegalMoves = LegalComputerMove();
	    int maxvalue = -10, index = 0;		
	    if (LegalMoves.size () == 0){
	        return;
	    }
	    else if (LegalMoves.size () == 1){
	        ComputerMove = LegalMoves.get (0);
	    }
	    else {
	        for(int i = 0; i < LegalMoves.size (); i++){
	            if(LegalMoves.get (i).value > maxvalue){
	                maxvalue = LegalMoves.get (i).value;
	                index = i;
	            }
	        }
	    }	    
	    ComputerMove = LegalMoves.get (index);
		executeAIMove(ComputerMove);		
	}	
	
	public static void playGame (final BoardSquare bs) {
		Scanner userInput = new Scanner (System.in);
		while(!gameComplete()) {
			bs.printBoard();
			userMove(userInput);
			aiMove();
		}
		bs.determineWinner();
	}
	
	public static void main (final String [] args) {
		BoardSquare bs = new BoardSquare();
		bs.initializeBoard();
		board = bs.board;
		playGame (bs);
	}
}
