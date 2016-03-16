package com.tableview.adapter;

import android.view.View;

public interface RecyclerItemClickListener {
    void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick);
}
