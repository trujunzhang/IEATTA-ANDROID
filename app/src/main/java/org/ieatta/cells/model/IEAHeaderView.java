package org.ieatta.cells.model;

import android.view.Display;
import android.widget.LinearLayout;

public class IEAHeaderView {
    private int cellHeight;

    public IEAHeaderView(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }
}
