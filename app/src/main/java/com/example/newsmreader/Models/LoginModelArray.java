package com.example.newsmreader.Models;

import java.util.ArrayList;

public class LoginModelArray {

    private  String Status;
    private ArrayList<LoginModel> data=new ArrayList<>();

    public LoginModelArray(String status, ArrayList<LoginModel> data) {
        Status = status;
        this.data = data;
    }

    public String getStatus() {
        return Status;
    }

    public ArrayList<LoginModel> getData() {
        return data;
    }
}
