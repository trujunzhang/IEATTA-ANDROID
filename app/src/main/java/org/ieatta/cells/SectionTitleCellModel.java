package org.ieatta.cells;

import org.ieatta.cells.model.EditBaseCellModel;
import org.ieatta.provide.IEAEditKey;

public class SectionTitleCellModel extends EditBaseCellModel {
    public int titleResId;

    public SectionTitleCellModel(IEAEditKey editKey, int titleResId) {
        super(editKey);
        this.titleResId = titleResId;
    }
}
