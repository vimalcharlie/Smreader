package com.example.newsmreader.Models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class LocationModel implements Serializable {



     private  String id;
     private  String name;
     private  String user_id;


    public LocationModel(String id, String name, String user_id) {
        this.id = id;
        this.name = name;
        this.user_id = user_id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUser_id() {
        return user_id;
    }
}
