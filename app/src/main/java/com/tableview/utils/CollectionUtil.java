package com.tableview.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CollectionUtil {

    public static List createList(Object item) {
        List list = new LinkedList();
        list.add(item);
        return list;
    }

    public static List createList(Object[] items) {
        List list = new LinkedList(Arrays.asList(items));
        return list;
    }

}
