package com.example.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myapplication.Utils.Converters;
import com.example.myapplication.dao.CommentDao;
import com.example.myapplication.dao.MomentDao;
import com.example.myapplication.dao.PersonalBookDao;
import com.example.myapplication.entitys.CommentEntity;
import com.example.myapplication.entitys.MomentEntity;
import com.example.myapplication.entitys.PersonalBookEntity;

@Database(entities = {PersonalBookEntity.class, CommentEntity.class, MomentEntity.class}, version = 7)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PersonalBookDao personalBookDao();
    public abstract CommentDao commentDao();
    public abstract MomentDao momentDao();

    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
