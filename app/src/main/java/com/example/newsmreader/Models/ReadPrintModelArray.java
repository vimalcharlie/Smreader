package com.example.newsmreader.Models;

import java.util.ArrayList;

public class ReadPrintModelArray {

    private String Status;
    private ArrayList<ReadPrintModel>data=new ArrayList<>();


    public String getStatus() {
        return Status;
    }

    public ArrayList<ReadPrintModel> getData() {
        return data;
    }
}
