package org.ieatta.cells.model;

import org.ieatta.database.models.DBReview;
import org.ieatta.provide.IEAEditKey;

public class ReviewsCellModel extends EditBaseCellModel {
    public String reviewUUID;
    public String userUUID;
    public String title;

    public String timeAgoString ;
    public int ratingValue ;
    public String reviewContent ;

    public ReviewsCellModel(IEAEditKey editKey, Object user, Object review) {
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
