package com.example.lab111.Activity.AddActivities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;
import java.util.Calendar;

import com.example.lab111.Database.DbHelper;
import com.example.lab111.Database.FacultyDb;
import com.example.lab111.Helper.DateTimeHelper;
import com.example.lab111.Helper.SystemHelper;
import com.example.lab111.Model.Faculty;
import com.example.lab111.R;

public class AddFacultyActivity extends AppCompatActivity {

    private EditText facultyName;
    private EditText dean;
    private EditText startWork;
    private EditText endWork;
    private Calendar time;
    private Button addFaculty;

    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);

        binding();
        setTimeListeners();
        setButtonListeners();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

    private void binding() {
        facultyName = findViewById(R.id.edtFacultyName);
        dean = findViewById(R.id.edtDean);
        startWork = findViewById(R.id.edtStartWork);
        endWork = findViewById(R.id.edtEndWork);
        time = Calendar.getInstance();
        addFaculty = findViewById(R.id.btnAddFaculty);

        db = new DbHelper(getApplicationContext()).getReadableDatabase();
    }

    private void setTimeListeners() {
        startWork.setInputType(InputType.TYPE_NULL);
        endWork.setInputType(InputType.TYPE_NULL);

        TimePickerDialog.OnTimeSetListener startT = (view, hourOfDay, minutes) -> {
            startWork.setText(DateTimeHelper.getGeneralTimeFormat(hourOfDay, minutes));
        };
        TimePickerDialog.OnTimeSetListener endT = (view, hourOfDay, minutes) -> {
            endWork.setText(DateTimeHelper.getGeneralTimeFormat(hourOfDay, minutes));
        };

        TimePickerDialog startWorkDialog = new TimePickerDialog(this, startT,
                time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE), true);
        TimePickerDialog endWorkDialog = new TimePickerDialog(this, endT,
                time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE), true);


        startWork.setOnClickListener(view -> {
            SystemHelper.hideKeyboard(this);
            startWorkDialog.show();
        });
        startWork.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus) {
                SystemHelper.hideKeyboard(this);
                startWorkDialog.show();
            }
        });

        endWork.setOnClickListener(view -> {
            SystemHelper.hideKeyboard(this);
            endWorkDialog.show();
        });
        endWork.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus) {
                SystemHelper.hideKeyboard(this);
                endWorkDialog.show();
            }
        });
    }

    private void setButtonListeners() {
        addFaculty.setOnClickListener(view -> {
            try {
                String facultyName = this.facultyName.getText().toString();
                String dean = this.dean.getText().toString();
                LocalTime startWork = LocalTime.of(
                        Integer.parseInt(this.startWork.getText().toString().substring(0, 2)),
                        Integer.parseInt(this.startWork.getText().toString().substring(3, 5)));
                LocalTime endWork = LocalTime.of(
                        Integer.parseInt(this.endWork.getText().toString().substring(0, 2)),
                        Integer.parseInt(this.endWork.getText().toString().substring(3, 5)));

                Faculty faculty = new Faculty(facultyName, dean, startWork, endWork);

                if(FacultyDb.add(db, faculty) != -1) {
                    Toast.makeText(this, "Факультет успешно добавлен", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else Toast.makeText(this, "Ошибка добавления", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Проверьте введенные данные", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        facultyName.setText("");
        dean.setText("");
        startWork.setText("");
        endWork.setText("");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, AddActivity.class));
    }
}