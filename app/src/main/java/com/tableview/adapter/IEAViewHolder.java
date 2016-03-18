package com.tableview.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.tableview.adapter.enums.ViewHolderType;

import org.ieatta.R;

public abstract class IEAViewHolder extends AbstractDraggableItemViewHolder implements ModelTransfer, View.OnClickListener, View.OnLongClickListener {
    protected boolean shouldOnClickItem() {
        return true;
    }
    public ViewGroup mContainer;

//    android:id="@+id/container"

    @Override
    public ViewHolderType getViewHolderType() {
        return ViewHolderType.cell;
    }

    private ItemClickListener clickListener;

    public IEAViewHolder(View itemView) {
        super(itemView);

        this.mContainer = (ViewGroup) itemView.findViewById(R.id.container);
        itemView.setTag(this.getViewHolderType().ordinal());
        if (this.shouldOnClickItem() == true) {
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        clickListener.onClick(view, getPosition(), false);
    }

    @Override
    public boolean onLongClick(View view) {
        clickListener.onClick(view, getPosition(), true);
        return true;
    }
}
