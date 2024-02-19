package com.example.mainactivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.example.mainactivity.TodoItemAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.AdapterView;

import android.util.Log;

public class MainActivity extends AppCompatActivity {

    TodoDbHelper dbHelper;
    SQLiteDatabase db;

    private List<TodoItem> todoItemList;
    private ArrayAdapter<TodoItem> adapter;
    private EditText editTextTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new TodoDbHelper(this);
        db = dbHelper.getWritableDatabase();

        // Print cursor information for debugging purposes
        Cursor cursor = db.query(TodoDbHelper.TABLE_NAME, null, null, null, null, null, null);
        printCursor(cursor);
        cursor.close();

        todoItemList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoItemList);

        ListView listView = findViewById(R.id.list_view_1);
        listView.setAdapter(adapter);

        editTextTask = findViewById(R.id.editText);
        Button addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });

        loadTasksFromDatabase();
    }

    private void loadTasksFromDatabase() {
        // Clear the current list
        todoItemList.clear();

        // Query the database for all tasks
        Cursor cursor = db.query(TodoDbHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String text = cursor.getString(cursor.getColumnIndex(TodoDbHelper.COLUMN_TEXT));
                @SuppressLint("Range") int isUrgent = cursor.getInt(((Cursor) cursor).getColumnIndex(TodoDbHelper.COLUMN_URGENT));
                boolean urgent = isUrgent == 1;
                todoItemList.add(new TodoItem(text, urgent));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
    }


    private void addTask() {
        String taskText = editTextTask.getText().toString().trim();
        if (!taskText.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(TodoDbHelper.COLUMN_TEXT, taskText);
            values.put(TodoDbHelper.COLUMN_URGENT, 0); // Assuming new tasks are not urgent by default

            // Insert the new task into the database
            db.insert(TodoDbHelper.TABLE_NAME, null, values);

            // Reload tasks from the database
            loadTasksFromDatabase();

            editTextTask.getText().clear();
        }
    }


    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to delete this?");
        builder.setMessage("The selected row is: " + position);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Remove the task from the database
                TodoItem item = todoItemList.get(position);
                db.delete(TodoDbHelper.TABLE_NAME, TodoDbHelper.COLUMN_TEXT + " = ?", new String[]{item.getText()});

                // Reload tasks from the database
                loadTasksFromDatabase();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    public static class TodoItem {
        private final String text;
        private final boolean isUrgent;

        public TodoItem(String text, boolean isUrgent) {
            this.text = text;
            this.isUrgent = isUrgent;
        }

        public String getText() {
            return text;
        }

        public boolean isUrgent() {
            return isUrgent;
        }

        @NonNull
        @Override
        public String toString() {
            return text;
        }
    }

    private void printCursor(Cursor cursor) {
        if (cursor == null) {
            Log.d("CursorInfo", "Cursor is null");
            return;
        }

        // a. Print the database version number
        int dbVersion = db.getVersion();
        Log.d("CursorInfo", "Database Version: " + dbVersion);

        // b. Print the number of columns in the cursor
        int columnCount = cursor.getColumnCount();
        Log.d("CursorInfo", "Number of Columns: " + columnCount);

        // c. Print the names of the columns in the cursor
        String[] columnNames = cursor.getColumnNames();
        Log.d("CursorInfo", "Column Names: " + arrayToString(columnNames));

        // d. Print the number of results in the cursor
        long rowCount = cursor.getCount();
        Log.d("CursorInfo", "Number of Results: " + rowCount);

        // e. Print each row of results in the cursor
        if (cursor.moveToFirst()) {
            do {
                StringBuilder rowInfo = new StringBuilder("Row: ");
                for (int i = 0; i < columnCount; i++) {
                    rowInfo.append(cursor.getString(i)).append(" | ");
                }
                Log.d("CursorInfo", rowInfo.toString());
            } while (cursor.moveToNext());
        }
    }

    // Utility method to convert array to string for logging
    private String arrayToString(String[] array) {
        StringBuilder sb = new StringBuilder();
        for (String s : array) {
            sb.append(s).append(", ");
        }
        return sb.toString();
    }

}
