package com.tableview.storage.models;

public class FooterModel {

    /// Items for current section
    /// - Warning: If you try to set new array to this property [T], the only way to do this without exception is to wrap it into items.map { $0 }. This is a workaround that exists because of Swift inability to cast [T] to [Any]. You can call `setItems` method instead of doing so.
    /// - SeeAlso: `setItems:`
    public Object item;
    public CellType cellType;

    public FooterModel(Object item) {
        this.item = item;
    }

    public FooterModel(Object item, CellType type) {
        this.item = item;
        this.cellType = type;
    }
}
