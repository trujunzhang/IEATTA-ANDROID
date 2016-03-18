package org.ieatta.cells.model;

import org.ieatta.provide.IEAEditKey;

public class SectionTitleCellModel extends EditBaseCellModel {
    public int titleResId;

    public SectionTitleCellModel(IEAEditKey editKey, int titleResId) {
        super(editKey);
        this.titleResId = titleResId;
    }
}
