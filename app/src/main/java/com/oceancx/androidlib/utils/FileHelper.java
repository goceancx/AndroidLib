package com.oceancx.androidlib.utils;

import android.os.Environment;

import com.oceancx.androidlib.DebugLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by oceancx on 15/10/26.
 */
public class FileHelper {
    static String env = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

    public static String load(String path) {
        DebugLog.e("load file:" + path);
        try {
            FileInputStream fis = new FileInputStream(env + path);
            byte[] bytes = new byte[1024];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((len = fis.read(bytes)) > 0) {
                bos.write(bytes, 0, len);
            }
            fis.close();
            String route = bos.toString();
            return route;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void delete(String path) {

        if (path == null || path.equals(""))
            path = env + "1.txt";
        else
            path = env + path;

        File f = new File(path);
        if (!f.exists()) {
            return;
        } else {
            f.delete();
        }
    }

    public static void save(String path, String text) {

        if (path == null || path.equals(""))
            path = env + "1.txt";
        else
            path = env + path;

        File f = new File(path);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            f.delete();
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(f);

            fw.write(text + "");
            fw.close();
            DebugLog.e("FileSave:" + text);
            DebugLog.e(path + "-文件保存成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String hash(String str) {
        int val = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            val += c;
        }
        return String.valueOf(val);
    }
}
