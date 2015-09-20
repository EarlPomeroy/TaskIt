package com.lajollasoftware.taskit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {
    private static final String TAG = "TaskListActivity";
    private static final int EDIT_TASK = 10;
    private static final int CREATE_TASK = 20;

    private ListView taskList;
    private TaskAdapter taskAdapter;

    private ArrayList<Task> tasks;
    private int lastPositionClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        tasks = new ArrayList<>();
        tasks.add(new Task());
        tasks.get(0).setName("task 1");
        tasks.add(new Task());
        tasks.get(1).setName("task 2");
        tasks.add(new Task());
        tasks.get(2).setDueDate(new Date());
        tasks.get(2).setName("task 3");
        tasks.add(new Task());
        tasks.get(3).setName("task 4");
        tasks.get(3).setDone(true);

        taskList = (ListView) findViewById(R.id.task_list);
        this.taskAdapter = new TaskAdapter(tasks);
        taskList.setAdapter(this.taskAdapter);
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastPositionClicked = position;

                Intent taskItem = new Intent(TaskListActivity.this, TaskActivity.class);
                Task task = (Task) parent.getAdapter().getItem(position);
                taskItem.putExtra(getString(R.string.task_item), task);
                startActivityForResult(taskItem, EDIT_TASK);
            }
        });

        taskList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        taskList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                Log.d(TAG, "Position is " + position + " checked is " + checked);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_task_item_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                int id = item.getItemId();
                SparseBooleanArray positions = taskList.getCheckedItemPositions();

                if (id == R.id.delete_task) {
                    for (int i = positions.size() - 1; i >= 0; i--) {
                        if (positions.valueAt(i)) {
                            tasks.remove(positions.keyAt(i));
                        }
                    }

                    taskAdapter.notifyDataSetChanged();
                    mode.finish();

                    return true;
                }

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case EDIT_TASK:
                if (resultCode == RESULT_OK) {
                    Task task = (Task) data.getSerializableExtra(getResources().getString(R.string.task_item));
                    Log.d(TAG, task.getName());

                    this.tasks.set(lastPositionClicked, task);
                    this.taskAdapter.notifyDataSetChanged();
                }
                break;
            case CREATE_TASK:
                if (resultCode == RESULT_OK) {
                    Task task = (Task) data.getSerializableExtra(getResources().getString(R.string.task_item));
                    Log.d(TAG, task.getName());

                    this.tasks.add(task);
                    this.taskAdapter.notifyDataSetChanged();
                }
                break;
            default:
                Log.d(TAG, "Unknown request");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_task) {
            Intent taskItem = new Intent(TaskListActivity.this, TaskActivity.class);
            startActivityForResult(taskItem, CREATE_TASK);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_task_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.delete_task) {
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            tasks.remove(menuInfo.position);
            this.taskAdapter.notifyDataSetChanged();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private class TaskAdapter extends ArrayAdapter<Task> {
        public TaskAdapter(List<Task> tasks) {
            super(TaskListActivity.this, R.layout.task_list_row, R.id.task_item_name, tasks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);

            Task task = getItem(position);

            TextView taskName = (TextView) convertView.findViewById(R.id.task_item_name);
            taskName.setText(task.getName());

            CheckBox taskDone = (CheckBox) convertView.findViewById(R.id.task_item_done);
            taskDone.setChecked(task.isDone());

            return convertView;
        }
    }
}
