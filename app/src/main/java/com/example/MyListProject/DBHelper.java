package com.example.MyListProject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    protected SQLiteDatabase db;
    protected static final String DB_Name = "MyList_Database";
    protected static final String Table = "MyList_Table";
    protected static final String c1 = "id";
    protected static final String c2 = "task";
    protected static final String c3 = "status";

    public DBHelper(@Nullable Context context) {
        super(context, DB_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Table + "(id INTEGER PRIMARY KEY AUTOINCREMENT, task TEXT, status INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Table);
        onCreate(db);
    }

    public void addTask(Task task){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(c2, task.getTask());
        cv.put(c3, 0);
        db.insert(Table, null, cv);
    }

    public void updateTask(int Id, String task){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(c2, task);
        db.update(Table, cv, "ID=?", new String[]{String.valueOf(Id)});
    }

    public void updateStatus(int Id, int Status){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(c3, Status);
        db.update(Table, cv, "ID=?", new String[]{String.valueOf(Id)});
    }

    public void deleteTask(int Id){
        db = this.getWritableDatabase();
        db.delete(Table, "ID=?", new String[]{String.valueOf(Id)});
    }

    @SuppressLint("Range")
    public List<Task> AllListTasks(){
        db = this.getWritableDatabase();
        Cursor c = null;
        List<Task> taskList = new ArrayList<>();

        db.beginTransaction();
        try {
            c = db.query(Table, null, null, null, null, null, null);
            if(c != null){
                if(c.moveToFirst()){
                    do{
                        Task task = new Task();
                        task.setId(c.getInt(c.getColumnIndex(c1)));
                        task.setTask(c.getString(c.getColumnIndex(c2)));
                        task.setStatus(c.getInt(c.getColumnIndex(c3)));
                        taskList.add(task);
                    }while (c.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            c.close();
        }
        return taskList;
    }
}
