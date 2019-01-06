/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.startup;

import client.view.ClientInterpreter;




/**
 *
 * @author darkferi
 */
public class StartClient {
    
    
    public static void main(String[] args){
            
            Thread userInterfaceThread = new Thread(new ClientInterpreter(false));
            userInterfaceThread.start();

    }
}
