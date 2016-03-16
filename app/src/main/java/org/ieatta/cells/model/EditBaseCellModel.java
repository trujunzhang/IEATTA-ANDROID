package org.ieatta.cells.model;

public class EditBaseCellModel {
    public IEAEditKey editKey = IEAEditKey.Unknow;
    public String editValue;

    public EditBaseCellModel(IEAEditKey editKey) {
        this.editKey = editKey;
    }

    public EditBaseCellModel(IEAEditKey editKey, String editValue) {
        this.editKey = editKey;
        this.editValue = editValue;
    }
}


