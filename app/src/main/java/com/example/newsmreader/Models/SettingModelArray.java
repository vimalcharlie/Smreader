package com.example.newsmreader.Models;

import java.util.ArrayList;

public class SettingModelArray {
    private String Status;
    private ArrayList<SettingModel> data=new ArrayList<>();


    public String getStatus() {
        return Status;
    }

    public ArrayList<SettingModel> getData() {
        return data;
    }
}
