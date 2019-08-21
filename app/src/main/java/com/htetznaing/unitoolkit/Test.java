package com.htetznaing.unitoolkit;

import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Test {
    public static void main(String args[]){
        String db = readTextFile("/Users/htetznaing/Downloads/DamhaDB.db");
        writeTextFile("/Users/htetznaing/Desktop/test.db",db);
    }

    public static void writeTextFile(String path, String texts){
        try{
            FileWriter writer = new FileWriter(path);
            writer.append(texts);
            writer.flush();
            writer.close();

        }catch (Exception e){
        }
    }

    public static String readTextFile(String path) {
        File file = new File(path);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
        }

        return text.toString();
    }
}
