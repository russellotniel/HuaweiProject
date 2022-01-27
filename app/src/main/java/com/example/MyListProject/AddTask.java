package com.example.huaweiproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddTask extends BottomSheetDialogFragment {

    protected static final String text = "AddTask";
    protected EditText editText;
    protected Button button;
    protected DBHelper dbHelper;

    public static AddTask newInstance(){
        return new AddTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_task, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = view.findViewById(R.id.taskName);
        button = view.findViewById(R.id.saveTask);

        dbHelper = new DBHelper(getActivity());

        boolean update = false;

        Bundle b = getArguments();

        if(b != null){
            update = true;
            String task = b.getString("task");
            editText.setText(task);

            if(task.length() > 0){
                button.setEnabled(false);
            }
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    button.setEnabled(false);
                    button.setBackgroundColor(Color.GRAY);
                }
                else{
                    button.setEnabled(true);
                    button.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        boolean finalUpdate = update;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String t = editText.getText().toString();

                if(finalUpdate){
                    dbHelper.updateTask(b.getInt("id"), t);
                }
                else{
                    Task task = new Task();
                    task.setTask(t);
                    task.setStatus(0);
                    dbHelper.addTask(task);
                }

                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();

        if(activity instanceof DialogListener){
            ((DialogListener)activity).onDialogClosed(dialog);
        }
    }
}
