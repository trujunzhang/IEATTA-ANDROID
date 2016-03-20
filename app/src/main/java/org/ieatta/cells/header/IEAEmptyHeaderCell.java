package org.ieatta.cells.header;

import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.enums.ViewHolderType;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
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

    private TextView titleLabel;

    public IEAEmptyHeaderCell(View itemView) {
        super(itemView);
    }

    @Override
    public void render(Object value) {
    }
}