package com.comuto.screenshots;

import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;

/**
 * A reader for json files (mocks) in raw/ folder.
 */
public class ResourceReader {

    private static final String RESOURCE_RAW_FOLDER = "raw";

    private ResourceReader() {
    }

    public static String readFromRawResource(Activity activity, String resourceName) {
        final int identifier = activity.getResources().getIdentifier(resourceName,
                RESOURCE_RAW_FOLDER, activity.getPackageName());
        return readFromRawResource(activity, identifier);
    }

    public static String readFromRawResource(Activity activity, int resourceId) {
        String content = "";
        InputStream inputStream = activity.getResources().openRawResource(resourceId);
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            content = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }


}
