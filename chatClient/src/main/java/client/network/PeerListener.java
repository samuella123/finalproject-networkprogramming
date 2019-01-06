/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author darkferi
 */
public class PeerListener implements Runnable {
        private final OutputHandler outputHandler;
        private final BufferedReader fromServer;
        private final String userName;
        private final Socket clientSocket;
        
        public PeerListener(OutputHandler outputHandler,BufferedReader fromServer, String userName, Socket clientSocket) {
            this.outputHandler = outputHandler;
            this.fromServer = fromServer;
            this.userName = userName;
            this.clientSocket = clientSocket;
        }

        @Override
        public void run(){
            try {
                String str;
                for (;;) {
                    
                    str = fromServer.readLine();
                    if(str == null){
                        //do nothing    
                    }
                    else if(str.equalsIgnoreCase("exit")){
                        clientSocket.close();
                        System.out.println("\n*********************************************"
                + "Chat Ended*********************************************\n");
                        System.out.println("Write \"exit\" in order to quit the chat window...\n");
                    }
                    else{
                        outputHandler.messageOnScreen(str);
                     }
                    
                }
            } catch (IOException e) {
                
            } 
        }

}