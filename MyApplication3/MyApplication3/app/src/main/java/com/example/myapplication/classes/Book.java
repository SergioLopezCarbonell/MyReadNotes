package com.example.myapplication.classes;

public class Book {

    private String title;
    private String author;
    private String coverResource;
    private String description;
    private String category;

    public Book(String title, String author, String coverResource, String category) {
        this.title = title;
        this.author = author;
        this.coverResource = coverResource;
        this.category = category;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getCoverResource() {
        return this.coverResource;
    }

    public String getCategory() {
        return this.category;
    }

    public String getDescription() {
        return this.description;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCoverResource(String coverResource) {
        this.coverResource = coverResource;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
