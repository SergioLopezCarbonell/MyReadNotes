package com.example.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.entitys.MomentEntity;

import java.util.List;

@Dao
public interface MomentDao {

    @Insert
    void insertMoment(MomentEntity moment);

    @Query("SELECT * FROM moments WHERE personalBookId = :personalBookId")
    List<MomentEntity> getMomentsForPersonalBook(long personalBookId);

    @Delete
    void deleteMoment(MomentEntity moment);
}
