package com.expensesplitter.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.expensesplitter.shared.WebUtil;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    UserRoleRepository userRoleRepository;
    
    public User findLoggedInUser(HttpServletRequest request) {
        String username = WebUtil.findLoggedInUsername(request);
        User loggedInUser = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return loggedInUser;
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
    
    public List<User> findAllByOrderByUsernameAsc() {
        return userRepository.findAllByOrderByUsernameAsc();
    }
    
    public User findOne(String id) {
        return userRepository.findOne(id);
    }
    
    public User save(User record) {
        return userRepository.save(record);
    }

    public User changeActive(String id, boolean active) {
        User toDelete = userRepository.findOne(id);
        if (toDelete.getActive() != active) {
            toDelete.setActive(active);
            toDelete = userRepository.save(toDelete);
        }
        return toDelete;
    }
}
