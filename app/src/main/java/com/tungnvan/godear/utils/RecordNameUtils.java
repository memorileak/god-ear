package com.tungnvan.godear.utils;

import com.tungnvan.godear.constants.GlobalConstants;

public class RecordNameUtils {

    public static String produceFilePathFromName(String file_name) {
        return GlobalConstants.RECORD_DIRECTORY + file_name + ".m4a";
    }

    public static String produceFileNameFromPath(String file_path) {
        return file_path
            .replace(GlobalConstants.RECORD_DIRECTORY, "")
            .replace(".m4a", "");
    }

}
