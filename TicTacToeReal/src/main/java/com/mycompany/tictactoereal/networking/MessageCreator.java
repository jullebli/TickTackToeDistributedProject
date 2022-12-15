package com.mycompany.tictactoereal.networking;

import com.mycompany.tictactoereal.gamelogic.Move;
import java.util.ArrayList;

public class MessageCreator {

    public MessageCreator() {

    }

    public static String createPlaceTileMessage(int symbolInTurn, int turnNumber, int x, int y, int tileId) {
        return getHeader(symbolInTurn, turnNumber) + ";" + x + "," + y + "," + tileId;
    }

    public static String createSendGameStateMessage(int symbolInTurn, int turnNumber, ArrayList<Move> moves) {
        String message = "";
        message += getHeader(symbolInTurn, turnNumber);
        for (int i = 0; i < moves.size(); i++) {
            message += ";";
            message += moves.get(i).getX();
            message += ",";
            message += moves.get(i).getY();
            message += ",";
            message += moves.get(i).getSymbol();
            message += ",";
            message += moves.get(i).getTurnNumber();
        }
        return message;
    }

    public static String getHeader(int symbolInTurn, int turnNumber) {
        String header = "todo-header";
        return header + "," + symbolInTurn + "," + turnNumber;
    }

    //every message has information who made the last move and how many moves have been made in the game
    //other messages we planned:
    //--------------------------
    //ping
    //vote
    //begin leader selection for voting
    //consensus of players in the game
    //turn timer activated
    //turn skipped
    //kick out a player
    //I won didn't I?
}
