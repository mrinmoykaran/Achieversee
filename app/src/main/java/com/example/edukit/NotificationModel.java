package com.example.edukit;

public class NotificationModel {
    String paid_for;
    String cost;
    String transaction_date;
    String transaction_time;

    public NotificationModel(String paid_for, String cost, String transaction_date, String transaction_time) {
        this.paid_for = paid_for;
        this.cost = cost;
        this.transaction_date = transaction_date;
        this.transaction_time = transaction_time;
    }

    public String getPaid_for() {
        return paid_for;
    }

    public void setPaid_for(String paid_for) {
        this.paid_for = paid_for;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }
}
