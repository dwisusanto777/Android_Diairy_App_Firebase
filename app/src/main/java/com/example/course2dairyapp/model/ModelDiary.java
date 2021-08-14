package com.example.course2dairyapp.model;

public class ModelDiary {

    private String title;
    private String description;
    private String tanggal;
    private String userId;

    public ModelDiary() {
    }

    public ModelDiary(String title, String description, String tanggal, String userId) {
        this.title = title;
        this.description = description;
        this.tanggal = tanggal;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
