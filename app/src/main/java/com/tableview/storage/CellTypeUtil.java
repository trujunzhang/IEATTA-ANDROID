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
        modelTypes.put(type.cellClass, type.layoutResId);

        if (this.isExistRegisterType(type.cellClass) == false) {
            int size = this.rowTypes.size();
            this.rowTypes.put(Integer.valueOf(size), type.cellClass);
        }
    }

    public int getRowModelType(RowModel rowModel) {
        for (Integer index : this.rowTypes.keySet()) {
            Class clazz = this.rowTypes.get(index);
            if (clazz == null) {
                int x = 0;
//                assert("not found class type: "+clazz.getName()+",you need to register it.");
            }
            if (rowModel.cellType.cellClass.equals(clazz)) {
                return index.intValue();
            }
        }
        return 0;
    }

    private boolean isExistRegisterType(Class aClass) {
        for (Integer index : this.rowTypes.keySet()) {
            Class clazz = this.rowTypes.get(index);
            if (aClass.equals(clazz)) {
                return true;
            }
        }
        return false;
    }
}
