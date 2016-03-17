package com.tableview.storage;

import com.tableview.storage.models.RowModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TableViewUtil {
    private HashMap<Integer, Integer> sectionInfo;
    private int rowLength = 0;

    public HashMap<Integer, SectionModel> sections = new LinkedHashMap<>();

    public void generateItems(HashMap<Integer, SectionModel> sections) {
        this.sections = sections;

        Set<Integer> keySet = sections.keySet();
        List<Integer> indexs = new LinkedList<>(keySet);
        Collections.sort(indexs);

        this.sectionInfo = new LinkedHashMap<>();
        this.rowLength = 0;
        for (Integer integer : indexs) {
            Integer itemsCountInSection = this.getItemsCountInSection(integer);
            rowLength += itemsCountInSection;
            sectionInfo.put(integer, itemsCountInSection);
        }
    }

    private Integer getItemsCountInSection(Integer integer) {
        SectionModel sectionModel = this.sections.get(integer);
        return sectionModel.numberOfItems();
    }

    public int getItemCount() {
        return rowLength;
    }


    public RowModel getItem(int position) {
        int total = 0;

        for (Integer key : this.sectionInfo.keySet()) {
            Integer count = this.sectionInfo.get(key);

            int begin = total;
            int end = total + count;

            if ((position + 1) >= begin && (position + 1) <= end) {
                int row = position - total;
                return this.sections.get(key).getRowModel(row);
            }
            total = end;
        }
        return null;
    }

    public int getItemViewType(int position) {
        int total = 0;

        for (Integer key : this.sectionInfo.keySet()) {
            Integer count = this.sectionInfo.get(key);

            int begin = total;
            int end = total + count;

            if ((position + 1) >= begin && (position + 1) <= end) {
                return key.intValue();
            }
            total = end;
        }
        return 0;
    }
}
