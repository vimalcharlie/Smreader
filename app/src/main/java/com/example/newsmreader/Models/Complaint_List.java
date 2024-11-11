package com.example.newsmreader.Models;

import androidx.annotation.NonNull;

public class Complaint_List {
    private String id;
    private String name;

    public Complaint_List(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
