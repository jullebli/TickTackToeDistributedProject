
package com.mycompany.tictactoereal.networking;

import com.mycompany.tictactoereal.gamelogic.GameLogic;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

/**
 *
 * @author bergmjul
 */
public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];
    private GameLogic gameLogic;
    private String address;

    public MulticastReceiver(GameLogic gamelogic, String address) {
        this.gameLogic = gamelogic;
        this.address = address;
    }

    @Override
    public void run() {
        try {
        socket = new MulticastSocket(4446);
        InetAddress group = InetAddress.getByName(address);
        socket.joinGroup(group);
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received = new String(
              packet.getData(), 0, packet.getLength());
            if ("end".equals(received)) {
                break;
            } else if (received.contains(".")) { // Create a better check later
                // THIS IS PROBABLY AN IP_ADDRESS
                
                String[] parts = received.split(";");
                ipChange(parts[0]);
                
                int pos = this.findPlayerPosition(parts);
                
                System.out.println("Setting playernumber to " + pos);
                this.gameLogic.setPlayerSymbol(pos);
                
                //Set the game size 
                this.gameLogic.setPlayerAmount(parts.length - 1);
                
            } else if (received.length() == 10 && received.split(",").length == 1) {
                // THIS IS PROBABLY A USERHASH, should be ignored
                continue;
            } else {
                //here separate the message which is "x,y,tileId" into parts
                String[] parts = received.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                int tileId = Integer.parseInt(parts[2]);
                System.out.println("Someone made a move");
                gameLogic.placeTile(x, y, tileId,false);
            }
        }
        socket.leaveGroup(group);
        socket.close();
        } catch (Exception e) {
            System.out.println("EXCEPTION in MulticastReceiver" + e);
        }
    }
    
    private void ipChange(String address) throws IOException {

        System.out.println("Switching to " + address);

        MulticastPublisher pub = this.gameLogic.getPublisher();

        InetAddress group = InetAddress.getByName(address);
        socket.joinGroup(group);

        pub.setAddress(address);
        this.address = address;
    }
    
    private int findPlayerPosition(String[] parts) {
        int pos = 1;

        for (int i=1;i< parts.length;i++) {
            if (parts[i].equals(this.gameLogic.getUserHash())) {
                pos = i;
                break;
            }
        }
        return pos;
    }
    
}