package com.example.MyListProject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogListener {

    Switch SC;

    protected RecyclerView rv;
    protected FloatingActionButton buttonAdd;
    protected DBHelper dbHelper;
    protected List<Task> taskList;
    protected ListAdapter adapter;

//    public MainActivity(){
//        if(BuildConfig.DEBUG){
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectLeakedClosableObjects()
//                    .penaltyLog()
//                    .build());
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.myTask);
        buttonAdd = findViewById(R.id.addNewTask);
        dbHelper = new DBHelper(MainActivity.this);
        taskList = new ArrayList<>();
        adapter = new ListAdapter(dbHelper, MainActivity.this);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        taskList = dbHelper.AllListTasks();
        Collections.reverse(taskList);
        adapter.setTaskList(taskList);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RvHelper(adapter));
        itemTouchHelper.attachToRecyclerView(rv);

        SC = findViewById(R.id.darkmodeSwitch);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            SC.setChecked(true);
        }
        SC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });

    }

    @Override
    public void onDialogClosed(DialogInterface dialogInterface) {
        taskList = dbHelper.AllListTasks();
        Collections.reverse(taskList);
        adapter.setTaskList(taskList);
        adapter.notifyDataSetChanged();
    }

    public void newTaskPage(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }
}