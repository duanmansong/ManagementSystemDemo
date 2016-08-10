package com.zhiyuan3g.managementsystemdemo.entity;

import java.sql.Blob;

/**
 * Created by Administrator on 2016/1/4.
 */
public class Employee {
    private String name;
    private String sex;
    private String mingZu;
    private String id;
    private String department;
    private String birthday;
    private String phone;
    private String user;
    private String more;
//    private byte[] image;
    private boolean checked;

//    public Employee(String name, String sex, String mingZu, String id,String department, String birthday, String phone, String more) {
//        super();
//        this.name = name;
//        this.sex = sex;
//        this.mingZu = mingZu;
//        this.id = id;
//        this.department = department;
//        this.birthday = birthday;
//        this.phone = phone;
//        this.more = more;
////        this.image = image;
//    }

//    public byte[] getImage() {
//        return image;
//    }
//
//    public void setImage(byte[] image) {
//        this.image = image;
//    }


    public Employee(String name, String sex, String mingZu, String id, String department, String birthday, String phone, String user, String more) {
        this.name = name;
        this.sex = sex;
        this.mingZu = mingZu;
        this.id = id;
        this.department = department;
        this.birthday = birthday;
        this.phone = phone;
        this.user = user;
        this.more = more;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMingZu() {
        return mingZu;
    }

    public void setMingZu(String mingZu) {
        this.mingZu = mingZu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
