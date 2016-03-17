package com.tableview.storage.models;

public class CellType {
    public int layoutResId;
    public Class cellClazz;

    public CellType(Class cellClazz, int layoutResId) {
        this.layoutResId = layoutResId;
        this.cellClazz = cellClazz;
    }
}
