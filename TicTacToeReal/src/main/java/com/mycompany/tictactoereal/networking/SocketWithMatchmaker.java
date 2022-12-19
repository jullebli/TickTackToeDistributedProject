
package com.mycompany.tictactoereal.networking;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bergmjul
 */
public class SocketWithMatchmaker extends Thread {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String ip;
    private int port;
    private MulticastAddressReceivedEventHandler multicastAddressReceivedEventHandler;

    public SocketWithMatchmaker(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            System.out.println("SocketWithMatchmaker.start(ip, port) " + ip + " " + port);
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println("hello matchmaker");
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                //inputLine = in.readLine();
                System.out.println("Client received message:" + inputLine);
                if (inputLine.equals(".")) {
                    sendMessage("good bye");
                    break;
                } else if (inputLine.equals("hello client")) {
                    sendMessage("we are connected");

                } else if (inputLine.equals("You can start game")) {
                    String multicastAddress = in.readLine();
                    sendMessage("Entering game");
                    System.out.println("Client send Entering game and got multicastAddress: " + multicastAddress);
                    closeSockets();
                    multicastAddressReceivedEventHandler.handle(new MulticastAddressReceivedEvent(multicastAddress));
                    break;
                }
            }
            System.out.println("Client went out of while loop");
        } catch (IOException ex) {
            Logger.getLogger(SocketWithMatchmaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //setReceivedStartGameMessage
    public void sendMessage(String message) throws IOException {
        out.println(message);
        System.out.println("Client sent message " + message);
    }

    public void closeSockets() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public void setMulticastAddressReceived(MulticastAddressReceivedEventHandler handler) {
        this.multicastAddressReceivedEventHandler = handler;
    }
}
