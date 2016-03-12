package org.ieatta.database.provide;


/**
 * Created by djzhang on 7/16/15.
 */
public enum ReviewType {
    Review_Restaurant(0),
    Review_Recipe(1),
    Review_Event(2),
    Review_Unknow(3);

    private int type;

    ReviewType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static ReviewType getInstance(int type) {
        switch (type) {
            case 0:
                return Review_Restaurant;
            case 1:
                return Review_Recipe;
            case 2:
                return Review_Event;
            case 3:
                return Review_Unknow;
        }
        return null;
    }
}

