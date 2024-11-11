package com.example.newsmreader.Models;

import java.util.ArrayList;

public class BillPrintModelArray {
    private String Status;
    private ArrayList<BillPrintModel> data=new ArrayList<>();

    public String getStatus() {
        return Status;
    }

    public ArrayList<BillPrintModel> getData() {
        return data;
    }
}
