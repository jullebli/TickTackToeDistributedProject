/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoereal.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
/**
 *
 * @author bivek
 */
public class WaitingInterface extends JPanel{
    
    
   
  
    // Label to display text
    
    private static Font font;
    private static boolean isWaitingResponse;
 
    // default constructor
    
  
    // Main class
    public WaitingInterface()
    {
     
  
        // Creating a new buttons
        
        JButton startButton = new JButton("start a game");
        
        font = new Font("Baskerville", Font.PLAIN, 32);

        setPreferredSize(new Dimension(900, 750));
        
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isWaitingResponse = true;
                startButton.setVisible(false);
                
            }
        });
  
        add(startButton);
    }
    
    
    public void paintComponent(Graphics g) {
        if (isWaitingResponse) {
            g.drawString("Waiting for response of match making service",40,40);  
        }
        repaint();
    }

    
        
}
