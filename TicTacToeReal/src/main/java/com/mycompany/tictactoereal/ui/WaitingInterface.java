/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoereal.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Font;

/**
 *
 * @author bivek, bergmjul
 */
public class WaitingInterface extends JPanel {

    private StartGameEventHandler startGameHandler;

    // default constructor
    // Main class
    public WaitingInterface() {

        // Creating a new buttons
        JButton startButton = new JButton("start a game");
        JLabel statusLabel = new JLabel("Press start to connect to server");

        Font font = new Font("Baskerville", Font.PLAIN, 32);
        statusLabel.setFont(font);

        setPreferredSize(new Dimension(900, 750));

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("Waiting for response of Matchmaking service");
                startButton.setVisible(false);
                startGameHandler.handle(new StartGameEvent());
            }
        });

        add(startButton);
        add(statusLabel);

    }

    public void setStartButtonClicked(StartGameEventHandler handler) {
        this.startGameHandler = handler;
    }
}
