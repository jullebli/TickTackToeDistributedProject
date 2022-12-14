package com.mycompany.tictactoereal.ui;

import com.mycompany.tictactoereal.gamelogic.GameLogic;
import com.mycompany.tictactoereal.gamelogic.Move;
import com.mycompany.tictactoereal.networking.MessageCreator;
import com.mycompany.tictactoereal.networking.MulticastPublisher;
import com.mycompany.tictactoereal.networking.MulticastReceiver;
import com.mycompany.tictactoereal.networking.Pinger;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameInterface extends JPanel {

    private static BufferedImage tile_empty, tile_x, tile_o, tile_tri, tile_star, sidebar, bottombar;
    private static boolean inGame;
    private static MulticastPublisher publisher;
    private static MulticastReceiver receiver;
    private static GameLogic gameLogic;
    private static Font font;
    //private static final String DEFAULT_ADDRESS = "230.0.0.0";

    public GameInterface(String multicastAddress, int playerNumber, String[] userHashes) {
        //Adresses needs to be given by Matchmaker
        publisher = new MulticastPublisher(multicastAddress);
        gameLogic = new GameLogic(publisher);
        gameLogic.setPlayerSymbol(playerNumber + 1);
        gameLogic.setPlayerAmount(userHashes.length);
        gameLogic.setUserHash(userHashes[playerNumber]);
        gameLogic.setPlayerArray(userHashes);
        gameLogic.setBoardChangedEventHandler(event -> {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Repainting");
                    repaint();
                }
            });
        });
        receiver = new MulticastReceiver(gameLogic, multicastAddress);
        Thread t = new Thread(receiver);
        t.start();
        Pinger pinger = new Pinger(this.gameLogic);
        gameLogic.setPinger(pinger);
        pinger.start();

        this.setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                //commands for testing during development
                if (e.getKeyCode() == KeyEvent.VK_1) {
                    gameLogic.setPlayerSymbol(1);
                } else if (e.getKeyCode() == KeyEvent.VK_2) {
                    gameLogic.setPlayerSymbol(2);
                } else if (e.getKeyCode() == KeyEvent.VK_3) {
                    gameLogic.setPlayerSymbol(3);
                } else if (e.getKeyCode() == KeyEvent.VK_4) {
                    gameLogic.setPlayerSymbol(4);
                } else if (e.getKeyCode() == KeyEvent.VK_5) {
                    gameLogic.setPlayerAmount(1);
                } else if (e.getKeyCode() == KeyEvent.VK_6) {
                    gameLogic.setPlayerAmount(2);
                } else if (e.getKeyCode() == KeyEvent.VK_7) {
                    gameLogic.setPlayerAmount(3);
                } else if (e.getKeyCode() == KeyEvent.VK_8) {
                    gameLogic.setPlayerAmount(4);
                } else if (e.getKeyCode() == KeyEvent.VK_0) {
                    System.out.println(MessageCreator.createSendGameStateMessage(gameLogic.getMoves(), gameLogic));
                    //ArrayList<Move> moves = new ArrayList<>();
                    //moves.add(new Move(1,5,5,1));
                    //moves.add(new Move(2,7,7,2));
                    //gameLogic.restoreGameState(moves);
                }

            }
        });

        font = new Font("Baskerville", Font.PLAIN, 32);

        setPreferredSize(new Dimension(900, 750));

        inGame = true;

        MouseHandler handler = new MouseHandler();
        addMouseListener(handler);

        try {
            tile_empty = ImageIO.read(new File("src/main/resources/tile_empty.png"));
            tile_x = ImageIO.read(new File("src/main/resources/tile_x.png"));
            tile_o = ImageIO.read(new File("src/main/resources/tile_o.png"));
            tile_tri = ImageIO.read(new File("src/main/resources/tile_tri.png"));
            tile_star = ImageIO.read(new File("src/main/resources/tile_star.png"));
            sidebar = ImageIO.read(new File("src/main/resources/sidebar.png"));
            bottombar = ImageIO.read(new File("src/main/resources/bottombar.png"));
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
                            g.drawImage(tile_empty, 20 * i, 20 * j, null);
                            break;
                        case 1:
                            g.drawImage(tile_x, 20 * i, 20 * j, null);
                            break;
                        case 2:
                            g.drawImage(tile_o, 20 * i, 20 * j, null);
                            break;
                        case 3:
                            g.drawImage(tile_tri, 20 * i, 20 * j, null);
                            break;
                        case 4:
                            g.drawImage(tile_star, 20 * i, 20 * j, null);

                            break;
                    }
                }
            }
            g.drawImage(sidebar, 600, 0, null);
            g.drawImage(bottombar, 0, 600, null);
            g.setFont(font);
            String playerSymbol = getSymbol(gameLogic.getPlayerSymbol());
            String inTurnSymbol = getSymbol(gameLogic.getSymbolInTurn());
            String players = "";
            switch (gameLogic.getPlayerAmount()) {
                case 1:
                    players = "???";
                    break;
                case 2:
                    players = "??????";
                    break;
                case 3:
                    players = "?????????";
                    break;
                case 4:
                    players = "????????????";
                    break;
            }
            g.drawString("Your symbol: " + playerSymbol, 35, 670);
            if (gameLogic.getGameWonBy() != 0) {
                String wonByWho = getSymbol(gameLogic.getGameWonBy());
                g.drawString("Game won by " + wonByWho + "!", 35, 705);
            } else {
                g.drawString("Current turn: " + inTurnSymbol, 35, 705);
            }
            g.drawString("Players: " + players, 625, 45);
            Font f = new Font("Baskerville", Font.PLAIN, 20);
            g.setFont(f);
            //g.drawString("Multicastmessage", 670, 35);
            //if (multicastMessage != null) {
            //    g.drawString(multicastMessage, 670, 35);
            //}

        }
        System.out.println("PaintComponent GameInterface");
        //repaint();
    }

    public String getSymbol(int i) {
        switch (i) {
            case 1:
                return "???";
            case 2:
                return "???";
            case 3:
                return "???";
            case 4:
                return "???";
        }
        return "fail";

    }

    private class MouseHandler implements MouseListener, MouseMotionListener {
        // MouseListener event handlers

        // handle event when mouse released immediately after press
        @Override
        public void mouseClicked(MouseEvent event) {
            if (inGame) {
                int clickX = (event.getX()) / 20;
                int clickY = event.getY() / 20;
                int i = gameLogic.tileIdAt(clickX, clickY);
                boolean succeeded;
                try {
                    gameLogic.placeTile(clickX, clickY, gameLogic.getPlayerSymbol(), true);
                } catch (IOException ex) {
                    Logger.getLogger(GameInterface.class.getName()).log(Level.SEVERE, null, ex);
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
