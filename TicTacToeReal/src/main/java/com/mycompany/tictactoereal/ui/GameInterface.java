package com.mycompany.tictactoereal.ui;

import com.mycompany.tictactoereal.gamelogic.GameLogic;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicTabbedPaneUI.MouseHandler;

public class GameInterface extends JPanel {

    private static BufferedImage tile_empty, tile_x, tile_o, tile_tri, tile_star;
    private static boolean inGame;
    private static GameLogic gameLogic;

    public GameInterface() {

        gameLogic = new GameLogic();

        setPreferredSize(new Dimension(900, 600));

        MouseHandler handler = new MouseHandler();
        addMouseListener(handler);

        JButton startButton = new JButton("start a game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGame = true;
                startButton.setVisible(false);
            }
        });
        add(startButton);

        try {
            tile_empty = ImageIO.read(new File("src/main/resources/tile_empty.png"));
            tile_x = ImageIO.read(new File("src/main/resources/tile_x.png"));
            tile_o = ImageIO.read(new File("src/main/resources/tile_o.png"));
            tile_tri = ImageIO.read(new File("src/main/resources/tile_tri.png"));
            tile_star = ImageIO.read(new File("src/main/resources/tile_star.png"));
        } catch (IOException ex) {
            System.out.println("file not found");
        }
    }

    public void paintComponent(Graphics g) {
        if (inGame) {
            for (int i = 0; i < 30; i++) {
                for (int j = 0; j < 30; j++) {
                    switch (gameLogic.getGameBoard()[i][j]) {
                        case 0:
                            g.drawImage(tile_empty, 150 + 20 * i, 20 * j, null);
                            break;
                        case 1:
                            g.drawImage(tile_x, 150 + 20 * i, 20 * j, null);
                            break;
                        case 2:
                            g.drawImage(tile_o, 150 + 20 * i, 20 * j, null);
                            break;
                        case 3:
                            g.drawImage(tile_tri, 150 + 20 * i, 20 * j, null);
                            break;
                        case 4:
                            g.drawImage(tile_star, 150 + 20 * i, 20 * j, null);

                            break;
                    }
                }
            }
        }
        repaint();
    }

    private class MouseHandler implements MouseListener, MouseMotionListener {
        // MouseListener event handlers
        
        // handle event when mouse released immediately after press
        @Override
        public void mouseClicked(MouseEvent event) {
            if (inGame) {
                int clickX = (event.getX() - 150) / 20;
                int clickY = event.getY() / 20;
                if (clickX >= 0 && clickX <= 30 && clickY >= 0 && clickY <= 30) {
                    int i = gameLogic.getGameBoard()[clickX][clickY];
                    if (i == 4) {
                        gameLogic.placeTile(clickX, clickY, 0);
                    } else {
                        gameLogic.placeTile(clickX, clickY, gameLogic.getGameBoard()[clickX][clickY] + 1);
                    }
                }
            }
        }

        // handle event when mouse pressed
        @Override
        public void mousePressed(MouseEvent event) {
        }

        // handle event when mouse released
        @Override
        public void mouseReleased(MouseEvent event) {
        }

        // handle event when mouse enters area
        @Override
        public void mouseEntered(MouseEvent event) {
        }

        // handle event when mouse exits area
        @Override
        public void mouseExited(MouseEvent event) {
        }

        // MouseMotionListener event handlers
        // handle event when user drags mouse with button pressed
        @Override
        public void mouseDragged(MouseEvent event) {
        }

        // handle event when user moves mouse
        @Override
        public void mouseMoved(MouseEvent event) {
        }
    }
}
