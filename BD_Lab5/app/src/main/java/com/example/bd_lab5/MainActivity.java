package com.example.bd_lab5;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String fname = "Lab5.txt";
    HashTable hashT = new HashTable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

 if (ExistBase(fname)) {
     try {
         ReadFileHash();
     } catch (IOException e) {
         e.printStackTrace();
     }
     try {
         openText();
     } catch (IOException e) {
         e.printStackTrace();
     }
 } else {
            ShowDialogCreateFile();
     try {
         openText();
     } catch (IOException e) {
         e.printStackTrace();
     }
 }

    }

    private void ShowDialogCreateFile() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Создается файл " + fname).setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Log_02", "Создание файла " + fname);
            }
        });
        AlertDialog ad = b.create();
        ad.show();

        try {
            FileOutputStream fileOutputStream = openFileOutput(fname, Context.MODE_PRIVATE);

            fileOutputStream.close();

        } catch (Exception e) {
            Log.d("ShowDialogCreateFile(): ERROR: ", e.getStackTrace()+"");
        }

    }

    private boolean ExistBase(String fname) {
        boolean rc = false;
        File f = new File(super.getFilesDir(), fname);
        if (rc = f.exists()) Log.d("Log_02", "Файл " + fname + " существуют");
        else Log.d("Log_02", "Файл " + fname + " не найден");

        return rc;
    }

    // вывод всего файла в текствью
    private void openText() throws IOException {
        TextView textOut = (TextView) findViewById(R.id.outTextView);
        String str ="";
       try {
           ReadFileHash();

       }
       catch(Exception a){}
        str = readFullHash();
        textOut.setText(str);
    }


    private void ReadFileHash() throws IOException {

        File fileP = new File("/data/data/com.example.bd_lab5/files/Lab5.txt");

        File f = super.getCacheDir();

        int chars = 15;
        String newStr;
        String[] words;
        // открываем файл только для чтения
        RandomAccessFile file = new RandomAccessFile(fileP.getPath(), "r");
        for(int i = 0; i<150; i+=15) {
            file.seek(i);
            byte[] bytes = new byte[chars];
            file.read(bytes);
            newStr = new String(bytes);
            Log.d("ReadFieHash: ", newStr);
            words = newStr.split(" ");
            try {
                hashT.setItem(words[0], words[1]);
                Log.d("ReadFieHash: ", "////////////////////////////////////////"+words[0] + " " + words[1]+"////////////////////////////////////////");
            } catch (Exception e) {
                Log.d("try", e.getMessage());
            }
        }
        file.close();
    }

    private String readFullHash() throws IOException {

        // открываем файл только для чтения
        RandomAccessFile file = new RandomAccessFile("/data/data/com.example.bd_lab5/files/Lab5.txt", "r");
        int chars = (int) file.length();
        String newStr;
        String[] words;

            file.seek(0);
            byte[] bytes = new byte[chars];
            file.read(bytes);
            newStr = new String(bytes);
            Log.d("ReadFieHash: ", newStr);
            file.close();
            return newStr;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void SaveOnclick(View v) throws IOException {

        EditText key = (EditText) findViewById(R.id.editTextKey);
        EditText value = (EditText) findViewById(R.id.editTextValue);

        RandomAccessFile raf = new RandomAccessFile("/data/data/com.example.bd_lab5/files/" + fname, "rw");

        if((int)raf.length()>150) {
            Toast.makeText(this,"Хэш таблица заполнена",Toast.LENGTH_LONG);
        }

        File file = new File("/data/data/com.example.bd_lab5/files/" + fname);
        int k = 0;
        String str = key.getText().toString() + " " + value.getText().toString();

        //заполняем остальную строку пробелами
        k = 15 - str.length();
        for(int i =k; i!=0;i--) str+=" ";


   if (hashT.getItem(key.getText().toString()) != "") {
            String fullHash = readFullHash();
            String newStr = readFullHash();

            int index = 0;


       for(int i = (int)raf.length(); i>0; i-=15) {
           index = newStr.indexOf(key.getText().toString(), index+1);//получаем указатель

           Log.d("INDEX ", String.valueOf(index));
           if (index % 15 == 0) {
               writeData(file.getPath(), str, index);
               ReadFileHash();
               openText();
               return;
           }
       }
            ReadFileHash();
            openText();
            return;
        }

        writeData(file.getPath(), str,  (int) raf.length());
        ReadFileHash();
        openText();

        return;
        }
// ищем свободное место
        private int getFree() throws IOException {
            File fileP = new File("/data/data/com.example.bd_lab5/files/Lab5.txt");
            int chars = 1;
            String newStr;
            String[] words;
            byte[] bytes = new byte[chars];
            // открываем файл только для чтения
            RandomAccessFile file = new RandomAccessFile(fileP.getPath(), "r");
            for(int i = 0; i<150; i+=15) {
                file.seek(i);
                file.read(bytes);
                Log.d("ReadFieHashAAAAA: ", new String(bytes)+"----////////////////////////////////////////");
            if(new String(bytes)=="")
            {
            Log.d("ReadFieHashAAAAA: ", new String(bytes)+"----////////////////////////////////////////");
            file.close();
            return i;
            }
            }
            file.close();

            return -1;
        }

    // метод для записи данных в файл
    private static void writeData(String filePath, String data, int seek) throws IOException {
        // открываем файл с возможностью как чтения, так и записи
        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
        // переходим на определенный индекс
        file.seek(seek);
        // запишем данные в этом месте
        file.write(data.getBytes());
        file.close();
    }

    // метод для чтения с файла
    private static void readCharsFromFile(String filePath, int seek, int chars) throws IOException {
        // открываем файл только для чтения
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        for(int i = 0; i<150; i++)
        {
        file.seek(i);
        byte[] bytes = new byte[chars];
        file.read(bytes);
        }
        file.close();
        //return bytes;
    }
    public void GetValueOnclick(View v)
    {

       EditText textView = findViewById(R.id.editTextValueGet);
       EditText findKey = findViewById(R.id.editTextKeyGet);

       textView.setText(hashT.getItem(findKey.getText().toString()));

    }

}