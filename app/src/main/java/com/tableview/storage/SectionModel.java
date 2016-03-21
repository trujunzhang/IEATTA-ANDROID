package com.tableview.storage;

import com.tableview.adapter.NSIndexPath;
import com.tableview.storage.models.CellType;
import com.tableview.storage.models.FooterModel;
import com.tableview.storage.models.HeaderModel;
import com.tableview.storage.models.RowModel;
import com.tableview.utils.CollectionUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class SectionModel {
    /// Items for current section
    /// - Warning: If you try to set new array to this property [T], the only way to do this without exception is to wrap it into items.map { $0 }. This is a workaround that exists because of Swift inability to cast [T] to [Any]. You can call `setItems` method instead of doing so.
    /// - SeeAlso: `setItems:`
    public List items = new LinkedList<>();
    private int sectionIndex;
    public CellType cellType;
    public HashMap<Integer, CellType> specialRows = new LinkedHashMap<>();

    private HeaderModel headerModel;
    private FooterModel footerModel;

    public SectionModel() {
        this.sectionIndex = -1;
    }

    public SectionModel(int sectionIndex) {
        this.sectionIndex = sectionIndex;
    }

    /// Set items of specific time to items property.
    /// - Parameter items: items to set
    /// - Note: This method exists because of inability of Swift to cast [T] to [Any].
    public SectionModel setItems(List items) {
        this.items = items;
        return this;
    }

    public SectionModel setItems(Object[] items) {
        this.items = CollectionUtil.createList(items);
        return this;
    }

    public RowModel getRowModel(int row) {
        if (getHeaderModel() != null) {
            if (row == 0) {
                return new RowModel(getHeaderModel());
            }
            row--;
        }
        if (row < this.items.size()) {
            CellType type = this.getRowType(row, this.cellType);
            return new RowModel(this.items.get(row), type, new NSIndexPath(sectionIndex, row));
        } else if (getFooterModel() != null) {
            return new RowModel(getFooterModel());
        }

        return null;
    }

    private CellType getRowType(int row, CellType type) {
        // Step1: If have special row type.
        CellType specialRowType = this.specialRows.get(new Integer(row));
        if (specialRowType != null) {
            return specialRowType;
        }

        return type;
    }

    /// Number of items in current section
    public int numberOfItems() {
        int itemsSize = this.items.size();
        if (getHeaderModel() != null) {
            itemsSize++;
        }
        if (getFooterModel() != null) {
            itemsSize++;
        }

        return itemsSize;
    }

    public SectionModel setHeaderModel(HeaderModel headerModel) {
        this.headerModel = headerModel;
        return this;
    }

    public SectionModel setFooterModel(FooterModel footerModel) {
        this.footerModel = footerModel;
        return this;
    }

    public HeaderModel getHeaderModel() {
        return headerModel;
    }

    public FooterModel getFooterModel() {
        return footerModel;
    }
}
