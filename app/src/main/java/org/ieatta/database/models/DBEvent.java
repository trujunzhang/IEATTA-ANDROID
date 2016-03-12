package org.ieatta.database.models;

import org.ieatta.database.provide.ParseObjectFlag;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DBEvent extends RealmObject {
    @PrimaryKey
    private String UUID = "";
    private Date objectCreatedDate = new Date();
    private String displayName = "";

    private Date startDate = new Date();
    private Date endDate;
    private String whatToEat = "";
    private String remarks = "";
    private String waiter = "";
    private String restaurantRef = "";

    private int flag = ParseObjectFlag.Normal.getStatus();

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getWhatToEat() {
        return whatToEat;
    }

    public void setWhatToEat(String whatToEat) {
        this.whatToEat = whatToEat;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public String getRestaurantRef() {
        return restaurantRef;
    }

    public void setRestaurantRef(String restaurantRef) {
        this.restaurantRef = restaurantRef;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
