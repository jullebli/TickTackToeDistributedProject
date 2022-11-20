package com.mycompany.tictactoereal.gamelogic;

public class GameLogic {

    private int[][] gameBoard;

    // 0 - empty
    // 1 - x
    // 2 - o
    // 3 - triangle
    // 4 - star
    
    public GameLogic() {
        this.gameBoard = new int[30][30];
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public void placeTile(int x, int y, int tileId) {
        gameBoard[x][y] = tileId;
    }

}
