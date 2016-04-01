package org.ieatta.cells.model;

import org.ieatta.database.provide.ReviewType;
import org.ieatta.provide.IEAEditKey;

public class SectionPhotoGalleryFooterCellModel extends EditBaseCellModel {
    public String reviewRef;
    public ReviewType type;

    public SectionPhotoGalleryFooterCellModel(String reviewRef, ReviewType type) {
        super(IEAEditKey.Section_Title);
        this.reviewRef = reviewRef;
        this.type = type;
    }

}
