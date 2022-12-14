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
public class GreetMatchmakingService {
    private ServerSocket matchmakerSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String myUserHash;
    
    public void start(int port) throws IOException {
        matchmakerSocket = new ServerSocket(port);
        clientSocket = matchmakerSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String greeting = in.readLine();
          if ("hello matchmaker".equals(greeting)) {
              out.println("hello player");
          } else {
              out.println("unrecognised greeting");
          }
    }
    
    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        matchmakerSocket.close();
    }
    
    //server.start(6666) ?  https://www.baeldung.com/a-guide-to-java-sockets
    
}
