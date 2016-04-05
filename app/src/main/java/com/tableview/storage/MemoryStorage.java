package com.tableview.storage;

import android.content.Intent;

import com.tableview.TableViewControllerAdapter;
import com.tableview.adapter.NSIndexPath;
import com.tableview.storage.models.CellType;
import com.tableview.storage.models.FooterModel;
import com.tableview.storage.models.HeaderModel;
import com.tableview.storage.models.RowModel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MemoryStorage {
    private final static int Header_View_Index = Integer.MIN_VALUE;
    private final static int Footer_View_Index = Integer.MAX_VALUE;
    public TableViewControllerAdapter adapter;
    private TableViewUtil tableViewUtil = new TableViewUtil();
    public CellTypeUtil cellTypeUtil = new CellTypeUtil();

    private HashMap<Integer, SectionModel> sections = new LinkedHashMap<>();

    public MemoryStorage(TableViewControllerAdapter adapter) {
        this.adapter = adapter;
        this.sections.put(Header_View_Index, new SectionModel());
        this.sections.put(Footer_View_Index, new SectionModel());
    }

    public void updateTableSections() {
        this.tableViewUtil.generateItems(this.sections);
    }

    private void reloadTableView(int position) {
        this.updateTableSections();

        this.adapter.notifyItemChanged(position);
    }

    private void reloadTableView(int positionStart, int itemCount) {
        this.updateTableSections();

        this.adapter.notifyItemRangeChanged(positionStart, itemCount);
    }


    public void reloadTableView() {
        this.updateTableSections();

        this.adapter.notifyDataSetChanged();
    }

    public void setHeaderItem(Object item, CellType type) {
        this.sections.get(Header_View_Index).setHeaderModel(new HeaderModel(item, type));
    }

    public void updateHeaderItem(Object newItem) {
        HeaderModel headerModel = this.sections.get(Header_View_Index).getHeaderModel();
        if (headerModel == null)
            throw new NullPointerException("Not found headerViewSection!");

        headerModel.item = newItem;
        this.reloadTableView(0);
    }

    public void setFooterItem(Object item, CellType type) {
        this.sections.get(Footer_View_Index).setFooterModel(new FooterModel(item, type));
    }

    /// Set items for specific section. This will reload UI after updating.
    /// - Parameter items: items to set for section
    /// - Parameter forSectionIndex: index of section to update
    public void setItems(List items, int forSectionIndex) {
        SectionModel section = this.verifySection(forSectionIndex);
        section.items = items;
    }

    public void updateItem(Object item, int forSectionIndex, int row) {
        SectionModel section = this.verifySection(forSectionIndex);
        section.items.set(row, item);
        this.reloadTableView(this.getRowPosition(forSectionIndex, row));
    }

    public void updateItems(List items, int forSectionIndex) {
        SectionModel section = this.verifySection(forSectionIndex);
        section.items = items;
        this.reloadTableView(this.getRowPosition(forSectionIndex, 0), items.size());
    }

    /// Set section header model for MemoryStorage
    /// - Note: This does not update UI
    /// - Parameter model: model for section header at index
    /// - Parameter sectionIndex: index of section for setting header
    public void setSectionHeaderModel(Object model, int forSectionIndex, CellType type) {
        // Step1: Register cell type.
        this.registerCellType(type);

        // Step2: Create/Add a Header Section.
        SectionModel section = this.verifySection(forSectionIndex);
        section.setHeaderModel(new HeaderModel(model, type));
    }

    /// Set section footer model for MemoryStorage
    /// - Note: This does not update UI
    /// - Parameter model: model for section footer at index
    /// - Parameter sectionIndex: index of section for setting footer
    public void setSectionFooterModel(Object model, int forSectionIndex, CellType type) {
        // Step1: Register cell type.
        this.registerCellType(type);

        // Step2: Create/Add a Footer Section.
        SectionModel section = this.verifySection(forSectionIndex);
        section.setFooterModel(new FooterModel(model, type));
    }

    private SectionModel verifySection(int forSectionIndex) {
        SectionModel model = this.sections.get(Integer.valueOf(forSectionIndex));
        if (model != null)
            return model;

        model = new SectionModel(forSectionIndex);
        this.sections.put(forSectionIndex, model);
        return model;
    }

    /// Remove items at index paths.
    /// - Parameter indexPaths: indexPaths to remove item from. Any indexPaths that will not be found, will be skipped
    public void removeItemsAtIndexPaths(NSIndexPath[] indexPaths) {

    }

    public void registerCellType(CellType type) {
        cellTypeUtil.registerType(type);
    }

    public void registerCellClass(CellType type, int forSectionIndex) {
        // Step1: Register class type
        this.registerCellType(type);

        // Step2: Create/Modify a section.
        SectionModel section = this.verifySection(forSectionIndex);
        section.cellType = type;
    }

    public void registerCellClassInSpecialRow(CellType type, int forSectionIndex, int forRowIndex) {
        // Step1: Register class type
        this.registerCellType(type);

        // Step2: Create/Modify a section.
        SectionModel section = this.verifySection(forSectionIndex);
        section.specialRows.put(forRowIndex, type);
    }

    public RowModel getRowModelFromPosition(int position) {
        return this.tableViewUtil.getItem(position);
    }

    public Object getRowModel(int position) {
        RowModel rowModel = this.getRowModelFromPosition(position);
        return rowModel.model;
    }

    public int getItemCount() {
        return tableViewUtil.getItemCount();
    }

    public int getItemViewType(int position) {
        RowModel rowModel = this.tableViewUtil.getItem(position);
        if (rowModel == null)
            throw new NullPointerException("Not found rowModel");

        return cellTypeUtil.getRowModelType(rowModel);
    }

    public int getRowPosition(int forSectionIndex, int row) {
        int position = 0;
        List<Integer> keys = new LinkedList<>(this.sections.keySet());

        int section = forSectionIndex;
        if (this.sections.get(Header_View_Index).numberOfItems() != 0)
            section = forSectionIndex + 1;

        for (int i = 0; i < section; i++) {
            SectionModel sectionModel = this.sections.get(keys.get(i));
            position += sectionModel.numberOfItems();
        }
        return position + row;
    }

    public RowModel getItem(int position) {
        return this.tableViewUtil.getItem(position);
    }
}
