package com.mycompany.tictactoereal.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author bergmjul
 */
public class MulticastPublisher {

    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;
    private String address;

    public MulticastPublisher(String address) {
        this.address = address;
        //System.out.println("made multicastReceiver \"" + address + "\"");
    }

    public void multicast(String multicastMessage) throws IOException {
        //System.out.println("Multicasting");
        multicast(multicastMessage, address);
    }

    public void multicast(String multicastMessage, String host) throws IOException {
        socket = new DatagramSocket();
        group = InetAddress.getByName(host);
        buf = multicastMessage.getBytes();

        //System.out.println("Someone is multicasting");
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, group, 4446);
        socket.send(packet);
        socket.close();
    }

    public void setAddress(String adr) {
        address = adr;
    }
}
