/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject.chatserver;

import javax.json.Json;
import java.io.StringReader;
import javax.json.*;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.*;
import javax.ejb.EJB;
import sun.misc.BASE64Decoder;

import finalproject.model.MyUser;
import finalproject.integration.UserDAO;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * REST Web Service
 *
 * @author Samie
 */
@Path("users")
public class UsersResource {

    @Context
    private UriInfo context;
    
    @EJB
    private UserDAO userDAO;
    
    public String st = "offline";

    /**
     * Creates a new instance of UsersResource
     */
    public UsersResource() {
    }

    /**
     * Retrieves representation of an instance of finalproject.chatserver.UsersResource
     * @return an instance of javax.json.jsonObject
     */    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray getAllJson(@HeaderParam("authorization") String authString) 
    {
        String resJSON = "[ ";
        try
        {
            authenticateUser(authString,"");
            MyUser[] u = userDAO.findAllUsers();
            
            for(int i=0;i<u.length;i++)
            {
                String tmp = 
                "  {" +
                "   \"type\": \""+ "result" +"\", " +        
                "   \"username\": \""+ u[i].getId() +"\", " +
                "   \"status\": \""+ u[i].getStatus() +"\" " +
                " }";
                
                if(i==(u.length-1))
                    resJSON = resJSON + tmp + " ]";
                else    
                    resJSON = resJSON + tmp + ",";
            }
        }
        catch(Exception ex)
        {
            resJSON = 
            "[  {" +
            "   \"type\": \""+ "error" +"\", " +        
            "   \"message\": \""+ ex.getMessage() +"\" " +
            " } ]";
        }
             
        System.out.println(resJSON);
        
        JsonReader reader = Json.createReader(new StringReader(resJSON));
        JsonArray res = reader.readArray();
        reader.close();
        return res;
        
    }
    
    @GET
    @Path("online")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray getOnlinesJson(@HeaderParam("authorization") String authString) 
    {
        
        String resJSON = "[ ";
        try
        {
            authenticateUser(authString,"");
            MyUser[] u = userDAO.findOnlineUsers();
            
            for(int i=0;i<u.length;i++)
            {
                String tmp = 
                "  {" +
                "   \"type\": \""+ "result" +"\", " +
                "   \"username\": \""+ u[i].getId() +"\", " +
                "   \"status\": \""+ u[i].getStatus() +"\" " +
                " }";
                
                if(i==(u.length-1))
                    resJSON = resJSON + tmp + " ]";
                else    
                    resJSON = resJSON + tmp + ",";
            }
        }
        catch(Exception ex)
        {
            resJSON = 
            "[  {" +
            "   \"type\": \""+ "error" +"\", " +        
            "   \"message\": \""+ ex.getMessage() +"\" " +
            " } ]";
        }
        
        
        JsonReader reader = Json.createReader(new StringReader(resJSON));
        JsonArray res = reader.readArray();
        reader.close();
        return res;
        
    }
    
    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getByIdJson(@PathParam("username") String userName,@HeaderParam("authorization") String authString) 
    {
        
        String resJSON;
        try
        {
            authenticateUser(authString,"");
            MyUser u = userDAO.findUserByID(userName);
            
            resJSON = 
            "  {" +
            "   \"type\": \""+ "result" +"\", " +
            "   \"username\": \""+ u.getId() +"\", " +
            "   \"status\": \""+ u.getStatus() +"\", " +
            "   \"ipport\": \""+ u.getIpport() +"\" " +
            " }"; 
        }
        catch(Exception ex)
        {
            resJSON = 
            "  {" +
            "   \"type\": \""+ "error" +"\", " +        
            "   \"message\": \""+ ex.getMessage() +"\" " +
            " } ";
        }
             
        JsonReader reader = Json.createReader(new StringReader(resJSON));
        JsonObject personObject = reader.readObject();
        reader.close();
        return personObject;
        
    }

    /**
     * PUT method for updating or creating an instance of UsersResource
     * @param content representation for the resource
     */
    @PUT
    @Path("{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject putByIdJson(@PathParam("username") String userName,@HeaderParam("authorization") String authString,JsonObject content) 
    {
        
        String response = "successfull";
        String resJSON = "";
        try
        {
            authenticateUser(authString,userName);
            String status = content.getString("status");
            String ipport = content.getString("ipport");
            
            userDAO.updateStatusById(userName, status, ipport);
            
            resJSON = 
            "  {" +
            "   \"type\": \""+ "result" +"\", " +
            "   \"message\": \""+ response +"\" " +
            " }";
        }
        catch(Exception ex)
        {
            resJSON = 
            "  {" +
            "   \"type\": \""+ "error" +"\", " +        
            "   \"message\": \""+ ex.getMessage() +"\" " +
            " } ";
        }
        

         
        JsonReader reader = Json.createReader(new StringReader(resJSON));
        JsonObject personObject = reader.readObject();
        reader.close();
        return personObject;
        
    }
    
    @DELETE
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject deleteByIdJson(@PathParam("username") String userName,@HeaderParam("authorization") String authString) 
    {
        
        String response = "successfull";
        String resJSON ="";
        try
        {
            authenticateUser(authString,userName);

            userDAO.deleteUserById(userName);
            
            resJSON = 
            "  {" +
            "   \"type\": \""+ "result" +"\", " +
            "   \"message\": \""+ response +"\" " +
            " }";
        }
        catch(Exception ex)
        {
            resJSON = 
            "  {" +
            "   \"type\": \""+ "error" +"\", " +        
            "   \"message\": \""+ ex.getMessage() +"\" " +
            " } ";
        }

        JsonReader reader = Json.createReader(new StringReader(resJSON));
        JsonObject personObject = reader.readObject();
        reader.close();
        return personObject;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject postJson(JsonObject content) 
    {
        String response = "successfull";
        String resJSON = "";
        try
        {
            String id = content.getString("username");
            String password = content.getString("password");
            String status = this.st;
            String ipport = content.getString("ipport");

            userDAO.registerUser(id, password, status, ipport);
            
            resJSON = 
            "  {" +
            "   \"type\": \""+ "result" +"\", " + 
            "   \"message\": \""+ response +"\" " +
            " }";
        }
        catch(Exception ex)
        {
            resJSON = 
            "  {" +
            "   \"type\": \""+ "error" +"\", " +        
            "   \"message\": \""+ ex.getMessage() +"\" " +
            " } ";
        }
        

         
        JsonReader reader = Json.createReader(new StringReader(resJSON));
        JsonObject personObject = reader.readObject();
        reader.close();  
        return personObject;    
    }
    
    private void authenticateUser(String authString, String username) throws Exception
    {
        String decodedAuth = "";
        // Header is in the format "Basic 5tyc0uiDat4"
        // We need to extract data before decoding it back to original string
        String[] authParts = authString.split("\\s+");
        String authInfo = authParts[1];
        // Decode the data back to original string
        byte[] bytes = null;

        bytes = new BASE64Decoder().decodeBuffer(authInfo);

        decodedAuth = new String(bytes);
        
        String[] tokens = decodedAuth.split(":");
        
        if(!username.equals(""))
        {
            if(!tokens[0].equals(username))
                throw new Exception("Unauthorized action");
        }
        
        MyUser u = userDAO.findUserByID(tokens[0]);
        
        if(!u.getPassword().equals(tokens[1]))
            throw new Exception("Wrong password");
        

    }
}
