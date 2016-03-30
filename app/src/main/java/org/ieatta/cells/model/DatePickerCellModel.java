package org.ieatta.cells.model;

import org.ieatta.provide.IEAEditKey;

import java.util.Date;

public class DatePickerCellModel extends EditBaseCellModel {
    public int dateTitleResId;
    public Date date = new Date();

    public DatePickerCellModel(IEAEditKey editKey, Date date, int dateTitleResId) {
        super(editKey);

        this.date = date;
        this.dateTitleResId = dateTitleResId;
    }
}


