package org.ieatta.cells.header;

import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.enums.ViewHolderType;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.cells.model.SectionTitleCellModel;

public class IEAViewForHeaderInSectionCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAViewForHeaderInSectionCell.class, R.layout.list_section_header);
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

    private IEAViewForHeaderInSectionCell self = this;

    private TextView titleLabel;

    public IEAViewForHeaderInSectionCell(View itemView) {
        super(itemView);

        self.titleLabel = (TextView) itemView.findViewById(android.R.id.text1);
    }

    @Override
    public void render(Object value) {
        SectionTitleCellModel more = (SectionTitleCellModel) value;
        self.titleLabel.setText(more.titleResId);
    }
}