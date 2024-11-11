package com.example.newsmreader.Models;

import java.util.ArrayList;

public class CustomerModelArray {
    private  String Status;
    private ArrayList<CustomerModel> data=new ArrayList<>();

    public String getStatus() {
        return Status;
    }

    public ArrayList<CustomerModel> getData() {
        return data;
    }
}
