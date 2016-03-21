package com.tableview.storage;

import com.tableview.TableViewControllerAdapter;
import com.tableview.adapter.NSIndexPath;
import com.tableview.storage.models.CellType;
import com.tableview.storage.models.FooterModel;
import com.tableview.storage.models.HeaderModel;
import com.tableview.storage.models.RowModel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MemoryStorage {
    public TableViewControllerAdapter adapter;
    private TableViewUtil tableViewUtil = new TableViewUtil();
    public CellTypeUtil cellTypeUtil = new CellTypeUtil();

    private SectionModel headerViewSection = new SectionModel();
    private SectionModel footerViewSection = new SectionModel();
    private HashMap<Integer, SectionModel> sections = new LinkedHashMap<>();

    public MemoryStorage(TableViewControllerAdapter adapter) {
        this.adapter = adapter;
    }

    /// Add items to section with `toSection` number.
    /// - Parameter items: items to add
    /// - Parameter toSection: index of section to add items
    public void addItems(List<Object> items, int toSection) {
        // this.sections.put(new Integer(toSection), new SectionModel(items));
    }

    private void reloadTableView(int position) {
        this.tableViewUtil.generateItems(this.sections, headerViewSection, footerViewSection);

        this.adapter.notifyItemChanged(position);
    }

    private void reloadTableView() {
        this.tableViewUtil.generateItems(this.sections, headerViewSection, footerViewSection);

        this.adapter.notifyDataSetChanged();
    }

    public void setHeaderItem(Object item, CellType type) {
        this.headerViewSection.setHeaderModel(new HeaderModel(item, type));
    }

    public void updateHeaderItem(Object newItem) {
        if (this.headerViewSection == null)
            throw new NullPointerException("Not found headerViewSection!");

        this.headerViewSection.getHeaderModel().item = newItem;

        this.reloadTableView(0);
    }

    public void setFooterItem(Object item, CellType type) {
        this.footerViewSection.setFooterModel(new FooterModel(item, type));
    }

    /// Set items for specific section. This will reload UI after updating.
    /// - Parameter items: items to set for section
    /// - Parameter forSectionIndex: index of section to update
    public void setItems(List items, int forSectionIndex) {
        SectionModel section = this.verifySection(forSectionIndex);
        section.items = items;
        this.reloadTableView();
    }

    public void updateItem(Object item, int forSectionIndex, int row) {
        SectionModel section = this.verifySection(forSectionIndex);
        section.items.set(row, item);
        this.reloadTableView(this.getRowPosition(forSectionIndex, row));
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

        // this.reloadTableView();
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

        this.reloadTableView();
    }

    private SectionModel verifySection(int forSectionIndex) {
        SectionModel model = this.sections.get(Integer.valueOf(forSectionIndex));
        if (model != null) {
            return model;
        }
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
        RowModel rowModel = this.getRowModelFromPosition(position);
        return cellTypeUtil.getRowModelType(rowModel);
    }

    public int getRowPosition(int forSectionIndex, int row) {
        int position = 0;
        for (int i = 0; i < forSectionIndex; i++) {
            position += this.sections.get(i).numberOfItems();
        }
        return position + row;
    }

    public RowModel getItem(int position) {
        return this.tableViewUtil.getItem(position);
    }
}
