package org.ieatta.cells.model;

import org.ieatta.database.provide.ReviewType;
import org.ieatta.provide.IEAEditKey;

public class SectionMoreReviewsFooterCellModel extends EditBaseCellModel {
    public int otherReviewCount;

    public SectionMoreReviewsFooterCellModel(int otherReviewCount) {
        super(IEAEditKey.Section_Title);
        this.otherReviewCount = otherReviewCount;
    }

}
