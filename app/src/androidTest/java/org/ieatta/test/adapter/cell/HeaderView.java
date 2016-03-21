package org.ieatta.test.adapter.cell;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tableview.storage.models.CellType;

public class HeaderView extends RecyclerView.ViewHolder{
    public static CellType getType() {
        return new CellType(HeaderView.class, 234);
    }

    public HeaderView(View itemView) {
        super(itemView);
    }

}
