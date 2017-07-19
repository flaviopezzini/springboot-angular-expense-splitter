package com.expensesplitter.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.expensesplitter.ControllerTestUtil;
import com.expensesplitter.user.User;
import com.expensesplitter.user.UserController;
import com.expensesplitter.user.UserRoleService;
import com.expensesplitter.user.UserSave;
import com.expensesplitter.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, secure = false)
public class UserControllerTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    @MockBean
    private UserRoleService userRoleService;
    @MockBean
    private BCryptPasswordEncoder encoder;
    @MockBean
    private AuthenticationFailureHandler failureHandler;
    
    @Test
    public void testListWrongMethod() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(UserController.REST_PREFIX)
                .accept(MediaType.APPLICATION_JSON)
                .principal(ControllerTestUtil.createUserPrincipal());;
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getStatus());
    }
    
    @Test
    public void testListSuccess() throws Exception {
        List<User> mockList = new ArrayList<>();
        mockList.add(new User());
        when(userService.findAllByOrderByUsernameAsc()).thenReturn(mockList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(UserController.REST_PREFIX)
                .accept(MediaType.APPLICATION_JSON)
                .principal(ControllerTestUtil.createUserPrincipal());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        JSONArray resultObject = new JSONArray(response.getContentAsString());
        assertThat(resultObject.length() > 0);
    }
    
    @Test
    public void testCreateWrongMethod() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(UserController.REST_PREFIX)
                .accept(MediaType.APPLICATION_JSON)
                .principal(ControllerTestUtil.createUserPrincipal());;
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getStatus());
    }
    
    @Test
    public void testCreateSuccess() throws Exception {
        User user = new User();
        when(userService.save(any(User.class))).thenReturn(user);
        ObjectMapper objectMapper = new ObjectMapper();
        UserSave userSave = new UserSave();
        userSave.setUser(user);
        userSave.setIsAdminParameter("false");
        userSave.setIsUserManagerParameter("false");
        userSave.setIsRegularUserParameter("true");
        byte[] requestJson =  objectMapper.writeValueAsBytes(userSave);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(UserController.REST_PREFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON)
                .principal(ControllerTestUtil.createUserPrincipal());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        JSONObject resultObject = new JSONObject(response.getContentAsString());
        assertNotNull(resultObject);
    }
    
    @Test
    public void testDeactivateWrongMethod() throws Exception {
        String url = UserController.REST_DEACTIVATE.replace("{id}", "777");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(url)
                .accept(MediaType.APPLICATION_JSON)
                .principal(ControllerTestUtil.createUserPrincipal());;
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getStatus());
    }
    
    @Test
    public void testDeactivateSuccess() throws Exception {
        String url = UserController.REST_DEACTIVATE.replace("{id}", "777");
        User mockUser = new User();
        when(userService.changeActive(any(String.class), any(Boolean.class)))
                .thenReturn(
                mockUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(url)
                .accept(MediaType.APPLICATION_JSON)
                .principal(ControllerTestUtil.createUserPrincipal());;
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        JSONObject resultObject = new JSONObject(response.getContentAsString());
        assertNotNull(resultObject);
    }
    
    @Test
    public void testActivateWrongMethod() throws Exception {
        String url = UserController.REST_ACTIVATE.replace("{id}", "777");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(url)
                .accept(MediaType.APPLICATION_JSON)
                .principal(ControllerTestUtil.createUserPrincipal());;
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getStatus());
    }
    
    @Test
    public void testActivateSuccess() throws Exception {
        String url = UserController.REST_ACTIVATE.replace("{id}", "777");
        User mockUser = new User();
        when(userService.changeActive(any(String.class), any(Boolean.class)))
                .thenReturn(
                mockUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(url)
                .accept(MediaType.APPLICATION_JSON)
                .principal(ControllerTestUtil.createUserPrincipal());;
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        JSONObject resultObject = new JSONObject(response.getContentAsString());
        assertNotNull(resultObject);
    }
}