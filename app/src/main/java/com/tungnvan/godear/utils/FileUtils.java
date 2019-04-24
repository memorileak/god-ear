package com.tungnvan.godear.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;

public class FileUtils {

    public static String generateRandomFileName(String prefix) {
        return prefix + RandomStringUtils.randomAlphabetic(16);
    }

    public static String generateRandomFileName() {
        return RandomStringUtils.randomAlphabetic(16);
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
