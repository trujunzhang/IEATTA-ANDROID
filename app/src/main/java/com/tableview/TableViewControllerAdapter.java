package com.tableview;

import android.support.v4.view.ViewCompat;
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
import com.tableview.utils.DrawableUtils;
import com.tableview.utils.ViewUtils;

import org.ieatta.R;

public class TableViewControllerAdapter
        extends RecyclerView.Adapter<IEAViewHolder>
        implements DraggableItemAdapter<IEAViewHolder> {
    private DTTableViewManager mProvider;

    // NOTE: Make accessible with short name
    private interface Draggable extends DraggableItemConstants {
    }

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

//        if (holder.haveBackground() == true)
//            setBackground(holder);

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                RowModel item = TableViewControllerAdapter.this.mProvider.memoryStorage.getItem(position);
                TableViewControllerAdapter.this.mProvider.getOnItemClickListener().onItemClick(view, item.indexPath, item.model, position, isLongClick);
            }
        });
    }

    private void setBackground(IEAViewHolder holder) {
        holder.mContainer.setBackgroundResource(R.drawable.bg_item_normal_state);

        // set background resource (target view ID: container)
        final int dragState = holder.getDragStateFlags();

        if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;

            if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_dragging_active_state;

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.mContainer.getForeground());
            } else if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.bg_item_dragging_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

            holder.mContainer.setBackgroundResource(bgResId);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.mProvider.getItemCount();
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
        // x, y --- relative from the itemView's top-left
        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mContainer;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }
    @Override
    public ItemDraggableRange onGetItemDraggableRange(IEAViewHolder holder, int position) {
        // no drag-sortable range specified
        return null;
    }
}
