package com.example.newsmreader.Models;

public class StateMentModel {

    private  String Consumer;
    private  String consumer_no;
    private  String Received;
    private  String Total;
    private  String id;
    private  String online;
    private String cash;


    public StateMentModel(String consumer, String consumer_no, String received, String total, String id, String online, String cash) {
        Consumer = consumer;
        this.consumer_no = consumer_no;
        Received = received;
        Total = total;
        this.id = id;
        this.online = online;
        this.cash = cash;
    }

    public String getOnline() {
        return online;
    }

    public String getCash() {
        return cash;
    }

    public String getConsumer() {
        return Consumer;
    }

    public String getConsumer_no() {
        return consumer_no;
    }

    public String getReceived() {
        return Received;
    }

    public String getTotal() {
        return Total;
    }

    public String getId() {
        return id;
    }
}
