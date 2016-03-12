package org.ieatta.database.provide;

/**
 * Created by djzhang on 11/27/15.
 */

public enum ParseObjectFlag {
    Normal(0),
    deleted(1);

    private int status;

    ParseObjectFlag(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static ParseObjectFlag getInstance(int status){
        switch (status) {
            case 1:
                return deleted;
        }
        return Normal;
    }

    @Override
    public String toString() {
        return this.status==0?"Normal":"deleted";
    }
}
