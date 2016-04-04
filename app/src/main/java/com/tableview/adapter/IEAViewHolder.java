package com.tableview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.tableview.adapter.enums.ViewHolderType;

import org.ieatta.R;

public abstract class IEAViewHolder extends RecyclerView.ViewHolder implements ModelTransfer, View.OnClickListener, View.OnLongClickListener {
    protected boolean shouldOnClickItem() {
        return true;
    }

    public boolean haveBackground(){
        return true;
    }

    /**
     * If the Cell have background,
     * Must set root view's id to "android:id="@+id/container"
     */
    public ViewGroup mContainer;

    @Override
    public ViewHolderType getViewHolderType() {
        return ViewHolderType.cell;
    }

    private ItemClickListener clickListener;

    public IEAViewHolder(View itemView) {
        super(itemView);

        if(haveBackground() == true) {
            this.mContainer = (ViewGroup) itemView.findViewById(R.id.container);
        }
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
