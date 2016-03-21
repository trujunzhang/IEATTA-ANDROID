package com.tableview.storage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tableview.adapter.RecyclerOnItemClickListener;
import com.tableview.TableViewControllerAdapter;
import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DTTableViewManager {
    public TableViewConfiguration configuration;
    public MemoryStorage memoryStorage;

    public void setConfiguration(TableViewConfiguration configuration, TableViewControllerAdapter adapter){
        this.configuration = configuration;
        this.memoryStorage = new MemoryStorage(adapter);
    }

    public RecyclerOnItemClickListener getOnItemClickListener(){
        return configuration.builder.itemOnClickListener;
    }

    public TableViewControllerAdapter getAdapter() {
        return this.memoryStorage.adapter;
    }

    public int getItemCount() {
        return this.memoryStorage.getItemCount();
    }

    public void registerHeaderClass(CellType type) {
        this.memoryStorage.cellTypeUtil.registerType(type);
    }

    public void registerFooterClass(CellType type) {
        this.memoryStorage.cellTypeUtil.registerType(type);
    }

    public IEAViewHolder createViewHolder(ViewGroup parent, int viewType) {
        CellType cellType = this.memoryStorage.cellTypeUtil.getModelType(viewType);
        Class cellClass = cellType.cellClazz;
        int layoutResId = cellType.layoutResId;

        Constructor[] ctors = cellClass.getDeclaredConstructors();
        Constructor viewConstructor = TableViewFactory.getConstructorForView(ctors);
        View view = LayoutInflater.from(this.configuration.builder.context).inflate(layoutResId, parent, false);

        IEAViewHolder viewHolder = null;
        try {
            viewHolder = (IEAViewHolder) viewConstructor.newInstance(new Object[]{view});
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return viewHolder;
    }

    public void registerCellClass(CellType type, int forSectionIndex) {
        this.memoryStorage.registerCellClass(type, forSectionIndex);
    }

    public void registerCellClassInSpecialRow(CellType type, int forSectionIndex, int forRowIndex) {
        this.memoryStorage.registerCellClassInSpecialRow(type, forSectionIndex, forRowIndex);
    }

    public void setRegisterHeaderView(CellType type) {
        this.memoryStorage.registerCellType(type);
    }

    public void setRegisterFooterView(CellType type) {
        this.memoryStorage.registerCellType(type);
    }
}
