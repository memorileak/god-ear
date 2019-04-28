package com.tungnvan.godear.utils;

public class TimeUtils {

    public static String toHMSString(int total_seconds) {
        int hours = total_seconds / 3600;
        int minutes = (total_seconds % 36000) / 60;
        int seconds = total_seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
