package com.tableview.storage.models;

public class CellType {
    public int layoutResId;
    public Class cellClass;

    public CellType(Class cellClass, int layoutResId) {
        this.layoutResId = layoutResId;
        this.cellClass = cellClass;
    }
}
