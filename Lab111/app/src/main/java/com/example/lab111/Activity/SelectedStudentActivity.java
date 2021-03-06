package com.example.lab111.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.Calendar;

import com.example.lab111.Database.DbHelper;
import com.example.lab111.Database.StudentDb;
import com.example.lab111.Model.Student;
import com.example.lab111.R;

public class SelectedStudentActivity extends AppCompatActivity {

    private EditText name;
    private EditText birthday;
    private Calendar date;
    private EditText address;
    private Button saveStudent;
    private Button deleteStudent;

    private Student selectedStudent;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_student);

        binding();
        setListeners();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

    private void binding() {
        name = findViewById(R.id.edtStudentName);
        birthday = findViewById(R.id.edtStudentBirthday);
        date = Calendar.getInstance();
        address = findViewById(R.id.edtStudentAddress);
        saveStudent = findViewById(R.id.btnAddStudent);
        deleteStudent = findViewById(R.id.btnDeleteStudent);

        db = new DbHelper(getApplicationContext()).getReadableDatabase();

        Intent intent = getIntent();
        selectedStudent = (Student) intent.getSerializableExtra("Student");

        name.setText(selectedStudent.getName());
        birthday.setText(selectedStudent.getBirthday().toString());
        address.setText(selectedStudent.getAddress());
    }

    private void setListeners() {
        saveStudent.setOnClickListener(view -> {
            try {
                Student student = new Student();
                student.setId(selectedStudent.getId());
                student.setGroupId(selectedStudent.getGroupId());
                student.setName(name.getText().toString());
                LocalDate birthday = LocalDate.of(
                        Integer.parseInt(this.birthday.getText().toString().substring(0, 4)),
                        Integer.parseInt(this.birthday.getText().toString().substring(5, 7)),
                        Integer.parseInt(this.birthday.getText().toString().substring(8, 10)));

                student.setBirthday(birthday);
                student.setAddress(address.getText().toString());

                StudentDb.updateStudent(db, student);
                Toast.makeText(this, "?????????????? ?????????????? ??????????????", Toast.LENGTH_SHORT).show();
            } catch (Exception e ) {
                Toast.makeText(this, "???????????? ??????????????????", Toast.LENGTH_SHORT).show();
            }
        });

        deleteStudent.setOnClickListener(view -> {
            try {
                if(StudentDb.deleteStudent(db, selectedStudent) > 0) {
                    Toast.makeText(this, "?????????????? ?????????????? ????????????", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, StudentsActivity.class));
                }
            } catch (SQLiteConstraintException e) {
                Toast.makeText(this, "???????? ??????????????????", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "???????????? ????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, StudentsActivity.class));
    }
}