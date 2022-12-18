/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tictactoereal.networking;

import com.mycompany.tictactoereal.ui.WaitingInterface;
import java.net.*;
import java.io.*;

/**
 *
 * @author bergmjul
 */
public class SocketWithMatchmaker {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private WaitingInterface waitingI;

    public SocketWithMatchmaker(WaitingInterface waiting) {
        this.waitingI = waiting;
    }

    public void start(String ip, int port) throws IOException {
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

                waitingI.setReceivedStartGameMessage(true);
                stopAndEnterGame();
            }
        }
        System.out.println("Client went out of while loop");
    }

    //setReceivedStartGameMessage
    public void sendMessage(String message) throws IOException {
        out.println(message);
        //String response = in.readLine();
        System.out.println("Client sent message " + message);
        //return response;
    }

    public void stopAndEnterGame() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        waitingI.startGame();

    }

    //server.start(6666) ?  https://www.baeldung.com/a-guide-to-java-sockets
}
