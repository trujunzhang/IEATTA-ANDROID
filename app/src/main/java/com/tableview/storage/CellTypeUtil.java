package com.tableview.storage;

import com.tableview.storage.models.CellType;
import com.tableview.storage.models.RowModel;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class CellTypeUtil {
    public CellType getModelType(int viewType) {
        Class aClass = this.rowTypes.get(Integer.valueOf(viewType));
        int layResId = this.modelTypes.get(aClass).intValue();
        return new CellType(aClass, layResId);
    }
    /**
     * HashMap:
     * key: cell's type, also is viewType.
     * value: cell's class.
     */
    private HashMap<Integer, Class> rowTypes = new LinkedHashMap<>();

    private HashMap<Class, Integer> modelTypes = new LinkedHashMap<>();

    public void registerType(CellType type) {
        this.modelTypes.put(type.cellClazz, type.layoutResId);

        if (this.isExistRegisterType(type.cellClazz) == false) {
            this.rowTypes.put(this.rowTypes.size(), type.cellClazz);
        }
    }

    public int getRowModelType(RowModel rowModel) {
        if (rowModel == null)
            throw new NullPointerException("Not found rowModel");

        for (Integer index : this.rowTypes.keySet()) {
            Class clazz = this.rowTypes.get(index);
            if (clazz == null)
                throw new NullPointerException("Not found class called "+clazz.getName());

            CellType cellType = rowModel.cellType;
            if (cellType == null)
                throw new NullPointerException("Not found cellType");

            if (cellType.cellClazz.equals(clazz))
                return index.intValue();
        }
        return 0;
    }

    private boolean isExistRegisterType(Class aClass) {
        for (Integer index : this.rowTypes.keySet()) {
            Class clazz = this.rowTypes.get(index);
            if (aClass.equals(clazz))
                return true;
        }
        return false;
    }
}
