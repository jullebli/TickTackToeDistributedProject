package com.mycompany.tictactoereal.networking;

import com.mycompany.tictactoereal.gamelogic.GameLogic;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
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
        //System.out.println("MulticastReceiver made");
    }

    @Override
    public void run() {
        //System.out.println("MulticastReceiver.run");
        try {
            socket = new MulticastSocket(4446);
            //System.out.println("MulticastReceiver \"" + address + "\"");
            InetAddress group = InetAddress.getByName(address);
            socket.joinGroup(group);

            while (true) {
                System.out.println("inside while loop");
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(
                        packet.getData(), 0, packet.getLength());
                System.out.println("made received");
                if (received.equals("end")) {
                    break;
                }

                System.out.println("received and userHash:" + received +"/" + gameLogic.getUserHash());
                if (!HeaderManager.validateHeader(received, gameLogic)) {
                    continue;
                }

                if (received.contains("suggest") || received.contains("consensus")) {
                    System.out.println("handled different");
                    handleSuggestionMessage(received);
                } else {
                    String message = HeaderManager.getMessage(received);
                    //System.out.println("handleMessage called with message" + message);
                    handleMessage(message);
                }
            }
            socket.leaveGroup(group);
            socket.close();
        } catch (Exception e) {
            System.out.println("EXCEPTION in MulticastReceiver" + e);
        }
    }

    private void handleMessage(String received) throws IOException {

        String[] parts = received.split(",");

        //System.out.println("parts[parts.length - 1]" + parts[parts.length - 1]);
        if (received.length() == 10 && received.split(",").length == 1) {
            // THIS IS PROBABLY A USERHASH, should be ignored
            Pinger ping = this.gameLogic.getPinger();
            if (ping == null) {
                return;
            }

            ping.playerReset(received);
        } else {
            //here separate the message which is "x,y,tileId" into parts
            if (received.length() < 5) {
                return; // not valid move information
            }
            if (gameLogic.getTurnNumber() != Integer.parseInt(parts[4]) || gameLogic.getSymbolInTurn() != Integer.parseInt(parts[3])) {
                return; // Add failure resolution here
            }
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int tileId = Integer.parseInt(parts[2]);

            System.out.println("Someone made a move");
            gameLogic.placeTile(x, y, tileId, false);
        }
    }

    private void handleSuggestionMessage(String received) throws IOException {
        String[] strArr = received.split(";");

        System.out.println("parts[parts.length - 1]" + strArr[strArr.length - 1]);
        if (strArr[strArr.length - 1].contains("suggestKickout:")) {
            System.out.println("I got a kicking out message");
            String[] message = strArr[strArr.length - 1].split(":");
            System.out.println("message[1]" + message[1]);
            if (!gameLogic.haveAlreadyVotedForKickingOut(strArr[0])) {
                //voteForKickingOutForUserFromUser(String voterUserHash, String votedUserHash)
                gameLogic.voteForKickingOutForUserFromUser(strArr[0], message[1]);
            }
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

        for (int i = 1; i < parts.length; i++) {
            if (parts[i].equals(this.gameLogic.getUserHash())) {
                pos = i;
                break;
            }
        }
        return pos;
    }
}
