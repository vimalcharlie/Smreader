package com.example.newsmreader.Models;

import java.util.ArrayList;

public class LocationModelArray {
    private  String Status;
    private ArrayList<LocationModel> data=new ArrayList<>();

    public String getStatus() {
        return Status;
    }

    public ArrayList<LocationModel> getData() {
        return data;
    }
}
