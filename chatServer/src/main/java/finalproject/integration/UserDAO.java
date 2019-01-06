/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject.integration;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import finalproject.model.MyUser;
import java.util.*;
import javax.persistence.*;
import javax.ejb.LocalBean;

/**
 *
 * @author Samie
 */
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
@LocalBean
public class UserDAO {
    
    @PersistenceContext(unitName = "userPU")
    private EntityManager em;
    
    public String st = "online";
    
    public UserDAO() 
    {}
    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public MyUser findUserByID(String id) throws Exception
    {
        MyUser user = em.find(MyUser.class, id);
        if (user == null) {
            throw new Exception("No User Found: " + id);
        }
        return user;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public MyUser[] findAllUsers() 
    {
        List<MyUser> listUsers = em.createQuery("SELECT p FROM MyUser p").getResultList();
        return listUsers.toArray(new MyUser[listUsers.size()]);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public MyUser[] findOnlineUsers() 
    {   
        List<MyUser> listUsers = em.createQuery(
        "SELECT p FROM MyUser p WHERE p.status LIKE :custStatus")
        .setParameter("custStatus", st)
        .getResultList();
        
        return listUsers.toArray(new MyUser[listUsers.size()]);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public MyUser updateStatusById(String id, String status, String ipport) throws Exception
    {
        MyUser u=em.find(MyUser.class,id);
        if(u==null)
        {
            throw new Exception("No user found with this id: " + id);
        }
        u.setStatus(status);
        u.setIpport(ipport);
        
        em.merge(u);
        return u; 
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteUserById(String id) throws Exception
    {
        MyUser u=em.find(MyUser.class,id);
        if(u==null)
        {
            throw new Exception("No user found with this id: " + id);
        }
        
        em.remove(u);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public MyUser registerUser(String id,String password, String status, String ipport) throws Exception
    {
        MyUser u1=em.find(MyUser.class,id);
        if(u1!=null)
        {
            throw new Exception("This username has token: " + id);
        }
        MyUser u = new MyUser(id, password, status, ipport);
        em.persist(u);
        return u;
    }
    
}
