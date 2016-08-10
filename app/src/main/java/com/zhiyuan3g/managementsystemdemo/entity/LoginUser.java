package com.zhiyuan3g.managementsystemdemo.entity;

/**
 * Created by Administrator on 2016/1/4.
 */
public class LoginUser {
    private String userName;
    private String password;
    private int flag;
    private boolean checked;

    public LoginUser(String userName, String password, int flag) {
        super();
        this.userName = userName;
        this.password = password;
        this.flag = flag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
