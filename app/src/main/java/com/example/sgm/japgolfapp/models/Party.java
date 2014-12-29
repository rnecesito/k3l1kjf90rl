package com.example.sgm.japgolfapp.models;

import com.google.gson.annotations.SerializedName;

public class Party {

    @SerializedName("id")
    private String id;

    @SerializedName("email")
    private String name;

    @SerializedName("gender")
    private String date;

    @SerializedName("handicap")
    private String course;


    public Party() {}

    public Party(String id, String name, String date, String course){
        this.id = id;
        this.name = name;
        this.date = date;
        this.course = course;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.course = date;
    }
}
