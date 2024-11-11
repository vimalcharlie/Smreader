package com.example.newsmreader.Models;

public class SlabModel {

    private String id;
    private String category_guid;
    private String title;
    private String unit_from;
    private String unit_to;
    private String rate;
    private String above;
    private String MinRent;


    public SlabModel(String id, String category_guid, String title, String unit_from, String unit_to, String rate, String above, String minRent) {
        this.id = id;
        this.category_guid = category_guid;
        this.title = title;
        this.unit_from = unit_from;
        this.unit_to = unit_to;
        this.rate = rate;
        this.above = above;
        MinRent = minRent;
    }

    public String getId() {
        return id;
    }

    public String getCategory_guid() {
        return category_guid;
    }

    public String getTitle() {
        return title;
    }

    public String getUnit_from() {
        return unit_from;
    }

    public String getUnit_to() {
        return unit_to;
    }

    public String getRate() {
        return rate;
    }

    public String getAbove() {
        return above;
    }

    public String getMinRent() {
        return MinRent;
    }
}
