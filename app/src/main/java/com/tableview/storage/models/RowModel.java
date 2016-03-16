package com.tableview.storage.models;

import com.tableview.adapter.NSIndexPath;

public class RowModel {
    public CellType cellType;
    public Object model;

    public NSIndexPath indexPath;

    public RowModel(HeaderModel headerModel) {
        this.model = headerModel.item;
        this.cellType = headerModel.cellType;
    }

    public RowModel(Object model, CellType type, NSIndexPath indexPath) {
        this.model = model;
        this.cellType = type;
        this.indexPath = indexPath;
    }

    public RowModel(FooterModel footerModel) {
        this.model = footerModel.item;
        this.cellType = footerModel.cellType;
    }
}
