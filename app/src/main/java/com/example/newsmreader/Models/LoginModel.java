package com.example.newsmreader.Models;

public class LoginModel {



    private String branch_code;
    private String name;
    private String email;
    private String address;
    private String password;
    private String id;
    private String UserMobile;
    private String header;
    private String header1;
    private String header2;
    private String mobile;
    private String content;
    private  String Useraddress;



    public LoginModel(String branch_code, String name, String email
            , String address, String password
            , String id, String userMobile
            , String header, String header1
            , String header2, String mobile
            , String content,String Useraddress) {
        this.branch_code = branch_code;
        this.name = name;
        this.email = email;
        this.address = address;
        this.password = password;
        this.id = id;
        UserMobile = userMobile;
        this.header = header;
        this.header1 = header1;
        this.header2 = header2;
        this.mobile = mobile;
        this.content = content;
        this.Useraddress=Useraddress;
    }


    public String getUseraddress() {
        return Useraddress;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public String getUserMobile() {
        return UserMobile;
    }

    public String getHeader() {
        return header;
    }

    public String getHeader1() {
        return header1;
    }

    public String getHeader2() {
        return header2;
    }

    public String getMobile() {
        return mobile;
    }

    public String getContent() {
        return content;
    }
}
