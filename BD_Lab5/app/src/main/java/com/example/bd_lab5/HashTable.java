package com.example.bd_lab5;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HashTable implements Serializable {
    private final int MAX = 10;
    @Nullable
    private List<List<String[]>> arr;
    public HashTable(){

        arr = new ArrayList<>(MAX);
        for(int i = 0; i<10;i++)
            arr.add(new ArrayList<>());
    }

    private int getHash(String key){
        int hash = 0;
        hash = key.hashCode();
        return hash%MAX;
    }

    public String getItem(String key){
        int h = getHash(key);
        String value = "";
        for (@Nullable String[] el: arr.get(h)) {
            if (el != null && el[0].equals(key)) {
                value += el[1] + " ";
Log.d("GETITEM: ", value );
            }
        }
        return value;
    }

    public void setItem(String key, String value){
        int h = getHash(key);
        for (@Nullable List<String[]> list:arr) {
            int i = 0;
            for (@Nullable String[] el:list) {
                if (el[0].equals(key)) {
                    arr.get(h).set(i, new String[]{key, value});
                    return;
                }
            }
            ++i;
        }
       arr.get(h).add(new String[]{key, value});
    }

    public String toString(){
        String result = "";
        for (@Nullable List<String[]> list:arr) {
            for (@Nullable String[] el:list) {
                if(el!=null)
                    result += el[0] + " " + el[1] + "\n";
                Log.d("Result", el[0] + " " + el[1] + "\n");
            }
        }
        return result;
    }
}
