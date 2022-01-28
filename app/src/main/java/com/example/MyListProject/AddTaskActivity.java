 package com.example.MyListProject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

 public class AddTaskActivity extends AppCompatActivity {
    DBHelper dbHelper;
    EditText editText;
    Button button;
    String taskName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        editText = findViewById(R.id.taskName);
        button = findViewById(R.id.btnSave);

        dbHelper = new DBHelper(this);
    }

     public void insertTask(View view){
        taskName = editText.getText().toString();
        Task task = new Task();
        task.setTask(taskName);
        task.setStatus(0);
        dbHelper.addTask(task);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
     }


 }