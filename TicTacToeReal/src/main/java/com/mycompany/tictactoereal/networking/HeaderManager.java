/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoereal.networking;

import com.mycompany.tictactoereal.gamelogic.GameLogic;
import java.util.Arrays;

/**
 *
 * @author sami
 */
public class HeaderManager {
    
    private static final int FIELDCOUNT = 3;
    
    /* IMPORTANT: in the header structure you should always make sure that the following positional rules are followed:
        
    0: sender
    1: game userlist
    last: message
    
    */
    
    
    
    public static boolean validateHeader(String fullMessage, GameLogic gl) { // Add better checks
        if(fullMessage.contains(".")) return true; // Ip-address? Change when adding headers to server
        //System.out.println("validateHeader fullMessage " + fullMessage);
        String[] strArr = fullMessage.split(";");
        String[] usrArr = gl.getPlayerArray();
        //System.out.println("strArr[0]:" + strArr[0]);

        if (strArr[0].equals(gl.getUserHash())) {
            return false;
        }
        
        boolean senderMatch = false; 
        for (String usr : usrArr) {
            if (usr.equals(strArr[0])) {
                senderMatch = true;
                break;
            }
        }
        
        return  senderMatch
            &&  strArr.length == FIELDCOUNT
            && (strArr[0].length() == 10 && !strArr[0].contains(",")) // valid user hash
            &&  strArr[1].contains(",")    // valid user list
        ; 
    }
    
    public static String addHeader(String message, String usrHash, String[] userList) {
        if(userList.length == 0) return usrHash + ";" + usrHash + ";" + message;
        String stringList = userList[0];
        for (int i = 1; i < userList.length; i++) {
            stringList += "," + userList[i];
        }
        
        return usrHash + ";" + stringList + ";" + message;
    }
    
    public static String addHeader(String message, GameLogic gameLogic) {
        String usrHash = gameLogic.getUserHash();
        String[] userList = gameLogic.getPlayerArray();
        
        return addHeader(message, usrHash, userList);
    }
    
    public static String addHeaderWithMovecount(String message, GameLogic gameLogic) {
        String newMessage = message + "," + gameLogic.getSymbolInTurn() + "," + gameLogic.getTurnNumber();
        return  HeaderManager.addHeader(newMessage, gameLogic);
    }
    
    public static String getMessage(String fullMessage) {
        String[] strArr = fullMessage.split(";");
        if(strArr.length < FIELDCOUNT) return strArr[0]; //Due to server lacking header
        return strArr[FIELDCOUNT-1];
    }
    
    public static String[] getUserList(String fullMessage) {
        String[] strArr = fullMessage.split(";");
        String[] userList = strArr[1].split(",");
        
        return userList;
    }
    
}
