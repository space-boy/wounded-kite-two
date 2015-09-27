package com.sunrise.ex.screengifv2;

import java.io.File;
import java.util.ArrayList;

public class FileStructure {

    public ArrayList<File> mFiles = new ArrayList<File>();
    public static final String TAG = "FileStructure";

    public void listFiles(File init){

        File[] files = init.listFiles();

        if(init.isDirectory()) {
            for (File inFile : files) {
                if (inFile.isFile()) {
                    mFiles.add(inFile);
                }

                if (inFile.isDirectory() && (!inFile.toString().startsWith(".")) && (!inFile.toString().contains("com"))) {
                    mFiles.add(inFile);
                }
            }
        }
    }

    public void searchFiles(File init) {

        if(mFiles != null){
            mFiles.clear();
        }

        File[] files = init.listFiles();

        if (init.isDirectory()) {
            for (File inFile : files) {

                mFiles.add(inFile);


                if (inFile.isDirectory()) {
                    mFiles.add(inFile);
                    listFiles(inFile);
                }
            }
        }
    }

    public ArrayList<File> getFiles(File files){
        listFiles(files);
        return mFiles;
    }

    public ArrayList<File>getSearchFiles(File files){
        searchFiles(files);
        return mFiles;
    }
}
