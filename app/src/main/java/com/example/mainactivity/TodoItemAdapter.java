package com.example.mainactivity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TodoItemAdapter extends BaseAdapter {

    private final List<MainActivity.TodoItem> todoItemList;

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    private final LayoutInflater inflater;

    public TodoItemAdapter(Context context, List<MainActivity.TodoItem> todoItemList) {
        this.todoItemList = todoItemList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return todoItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return todoItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_todo, parent, false);
        }

        MainActivity.TodoItem todoItem = todoItemList.get(position);

        TextView textViewTodo = view.findViewById(R.id.textView);

        textViewTodo.setText(todoItem.getText());

        if (todoItem.isUrgent()) {
            view.setBackgroundColor(Color.RED);
            textViewTodo.setTextColor(Color.WHITE);
        } else {
            // Reset the background color and text color for non-urgent items
            view.setBackgroundColor(Color.TRANSPARENT);
            textViewTodo.setTextColor(Color.BLACK);
        }



        return view;
    }

}
