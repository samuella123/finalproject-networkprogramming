/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Access;
import javax.persistence.AccessType;

/**
 *
 * @author Samie
 */
@Entity
@Table(name = "MYUSERS")
public class MyUser implements UserDTO,Serializable {
    
    @Id
    @Column(name = "id")
    private String id;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "ipport")
    private String ipport;


    public MyUser() {
    }


    public MyUser(String id, String pw, String st, String ip) {
        this.id = id;
        this.password = pw;
        this.status = st;
        this.ipport = ip;
    }
    
    @Override
    @Column(name = "id")
    @Id
    public String getId() {
        return this.id;
    }

    @Override
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    @Override
    @Column(name = "status")
    public String getStatus() {
        return status;
    }
    
    @Override
    @Column(name = "ipport")
    public String getIpport() {
        return ipport;
    }
    
    @Override
    @Column(name = "status")
    public void setStatus(String st) {
        status = st;
    }
    
    @Override
    @Column(name = "ipport")
    public void setIpport(String ip) {
        ipport = ip;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MyUser)) {
            return false;
        }
        MyUser other = (MyUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "finalproject.model.Rate[ id=" + id + " ]";
    }
    
}
