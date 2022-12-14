/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tictactoereal.networking;

import java.net.*;
import java.io.*;

/**
 *
 * @author bergmjul
 */
public class GreetingMatchmakingService {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    }

    public String sendMessage(String message) throws IOException {
        out.println(message);
        String response = in.readLine();
        return response;
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    //server.start(6666) ?  https://www.baeldung.com/a-guide-to-java-sockets
}
