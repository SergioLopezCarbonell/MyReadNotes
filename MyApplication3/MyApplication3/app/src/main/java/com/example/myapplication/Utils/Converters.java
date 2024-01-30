package com.example.myapplication.Utils;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class Converters {

    @TypeConverter
    public static String fromStringArray(String[] array) {
        return array == null ? null : String.join(",", array);
    }

    @TypeConverter
    public static String[] toStringArray(String concatenated) {
        return concatenated == null ? null : concatenated.split(",");
    }
}
