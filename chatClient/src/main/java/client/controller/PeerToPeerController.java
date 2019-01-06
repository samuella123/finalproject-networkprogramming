/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import client.network.OutputHandler;
import client.network.PeerToPeerSocketHandling;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author darkferi
 */
public class PeerToPeerController {
    private final PeerToPeerSocketHandling clientSocket = new PeerToPeerSocketHandling();
    private boolean acceptedChat = false;
    
    public void connect(int serverPort, OutputHandler screenHandler, String userName) {
        CompletableFuture.runAsync(() -> {
            clientSocket.connect(serverPort, screenHandler, userName);
        });
    }
    
    public boolean listen(int targetPort, OutputHandler screenHandler, String userName) {
        acceptedChat = clientSocket.listen(targetPort, screenHandler, userName);
        return acceptedChat;
    }
    
    public void exit() {
        CompletableFuture.runAsync(() -> {
            clientSocket.disconnect();
        });
    }

    
    public void sendMessage(String text) throws IOException {
        CompletableFuture.runAsync(() -> {
            clientSocket.sendMessage(text);
        });
    } 
}
