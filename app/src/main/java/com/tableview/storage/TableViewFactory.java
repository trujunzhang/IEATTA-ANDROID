package com.tableview.storage;

import android.view.View;
import java.lang.reflect.Constructor;

public class TableViewFactory {
    public static Constructor getConstructorForView(Constructor[] ctors) {
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 1) {
                Class[] types = ctor.getParameterTypes();
                boolean isView = verifyParaWithView(types);
                if (isView == true) {
                    return ctor;
                }
            }
        }
        return ctor;
    }

    public static boolean verifyParaWithView(Class[] types) {
        for (int i = 0; i < types.length; i++) {
            Class type = types[i];
            if (type.equals(View.class)) {
                return true;
            }
        }
        return false;
    }
}
