package tictactoereal;

import tictactoereal.ui.GameInterface;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main extends JFrame {

    public Main() {

        add(new GameInterface());

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
}
