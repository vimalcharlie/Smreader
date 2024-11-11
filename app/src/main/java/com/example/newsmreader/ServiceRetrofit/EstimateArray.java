package com.example.newsmreader.ServiceRetrofit;

public class EstimateArray {
    private String Status;
    private  String data;

    public EstimateArray(String status, String data) {
        Status = status;
        this.data = data;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

