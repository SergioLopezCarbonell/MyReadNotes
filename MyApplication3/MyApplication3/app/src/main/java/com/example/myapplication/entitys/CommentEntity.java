package com.example.myapplication.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "comments",
        foreignKeys = @ForeignKey(entity = PersonalBookEntity.class,
                parentColumns = "id",
                childColumns = "personalBookId",
                onDelete = ForeignKey.CASCADE))
public class CommentEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int personalBookId;

    private String title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
