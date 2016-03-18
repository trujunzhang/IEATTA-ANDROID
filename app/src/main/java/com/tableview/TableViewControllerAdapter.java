package com.tableview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.ItemClickListener;
import com.tableview.storage.DTTableViewManager;
import com.tableview.storage.models.RowModel;

public class TableViewControllerAdapter
        extends RecyclerView.Adapter<IEAViewHolder>
        implements DraggableItemAdapter<IEAViewHolder> {
    private DTTableViewManager mProvider;

    // NOTE: Make accessible with short name
    private interface Draggable extends DraggableItemConstants {
    }

    public TableViewControllerAdapter(DTTableViewManager mProvider) {
        this.mProvider = mProvider;
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
    public int getItemCount() {
        int itemCount = this.mProvider.getItemCount();
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = this.mProvider.memoryStorage.getItemViewType(position);
        return itemViewType;
    }


    @Override
    public void onMoveItem(int fromPosition, int toPosition) {

        if (fromPosition == toPosition) {
            return;
        }

        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public boolean onCheckCanStartDrag(IEAViewHolder holder, int position, int x, int y) {
        return false;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(IEAViewHolder holder, int position) {
        // no drag-sortable range specified
        return null;
    }
}
