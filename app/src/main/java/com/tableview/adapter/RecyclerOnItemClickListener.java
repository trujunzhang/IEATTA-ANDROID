package com.tableview.adapter;

import android.view.View;

public interface RecyclerOnItemClickListener {
    void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick);
}
