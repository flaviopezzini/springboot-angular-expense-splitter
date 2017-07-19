package com.expensesplitter.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {
    
    @Autowired
    UserRoleRepository userRoleRepository;
    
    public UserRole addRoleToUser(String userId, Role role) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRole(role);
        return userRoleRepository.save(userRole);
    }
    
    public boolean removeRoleFromUser(UserRole userRole) {
        userRoleRepository.delete(userRole);
        return true;
    }
    
    public List<UserRole> findByUserId(String userId) {
        return userRoleRepository.findByUserId(userId);
    }
    
    public void manageRoles(
            String userId,
            boolean isLoggedInAdmin, // the user logged in is admin
            boolean isAdmin, // the user to store is admin
            boolean isUserManager, // the user to store is user manager
            boolean isRegularUser) { // the user to store is regular user
        // keep only the roles that match
        List<UserRole> roles = findByUserId(userId);
        boolean wasRegularUser = false;
        boolean wasUserManager = false;
        boolean wasAdmin = false;
        for (UserRole userRole : roles) {
            boolean remove = false;
            if (userRole.getRole().equals(Role.REGULAR_USER)) {
                if (!isRegularUser) {
                    remove = true;
                }
                wasRegularUser = true;
            }
            if (userRole.getRole().equals(Role.USER_MANAGER)) {
                if (!isUserManager) {
                    remove = true;
                }
                wasUserManager = true;
            }
            if (userRole.getRole().equals(Role.ADMIN)) {
                if (!isAdmin && isLoggedInAdmin) {// one admin can remove another
                    remove = true;
                }
                wasAdmin = true;
            }
            if (remove) {
                removeRoleFromUser(userRole);
            }
        }
        // create the missing ones
        if (isAdmin && !wasAdmin) {
            addRoleToUser(userId, Role.ADMIN);
        }
        if (isUserManager && !wasUserManager) {
            addRoleToUser(userId, Role.USER_MANAGER);
        }
        if (isRegularUser && !wasRegularUser) {
            addRoleToUser(userId, Role.REGULAR_USER);
        }        
    }
}
