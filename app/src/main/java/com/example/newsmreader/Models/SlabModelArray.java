package com.example.newsmreader.Models;

import java.util.ArrayList;

public class SlabModelArray {
    private String Status;
    private ArrayList<SlabModel> data=new ArrayList<>();


    public String getStatus() {
        return Status;
    }

    public ArrayList<SlabModel> getData() {
        return data;
    }
}
