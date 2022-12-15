package com.mycompany.tictactoereal;

import com.mycompany.tictactoereal.networking.SocketWithMatchmaker;
import com.mycompany.tictactoereal.ui.GameInterface;
import com.mycompany.tictactoereal.ui.WaitingInterface;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main extends JFrame {

    public Main() {

        WaitingInterface waiting = new WaitingInterface();
        SocketWithMatchmaker socket = new SocketWithMatchmaker(waiting);
        add(waiting);

        setResizable(false);
        pack();

        setTitle("");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new Main();
                frame.setTitle("Tic tac toe");
                frame.setVisible(true);
            }
        });
        
    }

    public void enterGame(boolean gameMode) {

        if (gameMode) {
            add(new GameInterface());
        } else {
            add(new WaitingInterface());
        }

        setResizable(false);
        pack();

        setTitle("");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
