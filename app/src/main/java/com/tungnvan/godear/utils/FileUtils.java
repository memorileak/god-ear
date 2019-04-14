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
}
