package org.ieatta.cells;

public class SectionTitleCellModel extends EditBaseCellModel {
    public int titleResId;

    public SectionTitleCellModel(IEAEditKey editKey, int titleResId) {
        super(editKey);
        this.titleResId = titleResId;
    }

}
