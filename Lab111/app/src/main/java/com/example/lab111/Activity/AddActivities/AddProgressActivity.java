package com.example.lab111.Activity.AddActivities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import com.example.lab111.Database.DbHelper;
import com.example.lab111.Database.ProgressDb;
import com.example.lab111.Helper.DateTimeHelper;
import com.example.lab111.Helper.SpinnerSetter;
import com.example.lab111.Helper.SpinnerSetterResult;
import com.example.lab111.Helper.SystemHelper;
import com.example.lab111.Model.Group;
import com.example.lab111.Model.Progress;
import com.example.lab111.Model.Student;
import com.example.lab111.Model.Subject;
import com.example.lab111.R;

public class AddProgressActivity extends AppCompatActivity {

    private Spinner spnrGroup;
    private Spinner spnrStudent;
    private Spinner spnrSubject;
    private EditText examDate;
    private Calendar date;
    private EditText mark;
    private EditText teacher;
    private Button addProgress;

    private Context context;
    private SQLiteDatabase db;
    private List<Group> listGroups;
    private List<Student> listStudentsByGroup;
    private List<Subject> listSubjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_progress);

        binding();
        setDateListeners();
        setListeners();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

    private void binding() {
        spnrGroup = findViewById(R.id.spnrGroup);
        spnrStudent = findViewById(R.id.spnrStudent);
        spnrSubject = findViewById(R.id.spnrSubject);
        examDate = findViewById(R.id.edtExamDate);
        date = Calendar.getInstance();
        mark = findViewById(R.id.edtExamMark);
        teacher = findViewById(R.id.edtTeacherName);
        addProgress = findViewById(R.id.btnAddProgress);

        context = this;
        db = new DbHelper(getApplicationContext()).getReadableDatabase();


        SpinnerSetterResult<Group> spnrGroupResult = SpinnerSetter.setGroups(getApplicationContext());
        listGroups = spnrGroupResult.getList();
        ArrayAdapter<String> groupAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spnrGroupResult.getStr());
        spnrGroup.setAdapter(groupAdapter);


        SpinnerSetterResult<Subject> spnrSubjectResult = SpinnerSetter.setSubjects(getApplicationContext());
        listSubjects = spnrSubjectResult.getList();
        ArrayAdapter<String> subjectAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spnrSubjectResult.getStr());
        spnrSubject.setAdapter(subjectAdapter);
    }

    private void setDateListeners() {
        examDate.setInputType(InputType.TYPE_NULL);

        DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) ->
                examDate.setText(DateTimeHelper.getGeneralDateFormat(year, monthOfYear + 1, dayOfMonth));

        DatePickerDialog birthdayDialog = new DatePickerDialog(this, d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH));

        examDate.setOnClickListener(view -> {
            SystemHelper.hideKeyboard(this);
            birthdayDialog.show();
        });
        examDate.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus) {
                SystemHelper.hideKeyboard(this);
                birthdayDialog.show();
            }
        });
    }

    private void setListeners() {
        spnrGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SpinnerSetterResult<Student> spnrStudentResult =
                        SpinnerSetter.setStudents(getApplicationContext(),
                                listGroups.size(),
                                listGroups.get(spnrGroup.getSelectedItemPosition()).getId());

                listStudentsByGroup = spnrStudentResult.getList();

                ArrayAdapter<String> studentAdapter =
                        new ArrayAdapter<>(context,
                                android.R.layout.simple_spinner_dropdown_item,
                                spnrStudentResult.getStr());
                spnrStudent.setAdapter(studentAdapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {return;}
        });

        addProgress.setOnClickListener(view -> {
            try {
                int studentId = listStudentsByGroup.get(spnrStudent.getSelectedItemPosition()).getId();
                int subjectId = listSubjects.get(spnrSubject.getSelectedItemPosition()).getId();
                LocalDate examDate = LocalDate.of(
                        Integer.parseInt(this.examDate.getText().toString().substring(6, 10)),
                        Integer.parseInt(this.examDate.getText().toString().substring(3, 5)),
                        Integer.parseInt(this.examDate.getText().toString().substring(0, 2)));
                int mark = Integer.parseInt(this.mark.getText().toString());
                String teacher = this.teacher.getText().toString();

                Progress progress = new Progress(studentId, subjectId, examDate, mark, teacher);

                if(ProgressDb.add(db, progress) != -1) {
                    Toast.makeText(this, "Оценка успешно сохранена", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Проверьте введенные данные", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        examDate.setText("");
        mark.setText("");
        teacher.setText("");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, AddActivity.class));
    }
}