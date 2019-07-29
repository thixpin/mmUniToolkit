package com.htetznaing.unitoolkit.Utils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.os.EnvironmentCompat;

import com.htetznaing.unitoolkit.Constants;
import com.htetznaing.unitoolkit.CustomizeActivity;
import com.htetznaing.unitoolkit.MainActivity;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Toolkit {
    private static ArrayList<String> paths,mimeType;
    static int check = 0;
    public static void changeFileNameToUnicode(File file){
        if (file!=null){
            for (File f:file.listFiles()){
                if (f.isDirectory()){
                    if (Constants.CHANGE_HIDDEN) {
                        File newFile = new File(f.getParentFile() + "/" + AIOmmTool.getUnicode(f.getName()));
                        if (f.renameTo(newFile)) {
                            changeFileNameToUnicode(newFile);
                        } else {
                            changeFileNameToUnicode(f);
                        }
                    }else {
                        if (!isHidden(f)){
                            File newFile = new File(f.getParentFile() + "/" + AIOmmTool.getUnicode(f.getName()));
                            if (f.renameTo(newFile)) {
                                changeFileNameToUnicode(newFile);
                            } else {
                                changeFileNameToUnicode(f);
                            }
                        }
                    }
                }else {
                    if (Constants.CHANGE_HIDDEN){
                        File newFile = new File(f.getParentFile()+"/"+AIOmmTool.getUnicode(f.getName()));
                        if (f.renameTo(newFile)){
                            if (isImageFile(f.toString()) || isVideoFile(f.toString()) || isAudioFile(f.toString())){
                                paths.add(newFile.toString());
                                mimeType.add(getMimeType(newFile.toString()));
                            }
                            if (isAudioFile(newFile.toString())){
                                editAudioTag(newFile);
                            }
                        }else {
                            if (isAudioFile(f.toString())){
                                editAudioTag(f);
                            }
                        }
                    }else {
                        if (!isHidden(f)) {
                            File newFile = new File(f.getParentFile()+"/"+AIOmmTool.getUnicode(f.getName()));
                            if (f.renameTo(newFile)){
                                if (isImageFile(f.toString()) || isVideoFile(f.toString()) || isAudioFile(f.toString())){
                                    paths.add(newFile.toString());
                                    mimeType.add(getMimeType(newFile.toString()));
                                }
                                if (isAudioFile(newFile.toString())){
                                    editAudioTag(newFile);
                                }
                            }else {
                                if (isAudioFile(f.toString())){
                                    editAudioTag(f);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isHidden(File path){
        return path.isHidden();
    }

    public static void changeFileNameCustomExtension(File file,String extension,boolean changeFolderName){
        if (file!=null){
            for (File f:file.listFiles()){
                if (f.isDirectory()){
                    if (Constants.CHANGE_HIDDEN){
                        if (changeFolderName){
                            File newFile = new File(f.getParentFile()+"/"+AIOmmTool.getUnicode(f.getName()));
                            if (f.renameTo(newFile)) {
                                changeFileNameCustomExtension(newFile,extension,changeFolderName);
                            }else {
                                changeFileNameCustomExtension(f,extension,changeFolderName);
                            }
                        }else {
                            changeFileNameCustomExtension(f,extension,changeFolderName);
                        }
                    }else {
                        if (!isHidden(f)){
                            if (changeFolderName){
                                File newFile = new File(f.getParentFile()+"/"+AIOmmTool.getUnicode(f.getName()));
                                if (f.renameTo(newFile)) {
                                    changeFileNameCustomExtension(newFile,extension,changeFolderName);
                                }else {
                                    changeFileNameCustomExtension(f,extension,changeFolderName);
                                }
                            }else {
                                changeFileNameCustomExtension(f,extension,changeFolderName);
                            }
                        }
                    }
                }else {
                    if (Constants.CHANGE_HIDDEN){
                        String fileExt = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(f).toString());
                        if (extension.contains(fileExt)) {
                            File newFile = new File(f.getParentFile() + "/" + AIOmmTool.getUnicode(f.getName()));
                            if (f.renameTo(newFile)) {
                                if (isImageFile(f.toString()) || isVideoFile(f.toString()) || isAudioFile(f.toString())){
                                    paths.add(newFile.toString());
                                    mimeType.add(getMimeType(newFile.toString()));
                                }
                                if (isAudioFile(newFile.toString())){
                                    editAudioTag(newFile);
                                }
                            }else {
                                if (isAudioFile(f.toString())){
                                    editAudioTag(f);
                                }
                            }
                        }
                    }else {
                        if (!isHidden(f)){
                            String fileExt = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(f).toString());
                            if (extension.contains(fileExt)) {
                                File newFile = new File(f.getParentFile() + "/" + AIOmmTool.getUnicode(f.getName()));
                                if (f.renameTo(newFile)) {
                                    if (isImageFile(f.toString()) || isVideoFile(f.toString()) || isAudioFile(f.toString())){
                                        paths.add(newFile.toString());
                                        mimeType.add(getMimeType(newFile.toString()));
                                    }
                                    if (isAudioFile(newFile.toString())){
                                        editAudioTag(newFile);
                                    }
                                }else {
                                    if (isAudioFile(f.toString())){
                                        editAudioTag(f);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void resetPathsAndMimeType() {
        paths = new ArrayList<>();
        mimeType = new ArrayList<>();
    }


    public static void audioFileNameToUnicode(File file){
        if (file!=null){
            for (File f:file.listFiles()){
                if (f.isDirectory()){
                    if (Constants.CHANGE_HIDDEN){
                        audioFileNameToUnicode(f);
                    }else {
                        if (!isHidden(f)){
                            audioFileNameToUnicode(f);
                        }
                    }
                }else {
                    if (isAudioFile(f.toString())) {
                        if (Constants.CHANGE_HIDDEN){
                            File newFile = new File(f.getParentFile() + "/" + AIOmmTool.getUnicode(f.getName()));
                            if (f.renameTo(newFile)){
                                editAudioTag(newFile);
                                    paths.add(newFile.toString());
                                    mimeType.add(getMimeType(newFile.toString()));
                            }
                        }else {
                            if (!isHidden(f)){
                                File newFile = new File(f.getParentFile() + "/" + AIOmmTool.getUnicode(f.getName()));
                                if (f.renameTo(newFile)){
                                    editAudioTag(newFile);
                                    paths.add(newFile.toString());
                                    mimeType.add(getMimeType(newFile.toString()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public static void videoFileNameToUnicode(File file){
        if (file!=null){
            for (File f:file.listFiles()){
                if (f.isDirectory()){
                    if (Constants.CHANGE_HIDDEN){
                        videoFileNameToUnicode(f);
                    }else {
                        if (!isHidden(f)){
                            videoFileNameToUnicode(f);
                        }
                    }
                }else {
                    if (isVideoFile(f.toString())) {
                        if (Constants.CHANGE_HIDDEN){
                            File newFile = new File(f.getParentFile() + "/" + AIOmmTool.getUnicode(f.getName()));
                            if (f.renameTo(newFile)){
                                    paths.add(newFile.toString());
                                    mimeType.add(getMimeType(newFile.toString()));
                            }
                        }else {
                            if (!isHidden(f)){
                                File newFile = new File(f.getParentFile() + "/" + AIOmmTool.getUnicode(f.getName()));
                                if (f.renameTo(newFile)){
                                    paths.add(newFile.toString());
                                    mimeType.add(getMimeType(newFile.toString()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void imageFileNameToUnicode(File file){
        if (file!=null){
            for (File f:file.listFiles()){
                if (f.isDirectory()){
                    if (Constants.CHANGE_HIDDEN){
                        imageFileNameToUnicode(f);
                    }else {
                        if (!isHidden(f)){
                            imageFileNameToUnicode(f);
                        }
                    }
                }else {
                    if (isImageFile(f.toString())) {
                       if (Constants.CHANGE_HIDDEN){
                           File newFile = new File(f.getParentFile() + "/" + AIOmmTool.getUnicode(f.getName()));
                           if (f.renameTo(newFile)){
                                   paths.add(newFile.toString());
                                   mimeType.add(getMimeType(newFile.toString()));
                           }
                       }else {
                           if (!isHidden(f)){
                               File newFile = new File(f.getParentFile() + "/" + AIOmmTool.getUnicode(f.getName()));
                               if (f.renameTo(newFile)){
                                   paths.add(newFile.toString());
                                   mimeType.add(getMimeType(newFile.toString()));
                               }
                           }
                       }
                    }
                }
            }
        }
    }

    private static boolean isVideoFile(String path) {
        return MediaFileTypeUtil.getInstance().isVideoFile(path);
    }

    private static boolean isAudioFile(String path) {
        return MediaFileTypeUtil.getInstance().isAudioFile(path);
    }

    private static boolean isImageFile(String path) {
       return MediaFileTypeUtil.getInstance().isImageFile(path);
    }

    public static void updateMEDIA(final Activity context){
        String[] p = new String[paths.size()];
        paths.toArray(p);

        String[] m = new String[mimeType.size()];
        mimeType.toArray(m);

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Update media..");
        if (m.length>1) {
            progressDialog.show();
        }else {
            if (context==MainActivity.instance) {
                MainActivity.instance.done(true);
            }else {
                CustomizeActivity.instance.done(true);
            }
        }

        MediaScannerConnection.scanFile(context, p, m, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {
                System.out.println("Scanned: "+path);
                if (check==paths.size()-1){
                    progressDialog.dismiss();
                    if (context==MainActivity.instance) {
                        MainActivity.instance.done(true);
                    }else {
                        CustomizeActivity.instance.done(true);
                    }
                    check=0;
                }else check++;
            }
        });
    }

    private static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private static void editAudioTag(File audio){
        if (audio.getName().endsWith(".mp3")) {
            AudioFile audioFile = null;
            try {
                audioFile = AudioFileIO.read(audio);
                Tag tag = audioFile.getTagOrCreateAndSetDefault();

                Map<FieldKey, String> fieldKeyValueMap = new EnumMap<>(FieldKey.class);
                fieldKeyValueMap.put(FieldKey.TITLE, AIOmmTool.getUnicode(tag.getFirst(FieldKey.TITLE)));
                fieldKeyValueMap.put(FieldKey.ALBUM, AIOmmTool.getUnicode(tag.getFirst(FieldKey.ALBUM)));
                fieldKeyValueMap.put(FieldKey.ARTIST, AIOmmTool.getUnicode(tag.getFirst(FieldKey.ARTIST)));
                fieldKeyValueMap.put(FieldKey.GENRE, AIOmmTool.getUnicode(tag.getFirst(FieldKey.GENRE)));
                fieldKeyValueMap.put(FieldKey.YEAR, AIOmmTool.getUnicode(tag.getFirst(FieldKey.YEAR)));
                fieldKeyValueMap.put(FieldKey.TRACK, AIOmmTool.getUnicode(tag.getFirst(FieldKey.TRACK)));
                fieldKeyValueMap.put(FieldKey.LYRICS, AIOmmTool.getUnicode(tag.getFirst(FieldKey.LYRICS)));
                fieldKeyValueMap.put(FieldKey.ALBUM_ARTIST, AIOmmTool.getUnicode(tag.getFirst(FieldKey.ALBUM_ARTIST)));
                fieldKeyValueMap.put(FieldKey.ARTISTS, AIOmmTool.getUnicode(tag.getFirst(FieldKey.ARTISTS)));
                fieldKeyValueMap.put(FieldKey.COMMENT, AIOmmTool.getUnicode(tag.getFirst(FieldKey.COMMENT)));
                fieldKeyValueMap.put(FieldKey.COMPOSER, AIOmmTool.getUnicode(tag.getFirst(FieldKey.COMPOSER)));

                for (Map.Entry<FieldKey, String> entry : fieldKeyValueMap.entrySet()) {
                    try {
                        tag.setField(entry.getKey(), entry.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                audioFile.commit();
            } catch (CannotReadException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TagException e) {
                e.printStackTrace();
            } catch (ReadOnlyFileException e) {
                e.printStackTrace();
            } catch (InvalidAudioFrameException e) {
                e.printStackTrace();
            } catch (CannotWriteException e) {
                e.printStackTrace();
            }
        }
    }


    public static void changeContacts(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                System.out.println(updateContact(context,id,AIOmmTool.getUnicode(name)));
            }
        }
        if(cur!=null){
            cur.close();
        }
    }

    private static boolean updateContact(Context context,String contactID, String contactName) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation
                .newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE
                        + "=?", new String[]{contactID, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE})
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactName)
                .build());
        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static String[] getExternalStorageDirectories(Context context) {
        String LOG_TAG = "SDCARD";

        List<String> results = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //Method 1 for KitKat & above
            File[] externalDirs = context.getExternalFilesDirs(null);

            for (File file : externalDirs) {
                String path = file.getPath().split("/Android")[0];

                boolean addPath = false;

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    addPath = Environment.isExternalStorageRemovable(file);
                }
                else{
                    addPath = Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(file));
                }

                if(addPath){
                    results.add(path);
                }
            }
        }

        if(results.isEmpty()) { //Method 2 for all versions
            // better variation of: http://stackoverflow.com/a/40123073/5002496
            String output = "";
            try {
                final Process process = new ProcessBuilder().command("mount | grep /dev/block/vold")
                        .redirectErrorStream(true).start();
                process.waitFor();
                final InputStream is = process.getInputStream();
                final byte[] buffer = new byte[1024];
                while (is.read(buffer) != -1) {
                    output = output + new String(buffer);
                }
                is.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            if(!output.trim().isEmpty()) {
                String devicePoints[] = output.split("\n");
                for(String voldPoint: devicePoints) {
                    results.add(voldPoint.split(" ")[2]);
                }
            }
        }

        //Below few lines is to remove paths which may not be external memory card, like OTG (feel free to comment them out)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).toLowerCase().matches(".*[0-9a-f]{4}[-][0-9a-f]{4}")) {
                    Log.d(LOG_TAG, results.get(i) + " might not be extSDcard");
                    results.remove(i--);
                }
            }
        } else {
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).toLowerCase().contains("ext") && !results.get(i).toLowerCase().contains("sdcard")) {
                    Log.d(LOG_TAG, results.get(i)+" might not be extSDcard");
                    results.remove(i--);
                }
            }
        }

        String[] storageDirectories = new String[results.size()];
        for(int i=0; i<results.size(); ++i) storageDirectories[i] = results.get(i);

        return storageDirectories;
    }

    public static void scanMediaNormal(Context context){
        Bundle bundle = new Bundle();
        bundle.putString("volume", "external");
        context.startService(new Intent().setComponent(new ComponentName("com.android.providers.media", "com.android.providers.media.MediaScannerService")).putExtras(bundle));
        Toast.makeText(context, "Media scanner started", Toast.LENGTH_SHORT).show();
    }
}
