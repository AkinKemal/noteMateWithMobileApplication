package com.example.notemate.model;

import java.io.Serializable;

public class Note implements Serializable {

    private int ID;
    private String date;
    private String title;
    private String text;
    private String web_url;
    private String image_url;
    private String color;

    public Note() {

    }

    public Note(String date, String title, String text, String web_url, String image_url, String color) {
        this.date = date;
        this.title = title;
        this.text = text;
        this.web_url = web_url;
        this.image_url = image_url;
        this.color = color;
    }

    public Note(int ID, String date, String title, String text, String web_url, String image_url, String color) {
        this.ID = ID;
        this.date = date;
        this.title = title;
        this.text = text;
        this.web_url = web_url;
        this.image_url = image_url;
        this.color = color;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}