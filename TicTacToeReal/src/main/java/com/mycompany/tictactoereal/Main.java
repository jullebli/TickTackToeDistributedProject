package com.mycompany.tictactoereal;

import com.mycompany.tictactoereal.networking.SocketWithMatchmaker;
import com.mycompany.tictactoereal.ui.GameInterface;
import com.mycompany.tictactoereal.ui.UIFrame;
import com.mycompany.tictactoereal.ui.WaitingInterface;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class Main {

    //public static void main(String[] args) {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                WaitingInterface waitingI = new WaitingInterface();
                System.out.println("Made WaitingInterface");
                SocketWithMatchmaker socket = new SocketWithMatchmaker(waitingI, "127.0.0.1", 6666);
                System.out.println("Made socket");
                JFrame frame = new UIFrame(waitingI, socket);
                System.out.println("Made UIFrame");
                frame.setTitle("Tic tac toe");
                frame.setVisible(true);
            }
        });
    }
}