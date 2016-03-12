package org.ieatta.database.models;

import org.ieatta.database.provide.ReviewType;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DBReview extends RealmObject {
    @PrimaryKey
    private String UUID = "";
    private Date objectCreatedDate = new Date();

    private String content = "";

    /// Rate valuefrom 1 to 5
    private int rate = 1;
    private String userRef = "";// Point of Team
    private String reviewRef = "";
    private int reviewType = ReviewType.Review_Unknow.getType();

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Date getObjectCreatedDate() {
        return objectCreatedDate;
    }

    public void setObjectCreatedDate(Date objectCreatedDate) {
        this.objectCreatedDate = objectCreatedDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }

    public String getReviewRef() {
        return reviewRef;
    }

    public void setReviewRef(String reviewRef) {
        this.reviewRef = reviewRef;
    }

    public int getReviewType() {
        return reviewType;
    }

    public void setReviewType(int reviewType) {
        this.reviewType = reviewType;
    }
}
