package com.example.newsmreader.Models;

import java.util.ArrayList;

public class Complaint_ListArray {
 private String Status;
 private ArrayList<Complaint_List> data=new ArrayList<>();


    public String getStatus() {
        return Status;
    }

    public ArrayList<Complaint_List> getData() {
        return data;
    }
}
