package org.ieatta.cells.model;

import org.ieatta.database.models.DBReview;
import org.ieatta.provide.IEAEditKey;

public class SectionSeeReviewsCellModel extends EditBaseCellModel {
    public DBReview writedReview ;

    public String timeAgoString ;
    public int ratingValue ;
    public String reviewContent ;

    public SectionSeeReviewsCellModel(IEAEditKey editKey, Object user, Object review) {
        super(editKey);

//        this.user = (Team) user;
//
//        this.writedReview = (Review) review;
//
//        this.timeAgoString = writedReview.getTimeAgoString();
//        this.ratingValue = writedReview.rate;
//        this.reviewContent = writedReview.content;
    }

}
