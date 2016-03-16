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
    private SectionModel self = this;

    /// Items for current section
    /// - Warning: If you try to set new array to this property [T], the only way to do this without exception is to wrap it into items.map { $0 }. This is a workaround that exists because of Swift inability to cast [T] to [Any]. You can call `setItems` method instead of doing so.
    /// - SeeAlso: `setItems:`
    public List items = new LinkedList<>();
    public int sectionIndex;
    public CellType cellType;
    public HashMap<Integer, CellType> specialRows = new LinkedHashMap<>();

    public HeaderModel headerModel;
    public FooterModel footerModel;

    public SectionModel(int sectionIndex) {
        this.sectionIndex = sectionIndex;
    }

    /// Set items of specific time to items property.
    /// - Parameter items: items to set
    /// - Note: This method exists because of inability of Swift to cast [T] to [Any].
    public SectionModel setItems(List items) {
        this.items = items;
        return self;
    }

    public SectionModel setItems(Object[] items) {
        this.items = CollectionUtil.createList(items);
        return self;
    }

    public RowModel getRowModel(int row) {
        if (headerModel != null) {
            if (row == 0) {
                return new RowModel(headerModel);
            }
            row--;
        }
        if (row < self.items.size()) {
            CellType type = self.getRowType(row, self.cellType);
            return new RowModel(self.items.get(row), type, new NSIndexPath(sectionIndex, row));
        } else if (footerModel != null) {
            return new RowModel(footerModel);
        }

        return null;
    }

    private CellType getRowType(int row, CellType type) {
        // Step1: If have special row type.
        CellType specialRowType = self.specialRows.get(new Integer(row));
        if (specialRowType != null) {
            return specialRowType;
        }

        return type;
    }

    /// Number of items in current section
    public int numberOfItems() {
        int itemsSize = self.items.size();
        if (headerModel != null) {
            itemsSize++;
        }
        if (footerModel != null) {
            itemsSize++;
        }

        return itemsSize;
    }

    public SectionModel setHeaderModel(HeaderModel headerModel) {
        self.headerModel = headerModel;
        return self;
    }

    public SectionModel setFooterModel(FooterModel footerModel) {
        self.footerModel = footerModel;
        return self;
    }
}
