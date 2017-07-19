package com.expensesplitter.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.expensesplitter.security.config.WebSecurityConfig;
import com.expensesplitter.shared.WebUtil;
import com.expensesplitter.shared.json.View;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class UserController {

    protected static final String REST_PREFIX = "/api/users";
    protected static final String REST_PREFIX_ID = REST_PREFIX + "/{id}";
    protected static final String REST_DEACTIVATE = REST_PREFIX
            + "/{id}/deactivate";
    protected static final String REST_ACTIVATE = REST_PREFIX
            + "/{id}/activate";
    protected static final String REST_UPDATE_PROFILE = REST_PREFIX
            + "/updateProfile";
    private final BCryptPasswordEncoder encoder;
    private UserService userService;
    private UserRoleService userRoleService;

    @Autowired
    public UserController(final UserService userService,
            final UserRoleService userRoleService,
            final BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.encoder = encoder;
    }

    @CrossOrigin(origins = WebUtil.ANGULAR_URL)
    @RequestMapping(value = REST_PREFIX, method = RequestMethod.GET, produces = WebUtil.JSON_FORMAT)
    @JsonView(View.Summary.class)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER_MANAGER')")
    public @ResponseBody List<User> list(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        return (List<User>) userService.findAllByOrderByUsernameAsc();
    }

    @CrossOrigin(origins = WebUtil.ANGULAR_URL)
    @RequestMapping(value = REST_PREFIX, method = RequestMethod.POST, produces = WebUtil.JSON_FORMAT)
    @JsonView(View.Summary.class)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER_MANAGER')")
    public @ResponseBody User create(HttpServletRequest request,
            HttpServletResponse response, @RequestBody UserSave userSave)
            throws IOException, ServletException {
        User user = userSave.getUser();
        String userId = user.getId();
        User dbUser;
        if (!StringUtils.isEmpty(userId)) {
            throw new IllegalArgumentException(WebUtil.ID_MUST_BE_EMPTY);
        }
        dbUser = user;
        // encrypt the password
        dbUser.setPassword(encoder.encode(user.getPassword()));
        dbUser = userService.save(dbUser);
        String newUserId = dbUser.getId();
        // only admins can create other admins
        if (userSave.isAdmin() && WebUtil.isAdmin(request)) {
            userRoleService.addRoleToUser(newUserId, Role.ADMIN);
        }
        if (userSave.isUserManager()) {
            userRoleService.addRoleToUser(newUserId, Role.USER_MANAGER);
        }
        if (userSave.isRegularUser()) {
            userRoleService.addRoleToUser(newUserId, Role.REGULAR_USER);
        }
        return dbUser;
    }

    @CrossOrigin(origins = WebUtil.ANGULAR_URL)
    @RequestMapping(value = REST_PREFIX_ID, method = RequestMethod.PUT, produces = WebUtil.JSON_FORMAT)
    @JsonView(View.Summary.class)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER_MANAGER')")
    public @ResponseBody User update(HttpServletRequest request,
            HttpServletResponse response, @RequestBody UserSave userSave)
            throws IOException, ServletException {
        User user = userSave.getUser();
        String userId = user.getId();
        User dbUser;
        if (StringUtils.isEmpty(userId)) {
            throw new IllegalArgumentException(WebUtil.NO_ID_PROVIDED);
        }
        dbUser = userService.findOne(userId);
        dbUser.setUsername(user.getUsername());
        dbUser.setActive(user.getActive());
        dbUser = userService.save(dbUser);
        userRoleService.manageRoles(userId, WebUtil.isAdmin(request), userSave
                .isAdmin(), userSave.isUserManager(), userSave.isRegularUser());
        return dbUser;
    }

    @CrossOrigin(origins = WebUtil.ANGULAR_URL)
    @RequestMapping(value = WebSecurityConfig.SIGN_UP_ENTRY_POINT, method = RequestMethod.PUT, produces = WebUtil.JSON_FORMAT)
    @JsonView(View.Summary.class)
    public @ResponseBody User signUp(HttpServletRequest request,
            @RequestBody User user) {
        // encrypt the password
        user.setPassword(encoder.encode(user.getPassword()));
        User storedUser = userService.save(user);
        userRoleService.addRoleToUser(storedUser.getId(), Role.REGULAR_USER);
        return storedUser;
    }

    @CrossOrigin(origins = WebUtil.ANGULAR_URL)
    @RequestMapping(value = REST_UPDATE_PROFILE, method = RequestMethod.PUT, produces = WebUtil.JSON_FORMAT)
    @JsonView(View.Summary.class)
    public @ResponseBody User updateProfile(HttpServletRequest request,
            @RequestBody UserProfileChange userProfileChange) {
        // find the user
        User userParameter = userProfileChange.getUser();
        String username = userParameter.getUsername();
        User dbUser = userService.findByUsername(username);
        dbUser.setUsername(userParameter.getUsername());
        // check whether the old password matches
        // only if the user requested to change it
        if (StringUtils.isNotEmpty(userParameter.getPassword()) && StringUtils
                .isNotEmpty(userProfileChange.getOldPassword())) {
            if (!encoder.matches(userProfileChange.getOldPassword(), dbUser
                    .getPassword())) {
                throw new IllegalArgumentException("Invalid old password");
            }
            dbUser.setPassword(encoder.encode(userParameter.getPassword()));
        }
        return userService.save(dbUser);
    }

    @CrossOrigin(origins = WebUtil.ANGULAR_URL)
    @RequestMapping(value = REST_DEACTIVATE, method = RequestMethod.PUT, produces = WebUtil.JSON_FORMAT)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER_MANAGER')")
    public @ResponseBody User deactivate(HttpServletRequest request,
            HttpServletResponse response, @PathVariable("id") String id)
            throws IOException, ServletException {
        return userService.changeActive(id, false);
    }

    @CrossOrigin(origins = WebUtil.ANGULAR_URL)
    @RequestMapping(value = REST_ACTIVATE, method = RequestMethod.PUT, produces = WebUtil.JSON_FORMAT)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER_MANAGER')")
    public @ResponseBody User activate(HttpServletRequest request,
            HttpServletResponse response, @PathVariable("id") String id)
            throws IOException, ServletException {
        return userService.changeActive(id, true);
    }
}
