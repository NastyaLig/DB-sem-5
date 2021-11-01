package com.example.bd4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.*;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    String fname = "Base_Lab.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
    }

    public void SaveInfo(View view) {
        TextView T1 = (TextView) findViewById(R.id.SurnameT);
        TextView T2 = (TextView) findViewById(R.id.NameT);

        String name = String.valueOf(T1.getText());
        String surname = String.valueOf(T2.getText());

        WriteToFile(name, surname);
        T1.setText("");
        T2.setText("");
    }
    public void WriteToFile(String n, String s){
        String WritebleText = n + " " + s + "\r\n";
        try {
            FileOutputStream fileOutput = openFileOutput("Base_Lab.txt",MODE_APPEND);
            fileOutput.write(WritebleText.getBytes());
            fileOutput.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   public void OpenText (View view){
       TextView t2 = (TextView) findViewById(R.id.textView2);
       FileInputStream fin = null;
       try {
           fin = openFileInput(fname);
           byte[] bytes = new byte[fin.available()];
           fin.read(bytes);
           String text = new String (bytes);
           t2.setText(text);
       }
       catch(IOException ex) {
           Log.d("Log_02", "Файл" + fname + " невозможно прочитать");
       }
   }

    private boolean ExistFile(String FileName)
    {
        boolean rc = false;
        File f = new File(super.getFilesDir(), FileName);
        if(rc = f.exists()) Log.d("Log_02", "Файл "+ FileName + " существует!");
        else
        {
            Log.d("Log_02", "Файл "+ FileName + " не найден, но сейчас будет создан!");
            try {
                f.createNewFile();
                Log.d("Log_02", "Файл "+ FileName + " создан!");
            }
            catch (IOException e)
            {Log.d("Log_02", "Файл "+ FileName + " не открыт");}
        }
        return rc;
    }
    public void ClearFile()
    {
        TextView T = findViewById(R.id.textView2);
    }
}