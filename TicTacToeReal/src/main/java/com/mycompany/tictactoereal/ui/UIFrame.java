
package com.mycompany.tictactoereal.ui;

import com.mycompany.tictactoereal.networking.SocketWithMatchmaker;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Julia Bergman <bergmjul>
 */
public class UIFrame extends JFrame {

    public UIFrame() {
        WaitingInterface waitingI = new WaitingInterface();

        waitingI.setStartButtonClicked(event -> {
            SocketWithMatchmaker socket = new SocketWithMatchmaker("127.0.0.1", 6666);
            socket.setMulticastAddressReceived(event2 -> {
                 String multicastAddress = event2.getMulticastAddress();
                 System.out.println("MulticastAddress " + multicastAddress);
                //and because Swing components can only be modified in Swing thread ->
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        System.out.println("UIFrame multicastAddress \""+ multicastAddress+ "\"");
                        String[] parts = multicastAddress.split(",");
                        String multicastIP = parts[0];
                        int playerNumber = Integer.valueOf(parts[1]);
                        String[] userHashes = parts[2].split(":");

                        GameInterface gameI = new GameInterface(multicastIP, playerNumber, userHashes);
                        remove(waitingI);
                        add(gameI);
                        revalidate();
                        pack();
                        repaint();
                    }
                });
            });
            socket.start();
        });
        add(waitingI);
        setResizable(false);
        pack();

        setTitle("");
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
