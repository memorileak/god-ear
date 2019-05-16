package com.tungnvan.godear.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static String toHMSString(int total_seconds) {
        int hours = total_seconds / 3600;
        int minutes = (total_seconds % 36000) / 60;
        int seconds = total_seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Chuyển số lượng milli giây thành một String có ý nghĩa.
    public static String millisecondsToString(int milliseconds)  {
        int total_seconds = milliseconds/1000;
        //int hours = total_seconds / 3600;
        int minutes = (total_seconds % 36000) / 60;
        int seconds = total_seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

}
