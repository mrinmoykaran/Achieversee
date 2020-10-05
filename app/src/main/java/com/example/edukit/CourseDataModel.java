package com.example.edukit;

public class CourseDataModel {
    private String title;
    private String rating;
    private String date_of_publish;
    private String published_by;
    private String price;
    private String thumbnail_url;
    private String courseID;
    private String publisherID;
    private String Language;

    public CourseDataModel(String courseID, String title, String rating, String date_of_publish, String published_by, String price, String thumbnail_url, String publisherID, String Language) {
        this.title = title;
        this.rating = rating;
        this.date_of_publish = date_of_publish;
        this.published_by = published_by;
        this.price = price;
        this.thumbnail_url = thumbnail_url;
        this.courseID = courseID;
        this.publisherID = publisherID;
        this.Language = Language;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(String publisherID) {
        this.publisherID = publisherID;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate_of_publish() {
        return date_of_publish;
    }

    public void setDate_of_publish(String date_of_publish) {
        this.date_of_publish = date_of_publish;
    }

    public String getPublished_by() {
        return published_by;
    }

    public void setPublished_by(String published_by) {
        this.published_by = published_by;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }
}
