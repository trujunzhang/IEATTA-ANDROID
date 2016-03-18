package com.tableview;

import android.content.Context;
import android.graphics.drawable.NinePatchDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.tableview.TableViewControllerAdapter;
import com.tableview.adapter.NSIndexPath;
import com.tableview.adapter.RecyclerOnItemClickListener;
import com.tableview.adapter.decoration.TableViewDividerDecoration;
import com.tableview.storage.DTTableViewManager;
import com.tableview.storage.MemoryStorage;
import com.tableview.storage.TableViewConfiguration;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.cells.header.IEAViewForHeaderInSectionCell;
import org.ieatta.cells.model.EditBaseCellModel;

import java.util.List;

public class RecycleViewManager {
    public void onParse(){
        mRecyclerViewDragDropManager.cancelDrag();
    }

    public void onDestroyView(RecyclerView mRecyclerView){
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.release();
            mRecyclerViewDragDropManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
    }

    public DTTableViewManager manager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;


    private void setupDragDropManager(Context context){
        // drag & drop manager
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(context, R.drawable.material_shadow_z3));
    }

    public RecycleViewManager(Context context) {
        setupDragDropManager(context);

        TableViewConfiguration config =
                new TableViewConfiguration.Builder(context)
                        .setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
                        .setItemDecoration(new TableViewDividerDecoration(context))
                        .setDebugInfo("Activity_Table_View")
                        .build();

        this.manager = new DTTableViewManager();

        //adapter
        final TableViewControllerAdapter myItemAdapter = new TableViewControllerAdapter(this.manager);
        mAdapter = myItemAdapter;

        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(myItemAdapter);      // wrap for dragging

        this.manager.setConfiguration(config,myItemAdapter);

        this.setRegisterHeaderClass(IEAViewForHeaderInSectionCell.getType());
    }

    public void setOnItemClickListener(RecyclerOnItemClickListener listener){
        this.manager.configuration.builder.setOnItemClickListener(listener);
    }

    public void startManagingWithDelegate(RecyclerView recyclerView) {
        recyclerView.setAdapter(this.manager.getAdapter());
        recyclerView.setLayoutManager(this.manager.configuration.builder.manager);
        recyclerView.addItemDecoration(this.manager.configuration.builder.decoration);
    }

    public void setRegisterHeaderClass(CellType type) {
        getTableManager().registerHeaderClass(type);
    }

    public void setRegisterFooterClass(CellType type) {
        getTableManager().registerFooterClass(type);
    }

    public void setRegisterCellClassInSpecialRow(CellType type, int forSectionIndex, int forRowIndex) {
        getTableManager().registerCellClassInSpecialRow(type, forSectionIndex, forRowIndex);
    }

    public void setRegisterCellClass(CellType type, int forSectionIndex) {
        getTableManager().registerCellClass(type, forSectionIndex);
    }

    public void setRegisterCellClassWhenSelected(CellType type, int forSectionIndex) {
        getTableManager().registerCellClass(type, forSectionIndex);
    }

    public void setRegisterCellClassWhenSelected(CellType type, int forSectionIndex, int forRowIndex) {
        getTableManager().registerCellClassInSpecialRow(type, forSectionIndex, forRowIndex);
    }


    public void setSectionItems(List items, int forSectionIndex) {
        getMemoryStorage().setItems(items, forSectionIndex);
    }

    public void setFooterModelInSection(Object model, int forSectionIndex, CellType type) {
        getMemoryStorage().setSectionFooterModel(model, forSectionIndex, type);
    }

    public void removeSectionItemsAtIndexPaths(NSIndexPath[] indexPaths) {
        getMemoryStorage().removeItemsAtIndexPaths(indexPaths);
    }

    public void appendSectionTitleCell(EditBaseCellModel cell, int forSectionIndex) {
        this.appendSectionTitleCell(cell, forSectionIndex, IEAViewForHeaderInSectionCell.getType());
    }

    public void appendSectionTitleCell(EditBaseCellModel cell, int forSectionIndex, CellType type) {
        getMemoryStorage().setSectionHeaderModel(cell, forSectionIndex, type);
    }

    public MemoryStorage getMemoryStorage() {
        return getTableManager().memoryStorage;
    }

    public DTTableViewManager getTableManager() {
        return manager;
    }
}
