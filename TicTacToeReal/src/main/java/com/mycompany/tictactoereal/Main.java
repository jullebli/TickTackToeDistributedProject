package com.mycompany.tictactoereal;

import com.mycompany.tictactoereal.ui.UIFrame;
import java.awt.EventQueue;
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                JFrame frame = new UIFrame();
                System.out.println("Made UIFrame");
                frame.setTitle("Tic tac toe");
                frame.setVisible(true);
            }
        });
    }
}