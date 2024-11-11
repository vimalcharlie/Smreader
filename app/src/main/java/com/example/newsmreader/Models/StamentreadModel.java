package com.example.newsmreader.Models;

public class StamentreadModel {
    private String id;
    private String Consumer;

    private String consumer_no;

    private String reading;
    private String Total;


    public StamentreadModel(String id, String consumer, String consumer_no, String reading, String total) {
        this.id = id;
        Consumer = consumer;
        this.consumer_no = consumer_no;
        this.reading = reading;
        Total = total;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsumer() {
        return Consumer;
    }



    public String getConsumer_no() {
        return consumer_no;
    }


    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getTotal() {
        return Total;
    }


}
