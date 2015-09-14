package com.lajollasoftware.taskit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskActivity extends AppCompatActivity {
    private static final String TAG = "TaskActivity";

    private Task task;

    private EditText taskName;
    private Button taskDate;
    private CheckBox taskDone;
    private Button taskSubmit;

    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        this.task = (Task) getIntent().getSerializableExtra(getResources().getString(R.string.task_item));

        Log.d(TAG, "Got task " + task);

        cal = Calendar.getInstance();

        taskName = (EditText) findViewById(R.id.task_name);
        taskDate = (Button) findViewById(R.id.task_date);
        taskDone = (CheckBox) findViewById(R.id.task_done);
        taskSubmit = (Button) findViewById(R.id.task_submit);

        taskName.setText(task.getName());

        if (task.getDueDate() == null) {
            cal.setTime(new Date());
        }
        else {
            cal.setTime(task.getDueDate());
        }

        updateDateButton(cal.getTime());

        taskDone.setChecked(task.isDone());

        taskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (task.getDueDate() == null) {
                    cal.setTime(new Date());
                }
                else {
                    cal.setTime(task.getDueDate());
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(TaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        cal.set(year, monthOfYear, dayOfMonth);
                        updateDateButton(cal.getTime());
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        taskSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setName(taskName.getText().toString());
                task.setDueDate(cal.getTime());
                task.setDone(taskDone.isChecked());

                Intent i = new Intent();
                i.putExtra(getResources().getString(R.string.task_item), task);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    private void updateDateButton(Date time) {
        DateFormat df = DateFormat.getDateInstance();
        taskDate.setText(df.format(time));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
