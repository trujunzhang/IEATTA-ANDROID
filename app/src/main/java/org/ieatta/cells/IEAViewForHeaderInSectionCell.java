package org.ieatta.cells;

import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.enums.ViewHolderType;
import com.tableview.storage.models.CellType;

import org.ieatta.R;

public class IEAViewForHeaderInSectionCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAViewForHeaderInSectionCell.class, R.layout.view_for_header_in_section_cell);
    }

    @Override
    protected boolean shouldClickItem() {
        return false;
    }

    @Override
    public ViewHolderType getViewHolderType() {
        return ViewHolderType.header;
    }

    private IEAViewForHeaderInSectionCell self = this;

    private TextView titleLabel;

    public IEAViewForHeaderInSectionCell(View itemView) {
        super(itemView);

        self.titleLabel = (TextView) itemView.findViewById(R.id.headerTextView);
    }

    @Override
    public void render(Object value) {
        SectionTitleCellModel more = (SectionTitleCellModel) value;
        self.titleLabel.setText(more.titleResId);
    }
}