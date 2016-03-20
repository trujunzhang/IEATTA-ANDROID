package org.ieatta.cells.header;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.enums.ViewHolderType;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.cells.model.IEAEmptyHeader;
import org.ieatta.cells.model.SectionTitleCellModel;

public class IEAEmptyHeaderCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAEmptyHeaderCell.class, R.layout.cell_empty_header);
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

    public IEAEmptyHeaderCell(View itemView) {
        super(itemView);

        this.emptyLinearLayout = (LinearLayout) itemView.findViewById(R.id.empty_linearLayout);
    }

    @Override
    public void render(Object value) {
        IEAEmptyHeader header = (IEAEmptyHeader) value;
        int cellHeight = header.getCellHeight();
        this.emptyLinearLayout.setMinimumHeight(cellHeight);
    }
}