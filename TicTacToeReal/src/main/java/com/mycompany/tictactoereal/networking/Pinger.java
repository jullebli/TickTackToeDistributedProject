/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoereal.networking;

import com.mycompany.tictactoereal.gamelogic.GameLogic;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sami Vornanen <vornsami>
 */
public class Pinger extends Thread {
    
    private final GameLogic gamelogic;
    private boolean RUNNING; 
    private HashMap<String, Integer> lastSeen;
    
    public Pinger(GameLogic gl) {
        this.gamelogic = gl;
        this.lastSeen = new HashMap<>();
        
        String[] playerArr = gamelogic.getPlayerArray();
        String userHash = gamelogic.getUserHash();
        
        for (String playerHash : playerArr) {
            if (playerHash.equals(userHash)) {
                continue;
            }
            lastSeen.put(playerHash, 0);
        }
        
    }
    
    @Override
    public void run() {
        this.RUNNING = true;
        
        MulticastPublisher publisher = gamelogic.getPublisher();
        
        while(RUNNING) {
            try {
                publisher.multicast(gamelogic.getUserHash());
                Thread.sleep(5000);
                Iterator<Map.Entry <String, Integer>> it = lastSeen.entrySet().iterator();
                
                while(it.hasNext()) {
                    Map.Entry <String, Integer> user = it.next();
                    
                    if(user.getValue() > 5); // Add failure detection here
                    
                    user.setValue(user.getValue() + 1);
                }
                
                System.out.println(lastSeen);
                
            } catch (InterruptedException | IOException ex) {
                Logger.getLogger(Pinger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void playerReset(String playerHash) {
        if(!this.lastSeen.containsKey(playerHash)) return;
        
        this.lastSeen.replace(playerHash, 0);
    }
}
