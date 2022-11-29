
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

    public MulticastReceiver(GameLogic gamelogic) {
        this.gameLogic = gamelogic;
    }

    public void run() {
        try {
        socket = new MulticastSocket(4446);
        InetAddress group = InetAddress.getByName("230.0.0.0");
        socket.joinGroup(group);
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received = new String(
              packet.getData(), 0, packet.getLength());
            if ("end".equals(received)) {
                break;
            } else {
                //here separate the message wchich is "x,y,tileId" into parts
                String[] parts = received.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                int tileId = Integer.parseInt(parts[2]);
                gameLogic.placeTile(x, y, tileId);
            }
        }
        socket.leaveGroup(group);
        socket.close();
        } catch (Exception e) {
            System.out.println("EXCEPTION in MulticastReceiver" + e);
        }
    }
}