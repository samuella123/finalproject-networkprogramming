/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.network;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 *
 * @author darkferi
 */
public class PeerToPeerSocketHandling {
    
    private Socket clientSocket; 
    private ServerSocket serverSocket;
    private InetSocketAddress serverPort;
    private static final int ESTABLISHING_TIMEOUT = 10000;
    private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_IDLE = 600000;
    private PrintWriter toPeer;
    private BufferedReader fromPeer1, fromPeer2;
    private Thread listenerThread;
    private final boolean autoFlush = true;
    private static final String EXIT = "exit";
    private boolean firstTime = true;
    
    
    public void connect(int port, OutputHandler screenHandler, String userName){
        try{
            //creating client socket to request for TCP connection
            clientSocket = new Socket();
            clientSocket.setSoLinger(true, LINGER_TIME);
            clientSocket.setSoTimeout(TIMEOUT_IDLE);
            
            serverPort = new InetSocketAddress(port);
            //sending the request for the connection
            clientSocket.connect(serverPort,ESTABLISHING_TIMEOUT);
            toPeer = new PrintWriter(clientSocket.getOutputStream(), autoFlush);
            fromPeer2 = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            listenerThread = new Thread(new PeerListener(screenHandler, fromPeer2, userName, clientSocket));
            listenerThread.start();
                        
        } catch (IllegalArgumentException e){
            System.out.println("Socket Argument Problem in connect()!!");
        } catch (SocketTimeoutException e){
            System.out.println("Timeout expired before connecting!!");
        } catch(IOException e){
            System.out.println("connect(): IOException (Server is not listening)!!");
        }
    }
    
    public boolean listen(int port, OutputHandler screenHandler, String userName){
        try{
            if(firstTime){
                serverSocket = new ServerSocket(port);
                firstTime = false;
            }
            clientSocket = new Socket();
            
            clientSocket = serverSocket.accept();
            clientSocket.setSoLinger(true, LINGER_TIME);
            clientSocket.setSoTimeout(TIMEOUT_IDLE);
            
            screenHandler.messageOnScreen("PeerToPeer Connection Establishment!!! Press ENTER...");
            toPeer = new PrintWriter(clientSocket.getOutputStream(), autoFlush);
            fromPeer1 = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            listenerThread = new Thread(new PeerListener(screenHandler, fromPeer1, userName, clientSocket));
            listenerThread.start();
            
        } catch (IOException e){
            System.out.println("GameServer: IOException occured"
                    + "(possible reason: server run already)");
        } 
        
        return true;
    }
     
    public void disconnect(){
        try{
            toPeer.println(EXIT);
            clientSocket.close();
        
        } catch(IOException e){
            System.out.println("CliendSocket > disconnect() > IOException!!");
        }
    }
    
    public void sendMessage(String text){
        toPeer.println(text);
    }
}
