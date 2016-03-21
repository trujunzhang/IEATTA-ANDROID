package org.ieatta.cells.headerfooterview;

import android.view.View;
import android.widget.LinearLayout;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.enums.ViewHolderType;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.analytics.RecycleCellFunnel;
import org.ieatta.cells.model.IEAHeaderView;

public class IEAFooterView extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAFooterView.class, R.layout.cell_headerview);
    }

    @Override
    protected boolean shouldOnClickItem() {
        return false;
    }

    @Override
    public boolean haveBackground(){
        return false;
    }

    @Override
    public ViewHolderType getViewHolderType() {
        return ViewHolderType.header;
    }

    public IEAFooterView(View itemView) {
        super(itemView);
    }

    @Override
    public void render(Object value) {
    }
}