package com.tableview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tableview.adapter.enums.ViewHolderType;

public abstract class IEAViewHolder extends RecyclerView.ViewHolder implements ModelTransfer, View.OnClickListener, View.OnLongClickListener {
    private IEAViewHolder self = this;

    protected boolean shouldClickItem() {
        return true;
    }

    @Override
    public ViewHolderType getViewHolderType() {
        return ViewHolderType.cell;
    }

    private ItemClickListener clickListener;

    public IEAViewHolder(View itemView) {
        super(itemView);

        itemView.setTag(self.getViewHolderType().ordinal());
        if (self.shouldClickItem() == true) {
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
