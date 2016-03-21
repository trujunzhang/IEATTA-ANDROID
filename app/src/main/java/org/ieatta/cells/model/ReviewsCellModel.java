package org.ieatta.cells.model;

import org.ieatta.database.models.DBReview;
import org.ieatta.database.utils.DBUtil;
import org.ieatta.provide.IEAEditKey;

public class ReviewsCellModel extends EditBaseCellModel {
    public String reviewUUID;
    public String userUUID;
    public String title;

    public String timeAgoString ;
    public int ratingValue ;
    public String reviewContent ;

    public ReviewsCellModel(DBReview review) {
        super(IEAEditKey.Unknow);

        this.reviewUUID = review.getUUID();
        this.ratingValue = review.getRate();
        this.reviewContent = review.getContent();
        this.timeAgoString = DBUtil.getTimeAgoString(review.getObjectCreatedDate());



//        this.user = (Team) user;
//
//        this.writedReview = (Review) review;
//
//        this.timeAgoString = writedReview.getTimeAgoString();
//        this.ratingValue = writedReview.rate;
//        this.reviewContent = writedReview.content;
    }

}
