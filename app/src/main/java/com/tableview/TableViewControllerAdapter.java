package com.tableview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.DTTableViewManager;

import org.ieatta.analytics.TableViewControllerAdapterFunnel;

public class TableViewControllerAdapter extends RecyclerView.Adapter<IEAViewHolder> {
    private DTTableViewManager mProvider;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public TableViewControllerAdapter(DTTableViewManager mProvider) {
        this.mProvider = mProvider;

        // DraggableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    public void onItemHolderClick(IEAViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    @Override
    public IEAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IEAViewHolder viewHolder = this.mProvider.createViewHolder(parent, viewType);
        viewHolder.setAdapter(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final IEAViewHolder holder, int position) {
        Object model = this.mProvider.memoryStorage.getRowModel(position);
        holder.render(model);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = this.mProvider.getItemCount();
        new TableViewControllerAdapterFunnel().logItemCount(count);
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return this.mProvider.memoryStorage.getItemViewType(position);
    }

}
