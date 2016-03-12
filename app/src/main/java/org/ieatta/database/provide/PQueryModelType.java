package org.ieatta.database.provide;

/**
 * Created by djzhang on 7/15/15.
 */


public enum PQueryModelType {
    Recipe(0),
    Photo(1),
    Team(2),
    Review(3),
    Event(4),
    Restaurant(5),
    NewRecord(6),
    PeopleInEvent(7),
    Waiter(8),// unused
    ServedWaiter(9),//(unused)
    unkown(10);

    private int type;

    PQueryModelType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static PQueryModelType getInstance(int type) {
        switch (type) {
            case 0:
                return Recipe;
            case 1:
                return Photo;
            case 2:
                return Team;
            case 3:
                return Review;
            case 4:
                return Event;
            case 5:
                return Restaurant;
            case 6:
                return NewRecord;
            case 7:
                return PeopleInEvent;
            case 8:
                return Waiter;
            case 9:
                return ServedWaiter;
            case 10:
                return unkown;
        }
        return null;
    }

    @Override
    public String toString() {
        String[] names = {
                "Recipe",
                "Photo",
                "Team",
                "Review",
                "Event",
                "Restaurant",
                "NewRecord",
                "PeopleInEvent",
                "Waiter",
                "ServedWaiter",
                "unkown",
        };
        return names[this.type];
    }
}

