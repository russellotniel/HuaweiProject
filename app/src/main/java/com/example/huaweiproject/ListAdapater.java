package com.example.huaweiproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapater extends RecyclerView.Adapter<ListAdapater.ViewHolder> {

    protected List<Task> taskList;
    protected MainActivity ma;
    protected DBHelper dbHelper;

    public ListAdapater(DBHelper dbHelper, MainActivity ma){
        this.ma = ma;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_list_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Task item = taskList.get(position);
        holder.checkBox.setText(item.getTask());
        holder.checkBox.setChecked(b(item.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    dbHelper.updateStatus(item.getId(), 1);
                }
                else {
                    dbHelper.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    public boolean b(int number){
        return number != 0;
    }

    public Context getContext(){
        return ma;
    }

    public void setTaskList(List<Task> taskList){
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public void delTask(int position){
        Task item = taskList.get(position);
        dbHelper.deleteTask(item.getId());
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        Task item = taskList.get(position);

        Bundle b = new Bundle();
        b.putInt("id", item.getId());
        b.putString("task", item.getTask());

        AddTask addTask = new AddTask();
        addTask.setArguments(b);
        addTask.show(ma.getSupportFragmentManager(), addTask.getTag());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.check);
        }
    }

}
