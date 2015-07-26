import java.util.ArrayList;


public class SolverThread extends Thread {
    private static final int UPPER_CUBE_BOUND = 2;
    private static final int CUBE_SIZE = 3;
    private int [][] board;
    private int xCord;
    private int yCord;
    private ArrayList<Integer> possibleValues;

    public SolverThread (final int [][] sudoku, final int x, final int y) {
        board = sudoku;
        xCord = x;
        yCord = y;
        possibleValues = new ArrayList<Integer> ();
        for (int i = 1; i <= Sudoku.BOARD_SIZE; i++) {
            possibleValues.add(i);
        }
    }

    public final int [] getSquare () {
        int [] square = {xCord, yCord};
        return square;
    }

    public final void findValue (final int value) {
        for (int i = 0; i < possibleValues.size(); i++) {
            if (possibleValues.get(i) == value) {
                possibleValues.remove(i);
                break;
            }
        }
    }

    public final void solveRow () {
        for (int i = 0; i < board.length; i++) {
            final int squareValue = board [i][yCord];
            findValue (squareValue);
        }
    }

    public final void solveColumn () {
        for (int i = 0; i < board[0].length; i++) {
            final int squareValue = board [xCord][i];
            findValue (squareValue);
        }
    }

    public final void solveCube () {
        final int cubeXCord = xCord % CUBE_SIZE;
        final int lowCubeX = xCord - cubeXCord;
        int highCubeX;
        if (cubeXCord == 0) {
            highCubeX = xCord + UPPER_CUBE_BOUND;
        }
        else if (cubeXCord == 2) {
            highCubeX = xCord;
        }
        else {
            highCubeX = xCord + 1;
        }
        final int cubeYCord = yCord % CUBE_SIZE;
        final int lowCubeY = yCord - cubeYCord;
        int highCubeY;
        if (cubeYCord == 0) {
            highCubeY = yCord + 2;
        }
        else if (cubeYCord == 2) {
            highCubeY = yCord;
        }
        else {
            highCubeY = yCord + 1;
        }

        for (int x = lowCubeX; x <= highCubeX; x++) {
            for (int y = lowCubeY; y <= highCubeY; y++) {
                if (x == xCord && y == yCord) {
                    continue;
                }
                final int squareValue = board [x][y];
                findValue (squareValue);
            }
        }

    }

    public final void solveForSquare () {
        solveRow ();
        solveColumn();
        solveCube();
    }

    private void printBoard () {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                System.out.print(" " + board [x][y]);
            }
            System.out.println();
        }
    }

    public final void run () {
        if (board [xCord][yCord] == 0) {
            solveForSquare ();
            if (possibleValues.size() == 1) {
                synchronized (board) {
                    board [xCord][yCord] = possibleValues.get(0);
                }
            }
        }
        Thread.yield();
        return;
    }
}
