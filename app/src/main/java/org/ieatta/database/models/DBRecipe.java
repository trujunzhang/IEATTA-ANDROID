package com.ieatta.com.parse.engine.realm.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by djzhang on 12/20/15.
 */
public class DBRecipe extends RealmObject {
    @PrimaryKey
    private String UUID = "";
    private Date objectCreatedDate = new Date();
    private String displayName = "";

    private String price = "";

    private String eventRef = "";
    private String orderedPeopleRef = "";

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEventRef() {
        return eventRef;
    }

    public void setEventRef(String eventRef) {
        this.eventRef = eventRef;
    }

    public String getOrderedPeopleRef() {
        return orderedPeopleRef;
    }

    public void setOrderedPeopleRef(String orderedPeopleRef) {
        this.orderedPeopleRef = orderedPeopleRef;
    }
}
