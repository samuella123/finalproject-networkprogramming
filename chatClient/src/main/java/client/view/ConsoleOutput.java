
//no change

package client.view;

import client.network.OutputHandler;


public class ConsoleOutput implements OutputHandler {                      
    
    private ClientConsoleThreadSafety consoleManager = new ClientConsoleThreadSafety();
    
    
    @Override
    public void messageOnScreen(String message) {
        consoleManager.println(message);
    }
    
    @Override
    public void charOnScreen(String message) {
        consoleManager.print(message);
    }
    
}
