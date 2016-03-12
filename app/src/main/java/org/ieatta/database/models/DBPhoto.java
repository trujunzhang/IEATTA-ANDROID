package com.ieatta.com.parse.engine.realm.models;

import com.ieatta.com.parse.models.enums.PhotoUsedType;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by djzhang on 12/20/15.
 */
public class DBPhoto extends RealmObject {
    @PrimaryKey
    private String UUID = "";
    private Date objectCreatedDate = new Date();

    // Required
    private String restaurantRef = "";

    private String usedRef = "";
    private int usedType = PhotoUsedType.Photo_Used_Unknow.ordinal();

    // MARK: Variable for pushing to server.
    private String originalUrl = "";
    private String thumbnailUrl = "";

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

    public String getRestaurantRef() {
        return restaurantRef;
    }

    public void setRestaurantRef(String restaurantRef) {
        this.restaurantRef = restaurantRef;
    }

    public String getUsedRef() {
        return usedRef;
    }

    public void setUsedRef(String usedRef) {
        this.usedRef = usedRef;
    }

    public int getUsedType() {
        return usedType;
    }

    public void setUsedType(int usedType) {
        this.usedType = usedType;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
