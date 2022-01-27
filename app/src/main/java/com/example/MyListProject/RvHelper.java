package com.example.huaweiproject;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RvHelper extends ItemTouchHelper.SimpleCallback {

    protected ListAdapater adapater;

    public RvHelper(ListAdapater adapater) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapater = adapater;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adapater.getContext());
            builder.setTitle("Remove Task");
            builder.setMessage("Are you want to remove task?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapater.delTask(position);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapater.notifyItemChanged(position);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            adapater.editItem(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeRightBackgroundColor(ContextCompat.getColor(adapater.getContext(), R.color.design_default_color_secondary_variant))
                .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                .addSwipeLeftBackgroundColor(Color.RED)
                .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_forever_24)
                .create()
                .decorate();



        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}