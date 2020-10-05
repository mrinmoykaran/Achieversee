package com.example.edukit;

public class SavedNotesModel {
    String pubId;
    String notesId;
    String Date;
    String Time;

    public SavedNotesModel(String pubId, String notesId, String date, String time) {
        this.pubId = pubId;
        this.notesId = notesId;
        Date = date;
        Time = time;
    }

    public String getPubId() {
        return pubId;
    }

    public void setPubId(String pubId) {
        this.pubId = pubId;
    }

    public String getNotesId() {
        return notesId;
    }

    public void setNotesId(String notesId) {
        this.notesId = notesId;
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
