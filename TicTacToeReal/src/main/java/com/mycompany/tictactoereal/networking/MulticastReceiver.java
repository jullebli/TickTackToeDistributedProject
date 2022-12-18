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
        System.out.println("MulticastReceiver made");
    }

    @Override
    public void run() {
        System.out.println("MulticastReceiver.run");
        try {
            socket = new MulticastSocket(4446);
            System.out.println("MulticastReceiver \"" + address + "\"");
            InetAddress group = InetAddress.getByName(address);
            socket.joinGroup(group);

            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(
                        packet.getData(), 0, packet.getLength());

                if ("end".equals(received)) {
                    break;
                }
/*
                    System.out.println("Switching to " + parts[0]);

                    MulticastPublisher pub = this.gameLogic.getPublisher();

                    group = InetAddress.getByName(parts[0]);
                    socket.joinGroup(group);

                    pub.setAddress(parts[0]);
                    this.address = parts[0];
                    System.out.println("Setting playernumber to " + pos);
                    this.gameLogic.setPlayerSymbol(pos);

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
                    gameLogic.placeTile(x, y, tileId, false);
                }
            }
            socket.leaveGroup(group);
            socket.close();
*/
            System.out.println("received:" + received);
            if(!HeaderManager.validateHeader(received, gameLogic)) continue;
            
            String message = HeaderManager.getMessage(received);
            handleMessage(message);
        }
        socket.leaveGroup(group);
        socket.close();
        } catch (Exception e) {
            System.out.println("EXCEPTION in MulticastReceiver" + e);
        }
    }

    private void handleMessage(String received) throws IOException {
        
        String[] parts = received.split(",");
        
        if (received.contains(".")) { // Create a better check later
            // THIS IS PROBABLY AN IP_ADDRESS
            int pos = this.findPlayerPosition(parts);
            System.out.println(pos);

            if (pos == 0) return;

            ipChange(parts[0]);

            System.out.println("Setting playernumber to " + pos);
            this.gameLogic.setPlayerSymbol(pos);

            //Set the game size 
            this.gameLogic.setPlayerAmount(parts.length - 1);

            String[] playerList = Arrays.copyOfRange(parts, 1, parts.length);
            this.gameLogic.setPlayerArray(playerList);

            Pinger pinger = new Pinger(this.gameLogic);
            gameLogic.setPinger(pinger);
            pinger.start();

        } else if (received.length() == 10 && received.split(",").length == 1) {
            // THIS IS PROBABLY A USERHASH, should be ignored
            Pinger ping = this.gameLogic.getPinger();
            if(ping == null) return;

            ping.playerReset(received);
        } else {
            //here separate the message which is "x,y,tileId" into parts
            if (received.length() < 5) return; // not valid move information
            
            if (gameLogic.getTurnNumber() != Integer.parseInt(parts[4]) || gameLogic.getSymbolInTurn() != Integer.parseInt(parts[3])) return; // Add failure resolution here 
            
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int tileId = Integer.parseInt(parts[2]);
            
            System.out.println("Someone made a move");
            gameLogic.placeTile(x, y, tileId,false);
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
        int pos = 0;

        for (int i=1;i< parts.length;i++) {
            if (parts[i].equals(this.gameLogic.getUserHash())) {
                pos = i;
                break;
            }
        }
        return pos;
    }
}


//CHECK!!!
