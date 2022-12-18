
package com.mycompany.tictactoereal.networking;

/**
 *
 * @author bergmjul
 */
public class MulticastAddressReceivedEvent {
    
    private String multicastAddress;
    
    public MulticastAddressReceivedEvent(String multicastAddress) {
        this.multicastAddress = multicastAddress;
    }
    
    public String getMulticastAddress() {
        return this.multicastAddress;
    }
}
