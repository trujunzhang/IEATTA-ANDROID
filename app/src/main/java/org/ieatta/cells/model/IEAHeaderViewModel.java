package org.ieatta.cells.model;

import android.view.Display;
import android.widget.LinearLayout;

public class IEAHeaderViewModel {
    private int cellHeight;

    public IEAHeaderViewModel(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }
}
