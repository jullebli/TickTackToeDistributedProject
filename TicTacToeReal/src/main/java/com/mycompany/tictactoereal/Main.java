package com.mycompany.tictactoereal;

import com.mycompany.tictactoereal.networking.SocketWithMatchmaker;
import com.mycompany.tictactoereal.ui.GameInterface;
import com.mycompany.tictactoereal.ui.WaitingInterface;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class Main extends JFrame {

    private boolean gameMode;

    public Main(boolean gameMode) {
        this.gameMode = gameMode;

        enterGame(this.gameMode);

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new Main(false);
                System.out.println("new Main made");
                frame.setTitle("Tic tac toe");
                frame.setVisible(true);
            }
        });

    }

    public void enterGame(boolean gameMode) {
        System.out.println("In Main.enterGame");
        if (gameMode) {
            System.out.println("GameInterface made");
            WaitingInterface waiting = new WaitingInterface();
            SocketWithMatchmaker socket = new SocketWithMatchmaker(waiting);
            add(new GameInterface());
        } else {
            System.out.println("new WaitingInterface made");
            WaitingInterface waiting = new WaitingInterface();
            SocketWithMatchmaker socket = new SocketWithMatchmaker(waiting);
            add(waiting);
            try {
                System.out.println("Starting socket with matchmaker");
                socket.start("127.0.0.1", 6666);
            } catch (IOException e) {
                System.out.println("Exception: " + e);
            }

        }

        setResizable(false);
        pack();

        setTitle("");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

/*


        setResizable(false);
        pack();

        setTitle("");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


*/
