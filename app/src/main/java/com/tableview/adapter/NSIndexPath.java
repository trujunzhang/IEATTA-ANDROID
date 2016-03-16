package com.tableview.adapter;

public class NSIndexPath {
    public int row;
    public int section;

    public NSIndexPath(int section, int row) {
        this.section = section;
        this.row = row;
    }

    @Override
    public String toString() {
        return "NSIndexPath, section: " + section + " row: " + row;
    }
}
