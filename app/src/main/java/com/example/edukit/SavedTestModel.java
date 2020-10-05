package com.example.edukit;

public class SavedTestModel {
    String pubId;
    String testId;
    String Date;
    String Time;

    public SavedTestModel(String pubId, String testId, String date, String time) {
        this.pubId = pubId;
        this.testId = testId;
        Date = date;
        Time = time;
    }

    public String getPubId() {
        return pubId;
    }

    public void setPubId(String pubId) {
        this.pubId = pubId;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
