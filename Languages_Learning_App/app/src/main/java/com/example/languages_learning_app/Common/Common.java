package com.example.languages_learning_app.Common;

import android.location.Location;

import com.example.languages_learning_app.DTO.Language;
import com.example.languages_learning_app.DTO.User;
import com.example.languages_learning_app.R;

import java.util.Locale;

public class Common {
    public static User user;
    public static Language language;
    public static String role = "Admin";

    public static String RoleAdmin = "Admin";
    public static String RoleManager = "Manager";
    public static String RoleTrainee = "Trainee";

    // Set for practice type
    public static int PRACTICE_TYPE_EASY = 0;
    public static int PRACTICE_TYPE_HARD = 1;

    public static String defaltPass = "12345678";

    public static int getFlagLanguage(String name){
        int flag;
        name = name.toUpperCase();
        switch (name){
            case "ENGLISH":
                flag = R.drawable.flag_of_english;
                break;
            case "CHINESE":
                flag = R.drawable.flag_of_china;
                break;
            case "FRENCH":
                flag = R.drawable.flag_of_french;
                break;
            default:
                flag = R.drawable.bg_logo;
                break;
        }
        return flag;
    }

    public static Locale getLocale(String name){
        Locale locale = Locale.ENGLISH;
        name = name.toLowerCase();
        switch (name){
            case "ENGLISH":
                locale = Locale.ENGLISH;
                break;
            case "CHINESE":
                locale = Locale.CHINESE;
                break;
            case "FRENCH":
                locale = Locale.FRENCH;
                break;
        }
        return locale;
    }

    public enum mode{
        create,
        read,
        update,
        delete
    }
}
