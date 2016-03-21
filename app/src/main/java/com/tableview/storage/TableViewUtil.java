package com.tableview.storage;

import com.tableview.storage.models.RowModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TableViewUtil {
    private int rowLength = 0;
    private HashMap<Integer, Integer> sectionCountInfo;

    private SectionModel headerViewSection;
    private SectionModel footerViewSection;
    private HashMap<Integer, SectionModel> sections = new LinkedHashMap<>();

    public void generateItems(HashMap<Integer, SectionModel> sections, SectionModel headerViewSection, SectionModel footerViewSection) {
        this.sections = sections;
        this.headerViewSection = headerViewSection;
        this.footerViewSection = footerViewSection;

        Set<Integer> keySet = sections.keySet();
        List<Integer> indexs = new LinkedList<>(keySet);
        Collections.sort(indexs);

        this.rowLength = 0;
        this.sectionCountInfo = new LinkedHashMap<>();
        for (Integer integer : indexs) {
            Integer count = this.getItemsCountInSection(integer);
            this.rowLength += count;
            sectionCountInfo.put(integer, count);
        }
        if (this.headerViewSection != null)
            this.rowLength++;
        if (this.footerViewSection != null)
            this.rowLength++;
    }

    private Integer getItemsCountInSection(Integer integer) {
        SectionModel sectionModel = this.sections.get(integer);
        return sectionModel.numberOfItems();
    }

    public int getItemCount() {
        return rowLength;
    }

    public RowModel getItem(int position) {
        if (headerViewSection != null) {
            if (position == 0) {
                return headerViewSection.getRowModel(0);
            }
            position++;
        }
        if (footerViewSection != null) {
            if (position == (this.rowLength - 1)) {
                return footerViewSection.getRowModel(0);
            }
        }

        int total = 0;
        for (Integer key : this.sectionCountInfo.keySet()) {
            Integer count = this.sectionCountInfo.get(key);

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

        for (Integer key : this.sectionCountInfo.keySet()) {
            Integer count = this.sectionCountInfo.get(key);

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
