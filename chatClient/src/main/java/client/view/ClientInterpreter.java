/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import client.controller.PeerToPeerController;
import client.network.ServerRestFul;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 *
 * @author darkferi
 */
public class ClientInterpreter implements Runnable{
    
    private static final String PROMPT = "> ";
    private ServerRestFul restful;
    private PeerToPeerController clientCntrl;
    private ConsoleOutput screenHandler;                                        
    private BufferedReader console;
    private static boolean ThreadStarted = false;
    public String userName;
    private int targetPort = 0;
    private int listenPort = 0;
    private boolean chatStarted = false;
    protected boolean acceptedChat = false;
    private boolean flagConnected = false;
    private boolean peer2peerMode;
    private boolean readLineEscape =false;
    
    private final ClientConsoleThreadSafety consoleManager = new ClientConsoleThreadSafety();
    
    
    public ClientInterpreter(boolean peer2peerMode){
        this.peer2peerMode = peer2peerMode;
    }
    
    
    @Override
    public void run(){
        
        ThreadStarted = true;
        clientCntrl = new PeerToPeerController();
        console = new BufferedReader(new InputStreamReader(System.in));
        String command;
        String [] userData;
        screenHandler = new ConsoleOutput();
        
        /////////////////////////////Welcome Message Method////////////////////////////
        welcomeMessage();
        ///////////////////////////////////////////////////////////////////////////////
        
        
        while(ThreadStarted){
            if (!peer2peerMode){
                try {
                    
                    if(acceptedChat){
                        // update busy status to restful
                        restful.updateStatus("busy");
                        peerToPeerStart(false);
                        continue;
                    }
                    
                    command = console.readLine();
                    command = command.trim();
                    userData = command.split(" ");
                    userData[0] = userData[0].toLowerCase();

                //Legal command

                    ////////////////////////////////////////////REGISTER////////////////////////////////////////////////
                        
                    if(!readLineEscape){
                    
                        if(userData[0].equalsIgnoreCase("register") && userData.length == 3){

                            try
                            {
                                restful.registerUser(userData[1],userData[2]);
                                consoleManager.println("Successful Register!!!\n");
                            }
                            catch(Exception ex)
                            {
                               consoleManager.println(ex.getMessage());
                            }

                        }

                        //////////////////////////////////////////UNREGISTER////////////////////////////////////////////////

                        else if(userData[0].equalsIgnoreCase("unregister") && userData.length == 3){

                            try
                            {
                                restful.deleteUser(userData[1],userData[2]);
                                consoleManager.println("Successful Unregister!!!\n");
                            }
                            catch(Exception ex)
                            {
                               consoleManager.println(ex.getMessage());
                            }

                        }

                        ////////////////////////////////////////////LOGIN///////////////////////////////////////////////////

                        else if(userData[0].equalsIgnoreCase("login") && userData.length == 4){
                            if(!flagConnected){
                                
                                try
                                {
                                    restful = new ServerRestFul(userData[1],userData[2],userData[3]);
                                    
                                    consoleManager.println("Successful Login!!!\n");
                                    userName = userData[1];
                                    flagConnected = true;
                                    listenPort = new Integer(userData[3]);
                                    Executor pool = ForkJoinPool.commonPool();
                                    pool.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            acceptedChat = clientCntrl.listen(listenPort, screenHandler, userName);
                                            readLineEscape = true;
                                        }
                                    });
                                }
                                catch(Exception ex)
                                {
                                   consoleManager.println(ex.getMessage());
                             
                                }
                                
                                /*
                                userName = "Darkferi";

                                    consoleManager.println("Successful Login!!!\n");
                                    flagConnected = true;
                                    listenPort = 5202;
                                    Executor pool = ForkJoinPool.commonPool();
                                    pool.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            acceptedChat = clientCntrl.listen(listenPort, screenHandler, userName);
                                            readLineEscape = true;
                                        }
                                    });

                                */
                            }
                            else{
                                consoleManager.println("You are already logged in!!!.\n");        
                            }
                        }

                        ////////////////////////////////////////////LOGOUT//////////////////////////////////////////////////

                        else if(userData[0].equalsIgnoreCase("logout") && userData.length == 1){

                            try
                            {
                                if(restful==null)
                                {
                                    throw new Exception("You haven't logged in yet. First Login!!!\n");
                                }
                                                            
                                restful.exitRestful();
                                restful = null;
                                
                                consoleManager.println("Successful Logout!!!\n");
                                flagConnected = false;
                            }
                            catch(Exception ex)
                            {
                                consoleManager.println(ex.getMessage()); 
                            }

                        }
                        ////////////////////////////////////////////LIST////////////////////////////////////////////////////

                        else if(userData[0].equalsIgnoreCase("list") && userData.length == 1){
                            
                            try
                            {
                                if(restful==null)
                                {
                                    throw new Exception("You haven't logged in yet. First Login!!!\n");
                                }
                                      
                                consoleManager.println("");
                                String[] res = restful.getOnlineClients();
                                
                                consoleManager.println("Online users:");

                                for(int i=0;i<res.length;i++)
                                {
                                    consoleManager.println(res[i]);
                                }
                                
                                consoleManager.println("");

                            }
                            catch(Exception ex)
                            {
                                consoleManager.println(ex.getMessage()); 
                            }
                            
                            
                        }

                        ////////////////////////////////////////////CHAT////////////////////////////////////////////////////

                        else if(userData[0].equalsIgnoreCase("chat") && userData.length == 2){
                            
                            try
                            {
                                
                                if(restful==null)
                                {
                                    throw new Exception("You haven't logged in yet. First Login!!!\n");
                                }
                                
                                consoleManager.println("");
                                targetPort = restful.chatReq(userData[1]);
                                peerToPeerStart(true);
                            }
                            catch(Exception ex)
                            {
                                consoleManager.println(ex.getMessage()); 
                            }
                            /*
                            targetPort = 5202;
                            userName = "Shimoool";

                            switch (targetPort) {
                                case -1:
                                    consoleManager.println("The requested user does not exist!!!\n");
                                    break;
                                case -2:
                                    consoleManager.println("The requested user is offline!!!\n");
                                    break;
                                case -3:
                                    consoleManager.println("The requested user is busy!!!\n");
                                    break;
                                default:
                                    peerToPeerStart(true);
                                    break;
                            }*/
                        }

                        ///////////////////////////////////////////HELP/////////////////////////////////////////////////////

                        else if(userData[0].equalsIgnoreCase("help") && userData.length == 1){
                            
                            printHelpMessage();

                        }

                        ///////////////////////////////////////////EXIT/////////////////////////////////////////////////////

                        else if(userData[0].equalsIgnoreCase("exit") && userData.length == 1){
                            if(flagConnected){
                                //logout first
                                flagConnected = false;
                            } 
                            ThreadStarted = false;
                            consoleManager.println("Successful EXIT!!!\n");
                        }

                    //Illegal command

                        else{
                            consoleManager.println("Illegal Command...\n");
                        }
                        
                    }
                    
                    else{
                        readLineEscape = false;
                    }


                } catch (IOException e) {
                    System.out.println("ClientVeiw > run() > IOException");
                }
            }
           
        }      
    }
    
    private void peerToPeerStart(boolean cilentType){
        
        peer2peerMode = true;
        chatStarted = true;
        String text;
        if(cilentType){ 
            //update status to restful
            restful.updateStatus("busy");
            clientCntrl.connect(targetPort, screenHandler, userName);
        }
        
        consoleManager.println("\n********************************************"
                + "Chat Started********************************************\n");
        
        while(chatStarted){
            try {
                text = console.readLine();
               
                if(text.startsWith("exit") && text.length()==4){
                    consoleManager.println("\n*********************************************"
                +  "You Exited*********************************************\n");
                    clientCntrl.exit();
                    //update status to restful
                    restful.updateStatus("online");
                    if(!cilentType){
                        acceptedChat = false;
                        Executor pool = ForkJoinPool.commonPool();
                        pool.execute(new Runnable() {
                            @Override
                            public void run() {
                                 acceptedChat = clientCntrl.listen(listenPort, screenHandler, userName);
                            }
                        });
                    }
                    chatStarted = false;
                    peer2peerMode = false;
                    
                }
                
                else{
                    text = userName + PROMPT + text;
                    clientCntrl.sendMessage(text);
                }
                           
            } catch (IOException e) {
                System.out.println("ClientVeiwPeerToPeer > run() > IOException");
            }
        }
    }
    
    private void welcomeMessage(){
        String s = "-----------------------------------------------"
                    + "------------------------------------------------------------------------------------"
                    + "\n\t\t\t\t\t\tWelcome to Peer to Peer Chat Program\n\n"
                    + "In this application you are allowed to chat with the users already registered in the server.\n"
                    + "First you need to create an account using REGISTER command if you don't have any account in our user database.\n"
                    + "Otherwise you just need to login into our server using LOGIN command.\n"
                    + "After login, you can see list of online users in the application using LIST command.\n"
                    + "You are able to request for a chat with a user who is online and not busy (chatting with other user at current time).\n"
                    + "Note that as soon as you start a chat with someone and create a peer to peer connection, your state will change to \"busy\".\n"
                    + "After exiting the chat window, you will go back to the main sectoin of the application and your state wiil be updated to \"online\".\n"
                    + "To find the application commands and their syntax print HELP...\n\n";
         
         consoleManager.println(s);
    }
    
    private void printHelpMessage(){
        String s = "\n-----------------------------------------------"
                    + "------------------------------------------------------------------------------------\n" +
                    "Legal Commands in Application ([option]: Mandatory) \n\n" +
                    "01) REGISTER   --> register [username] [password]\n" +
                    "                   note: give your username and password for registration. \n\n" +
                    "02) UNREGISTER --> unregister [username] [password]\n" + 
                    "                   note: give your username and password to delete your account. \n\n" +
                    "03) LOGIN      --> login  [username] [password] [port]\n" + 
                    "                   note: used for logging into the server. the user needs to announce his/her listening port.  \n\n" +
                    "04) LOGOUT     --> logout\n" +
                    "                   note: used for logging out from the server. \n\n" +
                    "05) LIST       --> list\n" +
                    "                   note: this function shows online users in the chat program. \n\n" +
                    "06) CHAT       --> chat [target_username]\n" +
                    "                   note: used for requesting a chat with a target user. \n\n" +
                    "07) HELP       --> help\n" +
                    "                   note: used for showing command guidline. \n\n" +
                    "08) EXIT       --> exit\n" +
                    "                   note: used in order to quit the application. \n\n" +
                    "\n-----------------------------------------------"
                    + "------------------------------------------------------------------------------------\n";
        consoleManager.println(s);
    }
    
}

