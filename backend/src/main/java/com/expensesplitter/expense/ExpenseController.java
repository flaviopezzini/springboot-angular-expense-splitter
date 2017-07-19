package com.expensesplitter.expense;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.expensesplitter.shared.WebUtil;
import com.expensesplitter.shared.json.View;
import com.expensesplitter.user.UserService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class ExpenseController {

    protected static final String REST_PREFIX = "/api/expenses";
    protected static final String REST_FILTER_BY_DATE_RANGE = REST_PREFIX
            + "/filter/byDateRange";
    protected static final String REST_FILTER_BY_DATE_RANGE_ADMIN = REST_PREFIX
            + "/admin/filter/byDateRange";
    protected static final String REST_WEEKLY_REPORT = REST_PREFIX
            + "/report/weekly";
    protected static final String REST_PREFIX_ID = "/api/expenses/{id}";
    @Autowired
    private UserService userService;
    @Autowired
    private ExpenseService expenseService;

    @Autowired
    public ExpenseController(final UserService userService,
            final ExpenseService expenseService) {
        this.userService = userService;
        this.expenseService = expenseService;
    }

    @CrossOrigin(origins = WebUtil.ANGULAR_URL)
    @RequestMapping(value = REST_FILTER_BY_DATE_RANGE, method = RequestMethod.GET, produces = WebUtil.JSON_FORMAT)
    @JsonView(View.Summary.class)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REGULAR_USER')")
    public @ResponseBody List<Expense> filterByDateRange(
            HttpServletRequest request, HttpServletResponse response,
            @DateTimeFormat(pattern = WebUtil.DATE_PARAMETER_FORMAT) @RequestParam("startDate") LocalDate startDate,
            @DateTimeFormat(pattern = WebUtil.DATE_PARAMETER_FORMAT) @RequestParam("endDate") LocalDate endDate)
            throws IOException, ServletException {
        return expenseService.filterByUserAndDateRangeOrderByDescriptionAsc(
                WebUtil.findLoggedInUsername(request), startDate, endDate);
    }

    @CrossOrigin(origins = WebUtil.ANGULAR_URL)
    @RequestMapping(value = REST_FILTER_BY_DATE_RANGE_ADMIN, method = RequestMethod.GET, produces = WebUtil.JSON_FORMAT)
    @JsonView(View.Summary.class)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REGULAR_USER')")
    public @ResponseBody List<Expense> filterByDateRangeAdmin(
            HttpServletRequest request, HttpServletResponse response,
            @DateTimeFormat(pattern = WebUtil.DATE_PARAMETER_FORMAT) @RequestParam("startDate") LocalDate startDate,
            @DateTimeFormat(pattern = WebUtil.DATE_PARAMETER_FORMAT) @RequestParam("endDate") LocalDate endDate,
            @RequestParam("username") String username) throws IOException,
            ServletException {
        return expenseService.filterByUserAndDateRangeOrderByDescriptionAsc(
                username, startDate, endDate);
    }

    @CrossOrigin(origins = WebUtil.ANGULAR_URL)
    @RequestMapping(value = REST_PREFIX, method = RequestMethod.POST, produces = WebUtil.JSON_FORMAT)
    @JsonView(View.Summary.class)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REGULAR_USER')")
    public @ResponseBody Expense save(HttpServletRequest request,
            HttpServletResponse response, @RequestBody Expense expense)
            throws IOException, ServletException {
        // set the user for new records
        if (!StringUtils.isEmpty(expense.getId())) {
            throw new IllegalArgumentException(WebUtil.NO_ID_PROVIDED);
        }
        expense.setUser(userService.findLoggedInUser(request));
        return expenseService.save(expense);
    }

    @CrossOrigin(origins = WebUtil.ANGULAR_URL)
    @RequestMapping(value = REST_PREFIX_ID, method = RequestMethod.PUT, produces = WebUtil.JSON_FORMAT)
    @JsonView(View.Summary.class)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REGULAR_USER')")
    public @ResponseBody Expense update(HttpServletRequest request,
            HttpServletResponse response, @RequestBody Expense expense)
            throws IOException, ServletException {
        // set the user for new records
        if (StringUtils.isEmpty(expense.getId())) {
            throw new IllegalArgumentException(WebUtil.NO_ID_PROVIDED);
        }
        return expenseService.save(expense);
    }

    @CrossOrigin(origins = WebUtil.ANGULAR_URL)
    @RequestMapping(value = REST_PREFIX_ID, method = RequestMethod.DELETE, produces = WebUtil.JSON_FORMAT)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REGULAR_USER')")
    public @ResponseBody boolean delete(HttpServletRequest request,
            HttpServletResponse response, @PathVariable("id") String id)
            throws IOException, ServletException {
        expenseService.delete(id);
        return true;
    }
}
