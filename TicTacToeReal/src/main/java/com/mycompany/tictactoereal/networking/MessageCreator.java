package com.mycompany.tictactoereal.networking;

import com.mycompany.tictactoereal.gamelogic.GameLogic;
import com.mycompany.tictactoereal.gamelogic.Move;
import java.util.ArrayList;

public class MessageCreator {

    public static String createPlaceTileMessage(int x, int y, int tileId, GameLogic gameLogic) {
        String message = x + "," + y + "," + tileId;
        return HeaderManager.addHeaderWithMovecount(message,"placetile", gameLogic);
    }

    public static String createSendGameStateMessage(ArrayList<Move> moves, GameLogic gameLogic) {
        String message = "";
        
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
        return HeaderManager.addHeader(message,"gamestate", gameLogic);
    }
    
    public static String createPing(GameLogic gameLogic) {
        return HeaderManager.addHeader(gameLogic.getUserHash(),"ping", gameLogic);
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
