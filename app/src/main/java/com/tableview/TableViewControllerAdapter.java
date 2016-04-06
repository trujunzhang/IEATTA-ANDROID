package com.tableview;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.ItemClickListener;
import com.tableview.storage.DTTableViewManager;
import com.tableview.storage.models.RowModel;
import com.tableview.utils.DrawableUtils;
import com.tableview.utils.ViewUtils;

import org.ieatta.R;
import org.ieatta.analytics.TableViewControllerAdapterFunnel;

public class TableViewControllerAdapter
        extends RecyclerView.Adapter<IEAViewHolder> {
    private DTTableViewManager mProvider;

    public TableViewControllerAdapter(DTTableViewManager mProvider) {
        this.mProvider = mProvider;

        // DraggableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    @Override
    public IEAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return this.mProvider.createViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(final IEAViewHolder holder, int position) {
        Object model = this.mProvider.memoryStorage.getRowModel(position);
        holder.render(model);

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                RowModel item = TableViewControllerAdapter.this.mProvider.memoryStorage.getItem(position);
                TableViewControllerAdapter.this.mProvider.getOnItemClickListener().onItemClick(view, item.indexPath, item.model, position, isLongClick);
            }
        });
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
