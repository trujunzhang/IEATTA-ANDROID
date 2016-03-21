package org.ieatta.cells.headerfooterview;

import android.view.View;
import android.widget.LinearLayout;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.enums.ViewHolderType;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.analytics.RecycleCellFunnel;
import org.ieatta.cells.model.IEAHeaderViewModel;

public class IEAHeaderView extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAHeaderView.class, R.layout.cell_headerview);
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

    private LinearLayout emptyLinearLayout;

    public IEAHeaderView(View itemView) {
        super(itemView);

        this.emptyLinearLayout = (LinearLayout) itemView.findViewById(R.id.empty_linearLayout);
    }

    @Override
    public void render(Object value) {
        IEAHeaderViewModel header = (IEAHeaderViewModel) value;

        int cellHeight = header.getCellHeight();
        this.emptyLinearLayout.setMinimumHeight(cellHeight);
        new RecycleCellFunnel().logCellInfo("IEAHeaderView","cellHeight: "+cellHeight);
    }
}