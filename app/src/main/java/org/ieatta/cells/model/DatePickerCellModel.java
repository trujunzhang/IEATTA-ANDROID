package org.ieatta.cells.model;

import android.support.v4.app.FragmentManager;

import org.ieatta.provide.IEAEditKey;

import java.util.Date;

public class DatePickerCellModel extends EditBaseCellModel {
    public int dateTitleResId;
    public FragmentManager fm;
    public Date date = new Date();

    public DatePickerCellModel(IEAEditKey editKey, Date date, int dateTitleResId, FragmentManager fm) {
        super(editKey);

        this.date = date;
        this.dateTitleResId = dateTitleResId;
        this.fm = fm;
    }
}


