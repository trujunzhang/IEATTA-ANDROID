package com.ieatta.com.parse.engine.realm.models;

import com.ieatta.com.parse.models.Team;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by djzhang on 12/20/15.
 */
public class DBTeam extends RealmObject {
    @PrimaryKey
    private String UUID = "";
    private Date objectCreatedDate = new Date();
    private String displayName = "";

    private String email = "";
    private String address = "";

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
