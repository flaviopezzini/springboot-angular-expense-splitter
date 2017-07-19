package com.expensesplitter.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.expensesplitter.shared.json.View;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class User {
    private String id;
    
    private String username;
    
    private String password;
    
    private boolean active = true;
    
    private List<UserRole> roles;
    
    public User() { }
    
    public User(String id, String username, String password, List<UserRole> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    @JsonView(View.Summary.class)
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    @Column(columnDefinition = "VARCHAR(40)",unique=true, nullable=false)
    @JsonView(View.Summary.class)
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    @Column(columnDefinition = "VARCHAR(60)", nullable=false)
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @JsonView(View.Summary.class)
    public boolean getActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }

    @OneToMany
    @JoinColumn(name="user_id", referencedColumnName="id")
    @JsonView(View.Summary.class)
    public List<UserRole> getRoles() {
        return roles;
    }
    
    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }
}
