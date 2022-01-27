package com.example.MyListProject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogListener{

    protected RecyclerView rv;
    protected FloatingActionButton buttonAdd;
    protected DBHelper dbHelper;
    protected List<Task> taskList;
    protected ListAdapater adapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.myTask);
        buttonAdd = findViewById(R.id.addNewTask);
        dbHelper = new DBHelper(MainActivity.this);
        taskList = new ArrayList<>();
        adapater = new ListAdapater(dbHelper, MainActivity.this);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapater);

        taskList = dbHelper.AllListTasks();
        Collections.reverse(taskList);
        adapater.setTaskList(taskList);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTask.newInstance().show(getSupportFragmentManager(), AddTask.text);

            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RvHelper(adapater));
        itemTouchHelper.attachToRecyclerView(rv);
    }

    @Override
    public void onDialogClosed(DialogInterface dialogInterface) {
        taskList = dbHelper.AllListTasks();
        Collections.reverse(taskList);
        adapater.setTaskList(taskList);
        adapater.notifyDataSetChanged();
    }
}