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
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        out.println("hello matchmaker");
        String inputLine = in.readLine();
        while (inputLine != null) {
            if (inputLine.equals(".")) {
                out.println("good bye");
                break;
            } else if (inputLine.equals("hello client")) {
                out.prinln("we are connected");
            }
            out.println(inputLine);
        }

    }

    public String sendMessage(String message) throws IOException {
        out.println(message);
        String response = in.readLine();
        System.out.println("Send message " + message);
        return response;
    }

    public void stopAndEnterGame() throws IOException {
        in.close();
        out.close();
        clientSocket.close();

    }

    //server.start(6666) ?  https://www.baeldung.com/a-guide-to-java-sockets
}
