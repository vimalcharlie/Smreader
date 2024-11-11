package com.example.newsmreader.Models;

import java.util.ArrayList;

public class StateMentModelArray {
 private  String Status;

 private ArrayList<StateMentModel> data=new ArrayList<>();


    public StateMentModelArray(String status, ArrayList<StateMentModel> data) {
        Status = status;
        this.data = data;
    }


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public ArrayList<StateMentModel> getData() {
        return data;
    }

    public void setData(ArrayList<StateMentModel> data) {
        this.data = data;
    }
}
