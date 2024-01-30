package com.example.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.entitys.CommentEntity;

import java.util.List;

@Dao
public interface CommentDao {

    @Insert
    void insertComment(CommentEntity comment);

    @Query("SELECT * FROM comments WHERE personalBookId = :personalBookId")
    List<CommentEntity> getCommentsForPersonalBook(long personalBookId);

    @Delete
    void deleteComment(CommentEntity comment);
}

