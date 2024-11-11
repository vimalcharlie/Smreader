package com.example.newsmreader.Models;

import java.util.ArrayList;

import io.reactivex.Completable;

public class ComplaintModelArray {

   private  String Status;
   private ArrayList<ComplaintModel> data=new ArrayList<>();

    public String getStatus() {
        return Status;
    }

    public ArrayList<ComplaintModel> getData() {
        return data;
    }
}
