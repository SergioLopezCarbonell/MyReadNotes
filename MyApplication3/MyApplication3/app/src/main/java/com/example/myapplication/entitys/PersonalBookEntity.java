package com.example.myapplication.entitys;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.myapplication.classes.Book;

@Entity(tableName = "personal_books")
public class PersonalBookEntity extends Book {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private boolean finish;
    private String startDate;
    private String endDate;
    private String description;


    public PersonalBookEntity() {
        super("", "", "0", "");
    }

    public PersonalBookEntity(String title, String author, String coverResource, String category) {
        super(title, author, coverResource, category);
        this.finish = false;
        this.startDate = null;
        this.endDate = null;
        this.description = "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFinish() {
        return finish;
    }

    public int getId() {
        return id;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
