package com.tool.greeting_tool.pojo.vo;

public class UserLoginVO {
    private String username;
    private String password;
    private String lastLoginTime;
    public UserLoginVO(String username, String password, String lastLoginTime) {
        this.username = username;
        this.password = password;
        this.lastLoginTime = lastLoginTime;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getLastLoginTime() {
        return lastLoginTime;
    }
}
