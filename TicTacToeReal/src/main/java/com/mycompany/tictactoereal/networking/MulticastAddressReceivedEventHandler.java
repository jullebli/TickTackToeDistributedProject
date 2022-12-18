
package com.mycompany.tictactoereal.networking;

import com.mycompany.tictactoereal.networking.MulticastAddressReceivedEvent;

/**
 *
 * @author bergmjul
 */
public interface MulticastAddressReceivedEventHandler {
    
    public void handle(MulticastAddressReceivedEvent event);
}
