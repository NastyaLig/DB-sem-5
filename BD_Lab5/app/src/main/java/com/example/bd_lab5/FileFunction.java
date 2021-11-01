package com.example.bd_lab5;


import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FileFunction {

    public void CreateFile(String fpatch, String fname){

        File f = new File(fpatch, fname);
        try
        {
            f.createNewFile();
            Log.d("Log_02","Файл"+fname+"создан");

        } catch (IOException e) {
            Log.d("Log_02","Файл"+fname+"не создан");
        }

    }


}