package org.ieatta.test.adapter.cell;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tableview.storage.models.CellType;

public class FooterView extends RecyclerView.ViewHolder{
    public static CellType getType(int layoutResId) {
        return new CellType(FooterView.class, layoutResId);
    }

    public FooterView(View itemView) {
        super(itemView);
    }

}
