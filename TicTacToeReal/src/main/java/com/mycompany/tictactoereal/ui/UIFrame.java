/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tictactoereal.ui;

import com.mycompany.tictactoereal.networking.SocketWithMatchmaker;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Julia Bergman <bergmjul>
 */
public class UIFrame extends JFrame {

    private boolean gameMode;
    private WaitingInterface waitingI;
    private SocketWithMatchmaker socket;
    private String matchmakerIP;
    private int matchmakerPort;

    public UIFrame(WaitingInterface waitingI, SocketWithMatchmaker socket) {
        this.waitingI = waitingI;
        this.gameMode = this.waitingI.getReceivedStartGameMessage();
        this.socket = socket;
        this.matchmakerIP = "127.0.0.1";
        this.matchmakerPort = 6666;
        enterGame(this.gameMode);
    }

    public void enterGame(boolean gameMode) {
        System.out.println("In UIFrame.enterGame");
        if (gameMode) {
            System.out.println("GameInterface made");
            add(new GameInterface());
        } else {
            add(waitingI);

            try {
                socket.run(matchmakerIP, matchmakerPort);
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            }
        }

        setResizable(false);
        pack();

        setTitle("");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
