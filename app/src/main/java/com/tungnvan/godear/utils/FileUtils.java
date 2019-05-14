package com.tungnvan.godear.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {

    public static String generateFileNameByTime() {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.forLanguageTag("vi-VN"))).format(new Date());
    }

    public static String generateFileNameByTime(Date time) {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.forLanguageTag("vi-VN"))).format(time);
    }

    public static String generateFileNameByTime(String prefix) {
        return prefix + generateFileNameByTime();
    }

    public static String generateFileNameByTime(Date time, String prefix) {
        return prefix + generateFileNameByTime(time);
    }

    public static void createDirectory(String dir_path) {
        File directory = new File(dir_path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static boolean renameFile(String old_path, String new_path) throws Exception {
        try {
            File old_file = new File(old_path);
            File new_file = new File(new_path);
            if (new_file.exists()) {
                throw new Exception("File name is already existed");
            } else {
                return old_file.renameTo(new_file);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static boolean deleteFile(String file_path) throws Exception {
        try {
            File file = new File(file_path);
            if (!file.exists()) {
                throw new Exception("File not exists");
            } else {
                return file.delete();
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
