package org.ieatta.database.models;

import org.ieatta.database.provide.PQueryModelType;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DBNewRecord extends RealmObject {
    @PrimaryKey
    private String UUID = "";
    private Date objectCreatedDate = new Date();

    private int modelType = PQueryModelType.unkown.getType();
    private String modelPoint = "";

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

    public int getModelType() {
        return modelType;
    }

    public void setModelType(int modelType) {
        this.modelType = modelType;
    }

    public String getModelPoint() {
        return modelPoint;
    }

    public void setModelPoint(String modelPoint) {
        this.modelPoint = modelPoint;
    }
}
