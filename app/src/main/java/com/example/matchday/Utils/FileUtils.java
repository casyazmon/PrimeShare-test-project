package com.example.matchday.Utils;

import android.content.Context;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;

public class FileUtils {

    //public  static ArrayList<FoldersAndFiles> directories = new ArrayList<FoldersAndFiles>();

    public static String getPDF(){
        return "https://mindorks.s3.ap-south-1.amazonaws.com/courses/MindOrks_Android_Online_Professional_Course-Syllabus.pdf";
    }

    public static String getPDF(String url){
        return url;
    }

    public static String getPDFFromAssets(){
        return "MindOrks_Android_Online_Professional_Course-Syllabus.pdf";
    }

    public static String getRootDirPath(Context context) {

        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            File file = ContextCompat.getExternalFilesDirs(
                    context.getApplicationContext(),null
            )[0];
            return file.getAbsolutePath();

        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    public static ArrayList<String> getDirectories() {
        ArrayList<String> directories = new ArrayList<>();

        final String state = Environment.getExternalStorageState();
        final File path = Environment.getRootDirectory();

        if ( Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) ) {
            // we can read the External Storage...
            File[] files = path.listFiles();
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    directories.add(inFile.getName());
                }
            }
        }

        return directories;
    }
}
