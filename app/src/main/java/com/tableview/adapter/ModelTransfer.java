package com.tableview.adapter;

import com.tableview.adapter.enums.ViewHolderType;

public interface ModelTransfer {
    void render(Object value);

    ViewHolderType getViewHolderType();
}
