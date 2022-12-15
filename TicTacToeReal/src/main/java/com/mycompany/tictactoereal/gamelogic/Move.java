package com.mycompany.tictactoereal.gamelogic;

public class Move {

    private int symbol;
    private int x;
    private int y;
    private int turnNumber;

    public Move(int symbol, int x, int y, int turnNumber) {
        this.symbol = symbol;
        this.x = x;
        this.y = y;
        this.turnNumber = turnNumber;
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

}
