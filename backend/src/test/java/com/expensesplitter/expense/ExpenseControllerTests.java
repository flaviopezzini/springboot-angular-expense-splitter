package com.expensesplitter.expense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.expensesplitter.ControllerTestUtil;
import com.expensesplitter.expense.Expense;
import com.expensesplitter.expense.ExpenseController;
import com.expensesplitter.expense.ExpenseService;
import com.expensesplitter.user.User;
import com.expensesplitter.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ExpenseController.class, secure = false)
public class ExpenseControllerTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    @MockBean
    private ExpenseService expenseService;
    @MockBean
    private AuthenticationFailureHandler failureHandler;
    
    public void setUp() {
        User mockUser = new User();
        when(userService.findByUsername(any(String.class))).thenReturn(mockUser);
        when(userService.findLoggedInUser(any(HttpServletRequest.class))).thenReturn(mockUser);
    }
    
    @Test
    public void testFilterByDateRangeWrongMethod() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(ExpenseController.REST_FILTER_BY_DATE_RANGE)
                .accept(MediaType.APPLICATION_JSON)
                .principal(ControllerTestUtil.createUserPrincipal());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getStatus());
    }

    @Test
    public void testFilterByDateRangeSuccess() throws Exception {
        List<Expense> mockList = new ArrayList<>();
        mockList.add(new Expense());
        when(expenseService.filterByUserAndDateRangeOrderByDescriptionAsc(any(String.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(mockList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(ExpenseController.REST_FILTER_BY_DATE_RANGE)
                .accept(MediaType.APPLICATION_JSON)
                .principal(ControllerTestUtil.createUserPrincipal())
                .param("startDate", LocalDate.of(2017, 6, 17).toString())
                .param("endDate", LocalDate.of(2017, 6, 17).toString());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        JSONArray resultObject = new JSONArray(response.getContentAsString());
        assertThat(resultObject.length() > 0);
    }
    
    @Test
    public void testWeeklyReportWrongMethod() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(ExpenseController.REST_WEEKLY_REPORT)
                .accept(MediaType.APPLICATION_JSON)
                .principal(ControllerTestUtil.createUserPrincipal());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getStatus());
    }
    
    @Test
    public void testWeeklyReportSuccess() throws Exception {
        List<Expense> mockList = new ArrayList<>();
        Expense one = new Expense();
        one.setAmount(20.0);
        Expense two = new Expense();
        two.setAmount(50.0);
        Expense three = new Expense();
        three.setAmount(30.0);
        mockList.add(one);
        mockList.add(two);
        mockList.add(three);
        when(expenseService.filterByUserAndDateRangeOrderByDescriptionAsc(
                        any(String.class),
                        any(LocalDate.class),
                        any(LocalDate.class)))
                .thenReturn(mockList);
        final double TOTAL_AMOUNT = 100.0;
        final double DAILY_AVERAGE = 25.0;
        when(expenseService.sumByUserAndDateRange(any(String.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(TOTAL_AMOUNT);
        when(expenseService.calculateDailyAverage(any(LocalDate.class), any(LocalDate.class), any(Double.class))).thenReturn(DAILY_AVERAGE);
        //
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(ExpenseController.REST_WEEKLY_REPORT)
                .accept(MediaType.APPLICATION_JSON)
                .principal(ControllerTestUtil.createUserPrincipal())
                .param("startDate", LocalDate.of(2017, 6, 17).toString())
                .param("endDate", LocalDate.of(2017, 6, 21).toString());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        JSONObject resultObject = new JSONObject(response.getContentAsString());
        JSONArray expenses = resultObject.getJSONArray("expenses");
        assertThat(expenses.length() > 0);
        assertEquals("100.0", resultObject.getString("totalExpenses"));
        assertEquals("25.0", resultObject.getString("dailyAverage"));
    }
    
    @Test
    public void testSaveWrongMethod() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(ExpenseController.REST_PREFIX)
                .accept(MediaType.APPLICATION_JSON)
                .principal(ControllerTestUtil.createUserPrincipal());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getStatus());
    }
    
    @Test
    public void testSaveSuccess() throws Exception {
        Expense mockExpense = new Expense();
        when(expenseService.save(any(Expense.class))).thenReturn(mockExpense);
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] requestJson =  objectMapper.writeValueAsBytes(mockExpense);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(ExpenseController.REST_PREFIX)
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
    public void testDeleteWrongMethod() throws Exception {
        String url = ExpenseController.REST_PREFIX_ID.replace("{id}", "777");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(url)
                .accept(MediaType.APPLICATION_JSON)
                .principal(ControllerTestUtil.createUserPrincipal());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getStatus());
    }
    
    @Test
    public void testDeleteSuccess() throws Exception {
        String url = ExpenseController.REST_PREFIX_ID.replace("{id}", "777");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(url)
                .accept(MediaType.APPLICATION_JSON)
                .principal(ControllerTestUtil.createUserPrincipal());
        MvcResult result = mockMvc.perform(requestBuilder)
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        Boolean resultObject = Boolean.parseBoolean(response.getContentAsString()); 
        assertTrue(resultObject);
    }
}