package com.example.lab111.Activity.ShowActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import com.example.lab111.Activity.MainActivity;
import com.example.lab111.Database.DbHelper;
import com.example.lab111.Database.ProgressDb;
import com.example.lab111.Helper.DateTimeHelper;
import com.example.lab111.Helper.SpinnerSetter;
import com.example.lab111.Helper.SpinnerSetterResult;
import com.example.lab111.Helper.SystemHelper;
import com.example.lab111.Model.Faculty;
import com.example.lab111.Model.Group;
import com.example.lab111.R;

public class ShowActivity extends AppCompatActivity {

    private Spinner spnrFaculty;
    private Spinner spnrGroup;
    private Spinner spnrTypeOfLastPeriod;
    private RadioButton radioBtnPeriod;
    private Calendar date;
    private EditText startDate;
    private EditText endDate;
    private EditText numberOfLast;
    private Button showAvgIndicat;
    private Button showBestStud;
    private Button showUnderperfStud;
    private Button showComparByGroups;
    private Button showComparByFac;
    private Button showComparByGroups2;

    private SQLiteDatabase db;
    private List<Faculty> listFaculties;
    private List<Group> listGroups;
    private String[] strTypesOfLastPeriod;
    private LocalDate startSelectedDate;
    private LocalDate endSelectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        binding();
        setDateListeners();
        setButtonListeners();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

    private void binding() {
        spnrFaculty = findViewById(R.id.spnrFaculty);
        spnrGroup = findViewById(R.id.spnrGroup);
        spnrTypeOfLastPeriod = findViewById(R.id.spnrTypeOfLastPeriod);
        radioBtnPeriod = findViewById(R.id.radioBtnPeriod);
        date = Calendar.getInstance();
        startDate = findViewById(R.id.edtStartDate);
        endDate = findViewById(R.id.edtEndDate);
        numberOfLast = findViewById(R.id.edtNumberOfLast);
        showAvgIndicat = findViewById(R.id.btnShowAvgIndicat);
        showBestStud = findViewById(R.id.btnShowBestStud);
        showUnderperfStud = findViewById(R.id.btnShowUnderperfStud);
        showComparByGroups = findViewById(R.id.btnShowComparByGroups);
        showComparByFac = findViewById(R.id.btnShowComparByFac);
        //showComparByGroups2 = findViewById(R.id.btnShowComparByGroups2);

        db = new DbHelper(getApplicationContext()).getReadableDatabase();


        SpinnerSetterResult<Faculty> spnrFacultyResult = SpinnerSetter.setFaculties(getApplicationContext());
        listFaculties = spnrFacultyResult.getList();
        ArrayAdapter<String> facultyAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spnrFacultyResult.getStr());
        spnrFaculty.setAdapter(facultyAdapter);


