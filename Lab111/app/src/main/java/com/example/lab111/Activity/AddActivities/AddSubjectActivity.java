package com.example.lab111.Activity.AddActivities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab111.Database.DbHelper;
import com.example.lab111.Database.SubjectDb;
import com.example.lab111.Model.Subject;
import com.example.lab111.R;

public class AddSubjectActivity extends AppCompatActivity {

    private EditText subjectName;
    private Button addSubject;

    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        binding();
        setListeners();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

    private void binding() {
        subjectName = findViewById(R.id.edtSubjectName);
        addSubject = findViewById(R.id.btnAddSubject);

        db = new DbHelper(getApplicationContext()).getReadableDatabase();
    }

    private void setListeners() {
        addSubject.setOnClickListener(view -> {
            try {
                if(SubjectDb.add(db, new Subject(subjectName.getText().toString())) != -1) {
                    Toast.makeText(this, "Предмет успешно добавлен", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else Toast.makeText(this, "Ошибка добавления", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Проверьте введенные данные", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        subjectName.setText("");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, AddActivity.class));
    }
}