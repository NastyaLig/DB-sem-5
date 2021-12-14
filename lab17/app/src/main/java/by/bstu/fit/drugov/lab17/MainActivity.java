package by.bstu.fit.drugov.lab17;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    DBHelper dbHelper = new DBHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        dbHelper.execute("");

        Button select  = findViewById(R.id.btnSelect);
        TextView selected = findViewById(R.id.rsltSelect);
        Button prepSelect = findViewById(R.id.btnPreparedSelect);
        TextView prepSelected = findViewById(R.id.rsltPreparedSelect);

        Button addBatch = findViewById(R.id.btnAddBatch);
        Button clearBatch = findViewById(R.id.btnCancelBatch);

        Button update = findViewById(R.id.btnUpdateInfo);
        Button storedProcedure = findViewById(R.id.btnStoredProcedure);
        Button storedFunction = findViewById(R.id.btnFunction);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected.setText(dbHelper.selectFromTable("select * from table1"));
            }
        });

        prepSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepSelected.setText(dbHelper.PrepareSelect(1));
            }
        });

        addBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.InsertBatch(0,null,null);
            }
        });

        clearBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.CancelBatch();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.updateInfo();
            }
        });

        storedProcedure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected.setText(dbHelper.ExecuteStoredProcedure());
            }
        });

        storedFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Кол-во строк в таблице: "+String.valueOf(dbHelper.ExecuteStoredFunction()),Toast.LENGTH_LONG).show();
            }
        });
    }
}