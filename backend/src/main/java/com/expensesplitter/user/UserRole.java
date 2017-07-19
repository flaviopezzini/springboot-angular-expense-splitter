package com.expensesplitter.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.expensesplitter.shared.json.View;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * UserRole
 */
@Entity
public class UserRole {
    @Embeddable
    public static class Id implements Serializable {
        private static final long serialVersionUID = 1322120000551624359L;
        
        @Column(name = "user_id", columnDefinition = "VARCHAR(36)")
        protected String userId;
        
        @Enumerated(EnumType.STRING)
        @Column(insertable=false, updatable=false)
        protected Role role;
        
        public Id() { }

        public Id(String userId, Role role) {
            this.userId = userId;
            this.role = role;
        }
    }
    
    @EmbeddedId
    Id id = new Id();
    
    @JsonView(View.Summary.class)
    public Role getRole() {
        return id.role;
    }
    
    public void setUserId(String userId) {
        this.id.userId = userId;
    }
    
    public void setRole(Role role) {
        this.id.role = role;
    }
}
