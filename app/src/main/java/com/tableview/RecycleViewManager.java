package com.tableview;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;

import com.tableview.adapter.NSIndexPath;
import com.tableview.storage.DTTableViewManager;
import com.tableview.storage.MemoryStorage;
import com.tableview.storage.TableViewConfiguration;
import com.tableview.storage.models.CellType;

import org.ieatta.cells.header.IEAViewForHeaderInSectionCell;
import org.ieatta.cells.model.EditBaseCellModel;

import java.util.List;

public class RecycleViewManager {
    private DTTableViewManager mProvider = new DTTableViewManager();
    private TableViewControllerAdapter mAdapter;

    @VisibleForTesting
    public RecycleViewManager() {
    }

    public RecycleViewManager(Context context) {
        TableViewConfiguration config =
                new TableViewConfiguration.Builder(context)
                        .setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
                        //.setItemDecoration(new TableViewDividerDecoration(context))
                        .setDebugInfo("Activity_Table_View")
                        .build();

        //adapter
        this.mAdapter = new TableViewControllerAdapter(this.mProvider);
        this.mProvider.setConfiguration(config, mAdapter);

        this.setRegisterHeaderClass(IEAViewForHeaderInSectionCell.getType());
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mAdapter.setOnItemClickListener(listener);
    }

    public void startManagingWithDelegate(RecyclerView recyclerView) {
        recyclerView.setAdapter(this.mProvider.getAdapter());
        recyclerView.setLayoutManager(this.mProvider.configuration.builder.manager);
        if (this.mProvider.configuration.builder.decoration != null)
            recyclerView.addItemDecoration(this.mProvider.configuration.builder.decoration);
    }

    public void setRegisterHeaderClass(CellType type) {
        getProvider().registerHeaderClass(type);
    }

    public void setRegisterFooterClass(CellType type) {
        getProvider().registerFooterClass(type);
    }

    public void setRegisterCellClassInSpecialRow(CellType type, int forSectionIndex, int forRowIndex) {
        getProvider().registerCellClassInSpecialRow(type, forSectionIndex, forRowIndex);
    }

    public void setRegisterHeaderView(CellType type) {
        getProvider().setRegisterHeaderView(type);
    }

    public void setRegisterFooterView(CellType type) {
        getProvider().setRegisterFooterView(type);
    }

    public void setHeaderItem(Object item, CellType type) {
        getMemoryStorage().setHeaderItem(item, type);
    }

    public void updateHeaderItem(Object item) {
        getMemoryStorage().updateHeaderItem(item);
    }

    public void setFooterItem(Object item, CellType type) {
        getMemoryStorage().setFooterItem(item, type);
    }

    public void setRegisterCellClass(CellType type, int forSectionIndex) {
        getProvider().registerCellClass(type, forSectionIndex);
    }

    public void setRegisterCellClassWhenSelected(CellType type, int forSectionIndex) {
        getProvider().registerCellClass(type, forSectionIndex);
    }

    public void setRegisterCellClassWhenSelected(CellType type, int forSectionIndex, int forRowIndex) {
        getProvider().registerCellClassInSpecialRow(type, forSectionIndex, forRowIndex);
    }

    public void updateSectionItem(Object item, int forSectionIndex, int row) {
        getMemoryStorage().updateItem(item, forSectionIndex, row);
    }

    public void updateSectionItems(List items, int forSectionIndex) {
        getMemoryStorage().updateItems(items, forSectionIndex);
    }

    public void setAndRegisterSectionItems(CellType type, List items, int forSectionIndex) {
        this.setRegisterCellClass(type, forSectionIndex);
        getMemoryStorage().setItems(items, forSectionIndex);
    }

    public void setSectionItems(List items, int forSectionIndex) {
        getMemoryStorage().setItems(items, forSectionIndex);
    }

    public void setFooterModelInSection(Object model, int forSectionIndex, CellType type) {
        getProvider().registerFooterClass(type);
        getMemoryStorage().setSectionFooterModel(model, forSectionIndex, type);
    }

    public void removeSectionItemsAtIndexPaths(NSIndexPath[] indexPaths) {
        getMemoryStorage().removeItemsAtIndexPaths(indexPaths);
    }

    public void appendAndRegisterSectionTitleCell(CellType type, EditBaseCellModel cell, int forSectionIndex) {
        this.setRegisterCellClass(type, forSectionIndex);
        this.appendSectionTitleCell(cell, forSectionIndex, IEAViewForHeaderInSectionCell.getType());
    }

    public void appendSectionTitleCell(EditBaseCellModel cell, int forSectionIndex) {
        this.appendSectionTitleCell(cell, forSectionIndex, IEAViewForHeaderInSectionCell.getType());
    }

    public void appendSectionTitleCell(EditBaseCellModel cell, int forSectionIndex, CellType type) {
        getMemoryStorage().setSectionHeaderModel(cell, forSectionIndex, type);
    }

    public MemoryStorage getMemoryStorage() {
        return getProvider().memoryStorage;
    }

    public DTTableViewManager getProvider() {
        return mProvider;
    }

    public void updateTableSections() {
        this.getMemoryStorage().updateTableSections();
    }

    public void reloadTableView() {
        this.getMemoryStorage().reloadTableView();
    }
}
