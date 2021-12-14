package com.example.lab111.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab111.Activity.AddActivities.AddActivity;
import com.example.lab111.Activity.ShowActivities.ShowActivity;
import com.example.lab111.ContentProvider.StudentsContract;
import com.example.lab111.R;

public class MainActivity extends AppCompatActivity {

    private Button addActivity;
    private Button editActivity;
    private Button showActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Uri: ", StudentsContract.CONTENT_GROUP_URI.toString());

        binding();
        setListeners();
    }

    private void binding() {
        addActivity = findViewById(R.id.btnAddActivity);
        editActivity = findViewById(R.id.btnEditActivity);
        showActivity = findViewById(R.id.btnShowActivity);
    }

    private void setListeners() {
        addActivity.setOnClickListener(view ->
                startActivity(new Intent(this, AddActivity.class)));
        editActivity.setOnClickListener(view ->
                startActivity(new Intent(this, StudentsActivity.class)));
        showActivity.setOnClickListener(view ->
                startActivity(new Intent(this, ShowActivity.class)));
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}