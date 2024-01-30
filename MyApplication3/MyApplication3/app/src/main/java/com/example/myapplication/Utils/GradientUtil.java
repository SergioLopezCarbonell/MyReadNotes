package com.example.myapplication.Utils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class GradientUtil {

    public static GradientDrawable getGradientForCategory(String category) {
        GradientDrawable gradient = new GradientDrawable();

        switch (category) {
            case "Fiction":
                gradient.setColors(new int[]{Color.parseColor("#87CEFA"), Color.parseColor("#4682B4")});
                break;
            case "Non-Fiction":
                gradient.setColors(new int[]{Color.parseColor("#98FB98"), Color.parseColor("#32CD32")});
                break;
            case "Literary genre":
                gradient.setColors(new int[]{Color.parseColor("#FFD700"), Color.parseColor("#FFA500")});
                break;
            case "Adventures":
                gradient.setColors(new int[]{Color.parseColor("#FF6347"), Color.parseColor("#FF4500")});
                break;
            case "Mistery":
                gradient.setColors(new int[]{Color.parseColor("#9370DB"), Color.parseColor("#6A5ACD")});
                break;
            case "Romance":
                gradient.setColors(new int[]{Color.parseColor("#FF69B4"), Color.parseColor("#FF1493")});
                break;
            case "Terror":
                gradient.setColors(new int[]{Color.parseColor("#696969"), Color.parseColor("#2F4F4F")});
                break;
            case "Others":
                gradient.setColors(new int[]{Color.parseColor("#EBF51C"), Color.parseColor("#C0C63F")});
                break;
            default:
                gradient.setColors(new int[]{Color.parseColor("#000000"), Color.parseColor("#FFFFFF")});
        }

        gradient.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);


        return gradient;
    }
}
