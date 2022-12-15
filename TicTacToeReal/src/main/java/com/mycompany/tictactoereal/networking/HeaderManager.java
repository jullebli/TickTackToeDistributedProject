/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoereal.networking;

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
    
    
    
    public static boolean validateHeader(String fullMessage) { // Add better checks
        String[] strArr = fullMessage.split(";");
       
        // valid user has
        if (strArr[0].length() != 10 || strArr[0].contains(",")) return false; 
       
        // valid user list
        if (!strArr[1].contains(",")) return false;
        
        
        return true;
    }
    
    public static String addHeader(String message, String sender, String[] userList) {
        return sender + ";" + Arrays.toString(userList) + ";" + message;
    }
    
    public static String getMessage(String fullMessage) {
        String[] strArr = fullMessage.split(";");
        
        return strArr[FIELDCOUNT-1];
    }
    
    public static String[] getUserList(String fullMessage) {
        String[] strArr = fullMessage.split(";");
        
        String[] userList = strArr[1].split(",");
        
        return userList;
    }
    
}
