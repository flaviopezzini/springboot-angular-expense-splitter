package com.expensesplitter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.expensesplitter.security.auth.JwtAuthenticationToken;
import com.expensesplitter.security.model.UserContext;
import com.expensesplitter.user.Role;
import com.expensesplitter.user.User;
import com.expensesplitter.user.UserRole;

public class ControllerTestUtil {
    public static JwtAuthenticationToken createUserPrincipal() {
        User user = new User();
        ArrayList<UserRole> roles = new ArrayList<>();
        UserRole admin = new UserRole();
        admin.setRole(Role.ADMIN);
        roles.add(admin);
        user.setRoles(roles);
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getRole().authority()))
                .collect(Collectors.toList());
        
        UserContext userContext = UserContext.create("mock@user.name", authorities);
        JwtAuthenticationToken token = new JwtAuthenticationToken(userContext, authorities);
        return token;
    }
}
