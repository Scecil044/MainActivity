package com.example.mainactivity;

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


public class MainActivity extends AppCompatActivity {

    private List<TodoItem> todoItemList;
    private ArrayAdapter<TodoItem> adapter;
    private EditText editTextTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoItemList = new ArrayList<>();

        todoItemList.add(new TodoItem("Finish Lab 4", true));
        todoItemList.add(new TodoItem("Cook dinner", false));
        todoItemList.add(new TodoItem("Brush Teeth", true));
        todoItemList.add(new TodoItem("Do laundry", true));

        ListView listView = findViewById(R.id.list_view_1);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoItemList);
        listView.setAdapter(adapter);
//        listView.setAdapter(new TodoItemAdapter(this, todoItemList));

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
    }


    private void addTask() {
        String taskText = editTextTask.getText().toString().trim();
        if (!taskText.isEmpty()) {
            todoItemList.add(new TodoItem(taskText, false));
            editTextTask.getText().clear();
            adapter.notifyDataSetChanged();

        }
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to delete this?");
        builder.setMessage("The selected row is: " + position);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                todoItemList.remove(position);
                adapter.notifyDataSetChanged();
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

}
