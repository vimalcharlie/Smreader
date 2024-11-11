package com.example.newsmreader.Models;

import java.util.ArrayList;

public class StamentreadModelArray {
    private  String Status;
    private ArrayList<StamentreadModel> data=new ArrayList<>();

    public StamentreadModelArray(String status, ArrayList<StamentreadModel> data) {
        Status = status;
        this.data = data;
    }


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public ArrayList<StamentreadModel> getData() {
        return data;
    }

    public void setData(ArrayList<StamentreadModel> data) {
        this.data = data;
    }
}
