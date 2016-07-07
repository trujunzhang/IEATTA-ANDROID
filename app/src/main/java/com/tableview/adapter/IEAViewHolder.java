package com.tableview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tableview.TableViewControllerAdapter;
import com.tableview.adapter.enums.ViewHolderType;

import org.ieatta.R;

public abstract class IEAViewHolder extends RecyclerView.ViewHolder implements ModelTransfer, View.OnClickListener {


    protected boolean shouldOnClickItem() {
        return true;
    }

    public boolean haveBackground() {
        return true;
    }

    /**
     * If the Cell have background,
     * Must set root view's id to "android:id="@+id/container"
     */
    public ViewGroup mContainer;

    private TableViewControllerAdapter mAdapter;

    @Override
    public ViewHolderType getViewHolderType() {
        return ViewHolderType.cell;
    }

    public IEAViewHolder(View itemView) {
        super(itemView);

        if (this.haveBackground())
            this.mContainer = (ViewGroup) itemView.findViewById(R.id.container);

        itemView.setTag(this.getViewHolderType().ordinal());

        if (this.shouldOnClickItem())
            itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mAdapter != null)
            mAdapter.onItemHolderClick(this);
    }

    public void setAdapter(TableViewControllerAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }
}
