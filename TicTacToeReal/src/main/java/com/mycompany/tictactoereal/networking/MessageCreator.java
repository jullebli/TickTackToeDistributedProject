package com.mycompany.tictactoereal.networking;

import com.mycompany.tictactoereal.gamelogic.GameLogic;
import com.mycompany.tictactoereal.gamelogic.Move;
import java.util.ArrayList;

public class MessageCreator {

    public static String createPlaceTileMessage(int x, int y, int tileId, GameLogic gameLogic) {
        String message = x + "," + y + "," + tileId;
        return HeaderManager.addHeader(message, gameLogic);
    }

    public static String createSendGameStateMessage(ArrayList<Move> moves, GameLogic gameLogic) {
        String message = "";
        message += getHeader(gameLogic);
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
    
    public static String getHeader(GameLogic gameLogic) { 
        String header = "todo-header";
        return header + "," + gameLogic.getSymbolInTurn() + "," + gameLogic.getTurnNumber();
    }
    
    public static String createPing(GameLogic gameLogic) {
        return HeaderManager.addHeader(gameLogic.getUserHash(), gameLogic);
    }

    //every message has information who made the last move and how many moves have been made in the game
    //other messages we planned:
    //--------------------------
    //vote
    //begin leader selection for voting
    //consensus of players in the game
    //turn timer activated
    //turn skipped
    //kick out a player
    //I won didn't I?
}
