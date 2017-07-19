package com.expensesplitter.shared;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.expensesplitter.security.auth.JwtAuthenticationToken;
import com.expensesplitter.security.model.UserContext;
import com.expensesplitter.user.Role;

/**
 * Utility methods and constants used throughout the backend
 */
public class WebUtil {
    public static final String ANGULAR_URL = "http://localhost:3000";
    public static final String DATE_PARAMETER_FORMAT = "yyyy-MM-dd";
    public static final String JSON_FORMAT = "application/json";
    public static final String INVALID_REST_ACCESS = "Invalid REST access";
    
    public static final String ID_MUST_BE_EMPTY = "Id must be empty when creating new records";
    public static final String NO_ID_PROVIDED = "No id provided to update record";
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String X_REQUESTED_WITH = "X-Requested-With";

    private static final String CONTENT_TYPE = "Content-type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    public static boolean isAjax(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
    }

    public static boolean isAjax(SavedRequest request) {
        return request.getHeaderValues(X_REQUESTED_WITH).contains(XML_HTTP_REQUEST);
    }

    public static boolean isContentTypeJson(SavedRequest request) {
        return request.getHeaderValues(CONTENT_TYPE).contains(CONTENT_TYPE_JSON);
    }
    
    public static UserContext findUserContext(HttpServletRequest request) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) request.getUserPrincipal();
        return (UserContext) token.getPrincipal();
    }
    
    public static String findLoggedInUsername(HttpServletRequest request) {
        UserContext userContext = findUserContext(request);
        return userContext.getUsername();
    }
    
    private static HashSet<String> toAuthoritiesMap(HttpServletRequest request) {
        UserContext userContext = findUserContext(request);
        HashSet<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority grantedAuthority : userContext.getAuthorities()) {
            authoritiesSet.add(grantedAuthority.getAuthority());
        }
        return authoritiesSet;
    }
    
    public static boolean isAdmin(HttpServletRequest request) {
        HashSet<String> authoritiesSet = toAuthoritiesMap(request);
        return authoritiesSet.contains(Role.ADMIN.authority());
    }
    
    public static boolean isUserManager(HttpServletRequest request) {
        HashSet<String> authoritiesSet = toAuthoritiesMap(request);
        return authoritiesSet.contains(Role.USER_MANAGER.authority());
    }
    
    public static boolean isRegularUser(HttpServletRequest request) {
        HashSet<String> authoritiesSet = toAuthoritiesMap(request);
        return authoritiesSet.contains(Role.REGULAR_USER.authority());
    }

    public static boolean isAdminOrUserManager(HttpServletRequest request) {
        HashSet<String> authoritiesSet = toAuthoritiesMap(request);
        return authoritiesSet.contains(Role.ADMIN.authority()) || 
                authoritiesSet.contains(Role.USER_MANAGER.authority());
    }
    
    public static boolean isAdminOrRegularUser(HttpServletRequest request) {
        HashSet<String> authoritiesSet = toAuthoritiesMap(request);
        return authoritiesSet.contains(Role.ADMIN.authority()) || 
                authoritiesSet.contains(Role.REGULAR_USER.authority());
    }

}
