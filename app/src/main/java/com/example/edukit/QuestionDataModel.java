package com.example.edukit;

import java.io.Serializable;

public class QuestionDataModel implements Serializable {
    String Title, Language, Type, Demo_Pdf, Download_link, group, price, publisherID, notesID;

    public QuestionDataModel(String notesID, String title, String language, String type, String demo_Pdf, String download_link, String group, String price, String publisherID) {
        this.notesID = notesID;
        Title = title;
        Language = language;
        Type = type;//Type is for selecting view in viewholder
        Demo_Pdf = demo_Pdf;
        Download_link = download_link;
        this.group = group;
        this.price = price;
        this.publisherID = publisherID;
    }

    public String getNotesID() {
        return notesID;
    }

    public void setNotesID(String notesID) {
        this.notesID = notesID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDemo_Pdf() {
        return Demo_Pdf;
    }

    public void setDemo_Pdf(String demo_Pdf) {
        Demo_Pdf = demo_Pdf;
    }

    public String getDownload_link() {
        return Download_link;
    }

    public void setDownload_link(String download_link) {
        Download_link = download_link;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(String publisherID) {
        this.publisherID = publisherID;
    }
}
