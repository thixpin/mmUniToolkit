package com.htetznaing.unitoolkit;

import android.webkit.MimeTypeMap;

public class Test {
    public static void main(String args[]){
        System.out.println(getMimeType("/Users/htetznaing/Documents/AndroidStudioProjects/mmUnicodeToolkit/app/release/output.json"));
    }

    private static String getMimeType(String fileUrl) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(fileUrl);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
