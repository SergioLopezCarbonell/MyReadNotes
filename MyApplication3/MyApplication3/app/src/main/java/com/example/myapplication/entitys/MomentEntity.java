package com.example.myapplication.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "moments",
        foreignKeys = @ForeignKey(entity = PersonalBookEntity.class,
                parentColumns = "id",
                childColumns = "personalBookId",
                onDelete = ForeignKey.CASCADE))
public class MomentEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int personalBookId;
    private String chapter;
    private String page;
    private String category;
    private String body;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonalBookId() {
        return personalBookId;
    }

    public void setPersonalBookId(int personalBookId) {
        this.personalBookId = personalBookId;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
