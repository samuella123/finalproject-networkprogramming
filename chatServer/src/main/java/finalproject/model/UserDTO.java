/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject.model;

/**
 *
 * @author Samie
 */
public interface UserDTO {

    String getId();

    String getPassword();

    String getStatus();

    String getIpport();
    
    void setStatus(String st);
    
    void setIpport(String ip);
}