        SpinnerSetterResult<Group> spnrGroupResult = SpinnerSetter.setGroups(getApplicationContext());
        listGroups = spnrGroupResult.getList();
        ArrayAdapter<String> groupAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spnrGroupResult.getStr());
        spnrGroup.setAdapter(groupAdapter);


        strTypesOfLastPeriod = new String[] { "????????", "??????????", "??????"};
        ArrayAdapter<String> typeOfLastPeriodAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, strTypesOfLastPeriod);
        spnrTypeOfLastPeriod.setAdapter(typeOfLastPeriodAdapter);
    }

    private void setDateListeners() {
        startDate.setInputType(InputType.TYPE_NULL);
        endDate.setInputType(InputType.TYPE_NULL);

        DatePickerDialog.OnDateSetListener startD = (view, year, monthOfYear, dayOfMonth) -> {
            startDate.setText(DateTimeHelper.getGeneralDateFormat(year, monthOfYear + 1, dayOfMonth));
        };
        DatePickerDialog.OnDateSetListener endD = (view, year, monthOfYear, dayOfMonth) -> {
            endDate.setText(DateTimeHelper.getGeneralDateFormat(year, monthOfYear + 1, dayOfMonth));
        };

        DatePickerDialog startDateDialog = new DatePickerDialog(this, startD,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH));
        DatePickerDialog endDateDialog = new DatePickerDialog(this, endD,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH));


        startDate.setOnClickListener(view -> {
            SystemHelper.hideKeyboard(this);
            startDateDialog.show();
        });
        startDate.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus) {
                SystemHelper.hideKeyboard(this);
                startDateDialog.show();
            }
        });

        endDate.setOnClickListener(view -> {
            SystemHelper.hideKeyboard(this);
            endDateDialog.show();
        });
        endDate.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus) {
                SystemHelper.hideKeyboard(this);
                endDateDialog.show();
            }
        });
    }

    private void setButtonListeners() {
        showAvgIndicat.setOnClickListener(view -> {
            if(!getDateByRadioBtn())
                return;

            Intent intent = new Intent(this, ShowData.class);
            Cursor cursor =
                    ProgressDb.getAvgMarkByStudents(db,
                            listGroups.get(spnrGroup.getSelectedItemPosition()).getId(),
                            startSelectedDate.toString(),
                            endSelectedDate.toString());

            if(cursor.getCount() == 0) {
                intent.putExtra("strData", new String[] {});
                startActivity(intent);
            }

            String[] strData = new String[cursor.getCount()];
            int i = 0;

            while(cursor.moveToNext())
                strData[i++] = "??????: " + cursor.getString(0) + "\n\t\t" + "????. ????????: " + cursor.getString(1);

            intent.putExtra("strData", strData);
            startActivity(intent);
        });

        showBestStud.setOnClickListener(view -> {
            if(!getDateByRadioBtn())
                return;

            Intent intent = new Intent(this, ShowData.class);
            Cursor cursor =
                    ProgressDb.getBestStudByFaculty(db,
                            listFaculties.get(spnrFaculty.getSelectedItemPosition()).getId(),
                            startSelectedDate.toString(),
                            endSelectedDate.toString());

            if(cursor.getCount() == 0) {
                intent.putExtra("strData", new String[] {});
                startActivity(intent);
            }

            String[] strData = new String[cursor.getCount()];
            int i = 0;

            while(cursor.moveToNext())
               strData[i++] =
                       "??????: " + cursor.getString(0) + "\n\t\t" + "????. ????????: " + cursor.getString(1);

            intent.putExtra("strData", strData);
            startActivity(intent);
        });

        showUnderperfStud.setOnClickListener(view -> {
            if(!getDateByRadioBtn())
                return;

            Intent intent = new Intent(this, ShowData.class);
            Cursor cursor =
                    ProgressDb.getUnderperfStudByFaculty(db,
                            listFaculties.get(spnrFaculty.getSelectedItemPosition()).getId(),
                            startSelectedDate.toString(),
                            endSelectedDate.toString());

            if(cursor.getCount() == 0) {
                intent.putExtra("strData", new String[] {});
                startActivity(intent);
            }

            String[] strData = new String[cursor.getCount()];
            int i = 0;

            while(cursor.moveToNext())
                strData[i++] =
                        "??????: " + cursor.getString(0) + "\n\t\t" + "??????-???? ??????????. ????????????: " + cursor.getString(1);

            intent.putExtra("strData", strData);
            startActivity(intent);
        });

        showComparByGroups.setOnClickListener(view -> {
            if(!getDateByRadioBtn())
                return;

            Intent intent = new Intent(this, ShowData.class);
            Cursor cursor = ProgressDb.getComparByGroups(db, startSelectedDate.toString(), endSelectedDate.toString());
            Cursor cursor2 = ProgressDb.getAvgMarkByGroup(db, startSelectedDate.toString(), endSelectedDate.toString());

            if(cursor.getCount() == 0) {
                intent.putExtra("strData", new String[] {});
                startActivity(intent);
            }

            String[] strData = new String[cursor.getCount() + cursor2.getCount() + 1];
            int i = 0;

            while(cursor.moveToNext())
                strData[i++] =
                        "????????????: " + cursor.getString(0) + "-" + cursor.getString(1)
                        + "\n\t\t??????????????: " + cursor.getString(2)
                        + "\n\t\t????. ????????: " + cursor.getString(3);
            strData[i++] = "\n\n\n";
            while (cursor2.moveToNext())
                strData[i++] =
                        "????????????: " + cursor2.getString(0) + "-" + cursor2.getString(1)
                                + "\n\t\t ????. ????????: " + cursor2.getString(2);

            intent.putExtra("strData", strData);
            startActivity(intent);
        });

        showComparByFac.setOnClickListener(view -> {
            if(!getDateByRadioBtn())
                return;

            Intent intent = new Intent(this, ShowData.class);
            Cursor cursor = ProgressDb.getComparByFaculty(db, startSelectedDate.toString(), endSelectedDate.toString());

            if(cursor.getCount() == 0) {
                intent.putExtra("strData", new String[] {});
                startActivity(intent);
            }

            String[] strData = new String[cursor.getCount()];
            int i = 0;

            while(cursor.moveToNext())
                strData[i++] = "??????????????????: " + cursor.getString(0) + "\n\t\t" + "????. ????????: " + cursor.getString(1);

            intent.putExtra("strData", strData);
            startActivity(intent);
        });

        /*showComparByGroups2.setOnClickListener(view -> {
            //TODO: table output "compare by groups"
        });*/
    }

    private boolean getDateByRadioBtn() {
        try {
            if (radioBtnPeriod.isChecked()) {
                startSelectedDate = LocalDate.of(
                        Integer.parseInt(this.startDate.getText().toString().substring(6, 10)),
                        Integer.parseInt(this.startDate.getText().toString().substring(3, 5)),
                        Integer.parseInt(this.startDate.getText().toString().substring(0, 2)));
                endSelectedDate = LocalDate.of(
                        Integer.parseInt(this.endDate.getText().toString().substring(6, 10)),
                        Integer.parseInt(this.endDate.getText().toString().substring(3, 5)),
                        Integer.parseInt(this.endDate.getText().toString().substring(0, 2)));
            } else {
                endSelectedDate = LocalDate.now();
                long minuser = Long.valueOf(numberOfLast.getText().toString());

                if(spnrTypeOfLastPeriod.getSelectedItem().toString().equals("????????"))
                    startSelectedDate = LocalDate.now().minusDays(minuser);
                if(spnrTypeOfLastPeriod.getSelectedItem().toString().equals("??????????"))
                    startSelectedDate = LocalDate.now().minusMonths(minuser);
                if(spnrTypeOfLastPeriod.getSelectedItem().toString().equals("??????"))
                    startSelectedDate = LocalDate.now().minusYears(minuser);
            }
            return true;
        } catch (Exception e) {
            Toast.makeText(this, "?????????????? ????????", Toast.LENGTH_SHORT).show();
            Log.d("Error", e.getMessage());
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}