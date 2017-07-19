package com.expensesplitter.user;

public class UserSave {
    private User user;
    private String isAdminParameter;
    private String isUserManagerParameter;
    private String isRegularUserParameter;
    private Boolean isAdmin;
    private Boolean isUserManager;
    private Boolean isRegularUser;
    
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getIsAdminParameter() {
        return isAdminParameter;
    }
    public void setIsAdminParameter(String isAdminParameter) {
        this.isAdminParameter = isAdminParameter;
    }
    public String getIsUserManagerParameter() {
        return isUserManagerParameter;
    }
    public void setIsUserManagerParameter(String isUserManagerParameter) {
        this.isUserManagerParameter = isUserManagerParameter;
    }
    public String getIsRegularUserParameter() {
        return isRegularUserParameter;
    }
    public void setIsRegularUserParameter(String isRegularUserParameter) {
        this.isRegularUserParameter = isRegularUserParameter;
    }

    public boolean isAdmin() {
        if (isAdmin == null) {
            isAdmin = Boolean.parseBoolean(isAdminParameter);
        }
        return isAdmin;
    }
    public boolean isUserManager() {
        if (isUserManager == null) {
            isUserManager = Boolean.parseBoolean(isUserManagerParameter);
        }
        return isUserManager;
    }
    public boolean isRegularUser() {
        if (isRegularUser == null) {
            isRegularUser = Boolean.parseBoolean(isRegularUserParameter);
        }
        return isRegularUser;
    }
}
