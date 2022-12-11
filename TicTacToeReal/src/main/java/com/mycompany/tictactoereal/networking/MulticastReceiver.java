
package com.mycompany.tictactoereal.networking;

import com.mycompany.tictactoereal.gamelogic.GameLogic;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

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
            } else if (received.contains(".")) {
                // THIS IS PROBABLY AN IP_ADDRESS
                break;
            } else if (received.length() == 10 && received.split(",").length == 1) {
                // THIS IS PROBABLY A USERHASH
                break;
            } {
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
}