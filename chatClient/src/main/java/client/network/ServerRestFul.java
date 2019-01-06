/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.network;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Entity;

/**
 *
 * @author darkferi
 */
public class ServerRestFul {
    
    public static String LOCATION =  "http://130.229.163.129:8080/chatServer/webresources/users";
    
    private String username;
    private String password;
    private String ipport;
    
    public ServerRestFul(String id, String pass,String ipport) throws Exception
    {
        Client apiClient = ClientBuilder.newClient();
        String usernameAndPassword = id + ":" + pass;
        String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );

        
        String bodyJSON = 
            "  {" +
            "   \"type\": \""+ "body" +"\", " +
            "   \"status\": \""+ "online" +"\", " +
            "   \"ipport\": \""+ ipport +"\" " +
            " }";
        
        String myJson = apiClient
                .target(LOCATION+"/"+id)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", authorizationHeaderValue)
                .put(Entity.entity(bodyJSON, MediaType.APPLICATION_JSON), String.class);
        
        
        //Response response
        JsonReader reader = Json.createReader(new StringReader(myJson));
        JsonObject responseObject = reader.readObject();
        reader.close();
        
        String res = responseObject.getString("type");
        
        if(res.equals("result"))
        {
            this.username = id;
            this.password = pass;
            this.ipport = ipport;
            return;
        }
        else
            throw new Exception(responseObject.getString("message"));
    }
    
    
    public void updateStatus(String stat)
    {
        Client apiClient = ClientBuilder.newClient();
        String usernameAndPassword = username + ":" + password;
        String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );

        
        String bodyJSON = 
            "  {" +
            "   \"type\": \""+ "body" +"\", " +
            "   \"status\": \""+ stat +"\", " +
            "   \"ipport\": \""+ ipport +"\" " +
            " }";
        
        String myJson = apiClient
                .target(LOCATION+"/"+username)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", authorizationHeaderValue)
                .put(Entity.entity(bodyJSON, MediaType.APPLICATION_JSON), String.class);
        
        
        //Response response
        JsonReader reader = Json.createReader(new StringReader(myJson));
        JsonObject responseObject = reader.readObject();
        reader.close();
        
        String res = responseObject.getString("type");
        
        if(res.equals("result"))
        {
            return;
        }
        else
        {
            //throw new Exception(responseObject.getString("message"));
        }
    }
    
    
    public void exitRestful() throws Exception
    {
        Client apiClient = ClientBuilder.newClient();
        String usernameAndPassword = username + ":" + password;
        String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );

        
        String bodyJSON = 
            "  {" +
            "   \"type\": \""+ "body" +"\", " +
            "   \"status\": \""+ "offline" +"\", " +
            "   \"ipport\": \""+ ipport +"\" " +
            " }";
        
        String myJson = apiClient
                .target(LOCATION+"/"+username)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", authorizationHeaderValue)
                .put(Entity.entity(bodyJSON, MediaType.APPLICATION_JSON), String.class);
        
        
        //Response response
        JsonReader reader = Json.createReader(new StringReader(myJson));
        JsonObject responseObject = reader.readObject();
        reader.close();
        
        String res = responseObject.getString("type");
        
        if(res.equals("result"))
        {
            return;
        }
        else
            throw new Exception(responseObject.getString("message"));
    }
    
    
    public static void registerUser(String us,String ps) throws Exception
    {
        Client apiClient = ClientBuilder.newClient();
        
        
        String bodyJSON = 
            "  {" +
            "   \"type\": \""+ "body" +"\", " +
            "   \"username\": \""+ us +"\", " +
            "   \"password\": \""+ ps +"\", " +
            "   \"ipport\": \""+ "0" +"\" " +
            " }";
        
        
        String myJson = apiClient
                .target(LOCATION)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(bodyJSON, MediaType.APPLICATION_JSON), String.class);
        
        
        //Response response
        JsonReader reader = Json.createReader(new StringReader(myJson));
        JsonObject responseObject = reader.readObject();
        reader.close();
        
        String res = responseObject.getString("type");
        
        if(res.equals("result"))
        {
            return;
        }
        else
            throw new Exception(responseObject.getString("message"));
    }
    
    
    public int chatReq(String cli) throws Exception
    {
        Client apiClient = ClientBuilder.newClient();
    
        String usernameAndPassword = username + ":" + password;
        String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );
        
        String myJson = apiClient
                .target(LOCATION+"/"+cli) 
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", authorizationHeaderValue)
                .get(String.class);
        
        //Response response
        JsonReader reader = Json.createReader(new StringReader(myJson));
        JsonObject responseObject = reader.readObject();
        reader.close();
        
        String res = responseObject.getString("type");
        
        if(res.equals("result"))
        {
            String status = responseObject.getString("status");
            if(status.equals("offline"))
            {
                throw new Exception("Your Requested User is Offline.\n");
            }
            else if(status.equals("busy"))
            {
                throw new Exception("Your Requested User is busy.\n");
            }
            else
            {
                return new Integer(responseObject.getString("ipport"));
            }
        }
        else
            throw new Exception(responseObject.getString("message"));
    }
    
    
    public String[] getOnlineClients()
    {
        
        Client apiClient = ClientBuilder.newClient();
    
        String usernameAndPassword = username + ":" + password;
        String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );
        
        String myJson = apiClient
                .target(LOCATION+"/online")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", authorizationHeaderValue)
                .get(String.class);
        
        //Response response
        JsonReader reader1 = Json.createReader(new StringReader(myJson));
        JsonArray res = reader1.readArray();
        reader1.close();
        
        String[] stringRes = new String[res.size()];
        for(int i=0;i<res.size();i++)
        {
            stringRes[i] = res.getJsonObject(i).getString("username");
        }
        
        return stringRes;
    
    }
    
    
    public static void deleteUser(String us,String ps) throws Exception
    {
        Client apiClient = ClientBuilder.newClient();
        
        String usernameAndPassword = us + ":" + ps;
        String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );
        
        
        String myJson = apiClient
                .target(LOCATION+"/"+us)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", authorizationHeaderValue)
                .delete(String.class);
        
        
        //Response response
        JsonReader reader = Json.createReader(new StringReader(myJson));
        JsonObject responseObject = reader.readObject();
        reader.close();
        
        String res = responseObject.getString("type");
        
        if(res.equals("result"))
        {
            return;
        }
        else
            throw new Exception(responseObject.getString("message"));
        
    }
}
