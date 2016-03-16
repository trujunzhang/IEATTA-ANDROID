package com.tableview.storage.models;

import com.tableview.adapter.NSIndexPath;

public class RowModel {
    private RowModel self = this;

    public CellType cellType;
    public Object model;

    public NSIndexPath indexPath;

    public RowModel(HeaderModel headerModel) {
        self.model = headerModel.item;
        self.cellType = headerModel.cellType;
    }

    public RowModel(Object model, CellType type, NSIndexPath indexPath) {
        this.model = model;
        this.cellType = type;
        this.indexPath = indexPath;
    }

    public RowModel(FooterModel footerModel) {
        self.model = footerModel.item;
        self.cellType = footerModel.cellType;
    }
}
