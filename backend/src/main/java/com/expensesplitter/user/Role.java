package com.expensesplitter.user;

/**
 * Enumerated {@link User} roles.
 * 
 */
public enum Role {
    ADMIN, USER_MANAGER, REGULAR_USER;
    
    public String authority() {
        return "ROLE_" + this.name();
    }
}
