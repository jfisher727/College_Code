import java.util.ArrayList;
import java.util.Scanner;

/*
 * Author: Jason Fisher, jfisher2009@my.fit.edu
 * Course: CSE 4015
 * Project: proj07
 */

public class Sudoku {
    public static final int BOARD_SIZE = 9;
    private static final int ACTIVE_THREAD_COUNT = 3;
    static ArrayList<int []> positionsSolved = new ArrayList<int []> ();
    static ArrayList<int []> positionsBeingSolved = new ArrayList<int []> ();
    static ArrayList<int []> unsolvedPositions = new ArrayList<int []> ();
    //public static int board [][] = new int [BOARD_SIZE][BOARD_SIZE];

    public static int [][] processInput (final String input) {
        final int [][] board = new int [BOARD_SIZE][BOARD_SIZE];
        int counter = 0;
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (Character.isDigit(input.charAt(counter))) {
                    board [x][y] = Integer.valueOf
                            (input.charAt(counter)
                            - '0');
                }
                else {
                    board [x][y] = 0;
                    final int [] position = {x, y};
                    unsolvedPositions.add(position);
                }
                counter++;
            }
        }
        return board;
    }

    public static boolean boardComplete (final int [][] board) {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board [x][y] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean compareAlreadSolved (final ArrayList<int []> solved,
            final int [] currentSquare) {
        for (int i = 0; i < solved.size(); i++) {
            final int [] comparision = solved.get(i);
            if (comparision [0] == currentSquare [0]
                    &&  comparision [1] == currentSquare [1]) {
                return true;
            }
        }
        return false;
    }

    public static boolean compareBeingSolved (final ArrayList<int []> processing,
            final int [] currentSquare) {
        for (int i = 0; i < processing.size(); i++) {
            final int [] comparision = processing.get(i);
            if (comparision [0] == currentSquare [0]
                    &&  comparision [1] == currentSquare [1]) {
                return true;
            }
        }
        return false;
    }

    public static void removeSquare (final int [] square) {
        for (int i = 0; i < positionsBeingSolved.size(); i++) {
            final int [] current = positionsBeingSolved.get(i);
            if (current [0] == square [0] && current [1] == square [1]) {
                positionsBeingSolved.remove(i);
            }
        }
    }

    public static boolean needsSolving (final int [] square) {
        for (int i = 0; i < unsolvedPositions.size(); i++) {
            final int [] current = unsolvedPositions.get(i);
            if (square [0] == current [0] && square [1] == current [1]) {
                return true;
            }
        }
        return false;
    }

    public static int [] generateNewPosition () {
        final int [] currentSquare = {generateRandomPosition (),
                generateRandomPosition ()};
        while (compareAlreadSolved (positionsSolved, currentSquare)
                && !compareBeingSolved(positionsBeingSolved,
                        currentSquare)) {
            if (needsSolving(currentSquare)) {
                break;
            }
            final int x = generateRandomPosition ();
            final int y = generateRandomPosition ();
            currentSquare [0] = x;
            currentSquare [1] = y;
        }
        return currentSquare;
    }

    public static int generateRandomPosition () {
        return (int) (Math.random() * System.currentTimeMillis() % BOARD_SIZE);
    }

    public static boolean checkSquareSolved (final int [][]
            solvedBoard, final int [] currentSquare) {
        synchronized (solvedBoard) {
            if (solvedBoard [currentSquare[0]]
                    [currentSquare[1]] != 0) {
                positionsSolved.add(currentSquare);
                return true;
            }
        }
        return false;
    }

    public static SolverThread [] initializeThreads (final int [][] solvedBoard) {
        final SolverThread [] activeThreads =
                new SolverThread [ACTIVE_THREAD_COUNT];
        for (int i = 0; i < ACTIVE_THREAD_COUNT; i++) {
            //final int [] currentSquare = generateNewPosition ();
            final int [] currentSquare = unsolvedPositions.get(0);
            unsolvedPositions.remove(0);
            activeThreads [i] = new SolverThread(solvedBoard,
                    currentSquare [0], currentSquare [1]);
            activeThreads [i].start();
            positionsBeingSolved.add(currentSquare);
            final boolean solved = checkSquareSolved
                    (solvedBoard, currentSquare);
            if (!solved) {
                removeSquare (currentSquare);
                unsolvedPositions.add(currentSquare);
            }

        }
        return activeThreads;
    }

    public static int [][] solveBoard (final int [][] board) {
        final SolverThread [] activeThreads;
        int [][] solvedBoard = board;
        int counter = 0;
        boolean solved = false;
        activeThreads = initializeThreads (solvedBoard);

        while (!boardComplete (solvedBoard) && unsolvedPositions.size() > 0) {
            if (!activeThreads[counter].isAlive()) {
                System.out.println("Selecting new square.");
                final int [] currentSquare = generateNewPosition ();
                activeThreads [counter] =
                        new  SolverThread(solvedBoard,
                                currentSquare [0],
                                currentSquare [1]);
                activeThreads [counter].start();
                solved = checkSquareSolved (solvedBoard, currentSquare);
                if (!solved) {
                    removeSquare (currentSquare);
                    unsolvedPositions.add(currentSquare);
                }
            }
            else {
                final SolverThread active = activeThreads[counter];
                final int [] currentSquare = active.getSquare();
                solved = checkSquareSolved (solvedBoard, currentSquare);
                if (!solved) {
                    removeSquare (currentSquare);
                    unsolvedPositions.add(currentSquare);
                }
            }
            counter++;
            counter %= ACTIVE_THREAD_COUNT;
        }
        return solvedBoard;
    }

    public static String printBoard (final int [][] board) {
        String solved = "";
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                solved += board [x][y];
            }
        }
        return solved;
    }

    public static void main (final String [] args) {
        final Scanner stdInput = new Scanner (System.in);
        while (stdInput.hasNext()) {
            final String currentBoard = stdInput.nextLine();
            final int [][] boardLayout = processInput(currentBoard);
            final int [][] solvedBoard = solveBoard (boardLayout);
            final String solved = printBoard (solvedBoard);
            System.out.println(solved);
        }
        stdInput.close();
    }
}
